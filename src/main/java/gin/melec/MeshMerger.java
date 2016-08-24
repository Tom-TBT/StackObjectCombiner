/*
 * Copyright (C) 2015 ImageJ
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package gin.melec;

import static gin.melec.CustomFrame.appendToLog;
import ij.IJ;
import ij.gui.GenericDialog;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class MeshMerger {

    private MeshMerger() {
    }

    public static MeshMerger getInstance() {
        return MeshMergerHolder.INSTANCE;
    }

    private static class MeshMergerHolder {
        private static final MeshMerger INSTANCE = new MeshMerger();
    }

    public static void merge() {
        final Mesh mesh1 = DialogContentManager.ACTIVE_MESH_1;
        final Mesh mesh2 = DialogContentManager.ACTIVE_MESH_2;
        try {
            mesh1.importMesh(true);
            mesh2.importMesh(true);
        } catch (ParseException ex) {
            IJ.handleException(ex);
        } catch (IOException ex) {
            IJ.handleException(ex);
        }

        Thread thread1 = new Thread() {
            {
                setPriority(Thread.NORM_PRIORITY);
            }

            @Override
            public void run() {
                CustomFrame.appendToLog("Working on " + mesh1.getFile().getName());
                mesh1.findPrimers();
                mesh1.createBorders(DialogContentManager.ACTIVE_SPLIT);
                final List<Border> tmpBorders = new ArrayList();
                for (Border border : mesh1.getBorders()) {
                    tmpBorders.addAll(border.separateSubBorders());
                }
                mesh1.setBorders(tmpBorders);
                int numberOfCircular = 0;
                for (Border border:mesh1.getBorders()) {
                    if(border.isCircular()) {
                        numberOfCircular++;
                    }
                }
                CustomFrame.appendToLog(mesh1.getBorders().size() +
                        " borders detected for "+ mesh1.getFile().getName()+
                        ", "+(mesh1.getBorders().size()-numberOfCircular) +
                        " linears and "+numberOfCircular+" circulars");
            }
        };
        Thread thread2 = new Thread() {
            {
                setPriority(Thread.NORM_PRIORITY);
            }

            @Override
            public void run() {
                AbstractSplit tmpSplit;
                if (WidthSplit.class.isInstance(DialogContentManager.ACTIVE_SPLIT)) {
                    tmpSplit = new WidthSplit(DialogContentManager.ACTIVE_SPLIT);
                } else if (HeightSplit.class.isInstance(DialogContentManager.ACTIVE_SPLIT)) {
                    tmpSplit = new HeightSplit(DialogContentManager.ACTIVE_SPLIT);
                } else {
                    tmpSplit = new DepthSplit(DialogContentManager.ACTIVE_SPLIT);
                }
                CustomFrame.appendToLog("Working on " + mesh2.getFile().getName());
                mesh2.findPrimers();
                mesh2.createBorders(tmpSplit);
                final List<Border> tmpBorders = new ArrayList();
                for (Border border : mesh2.getBorders()) {
                    tmpBorders.addAll(border.separateSubBorders());
                }
                mesh2.setBorders(tmpBorders);
                int numberOfCircular = 0;
                for (Border border:mesh2.getBorders()) {
                    if(border.isCircular()) {
                        numberOfCircular++;
                    }
                }
                CustomFrame.appendToLog(mesh2.getBorders().size() +
                        " borders detected for "+ mesh2.getFile().getName()+
                        ", "+(mesh2.getBorders().size()-numberOfCircular) +
                        " linears and "+numberOfCircular+" circulars");
            }
        };
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException ex) {
            IJ.handleException(ex);
        }

        boolean bordersDetected = true;
        if (mesh1.getBorders().isEmpty()) {
            CustomFrame.appendToLog("Error : " + mesh1.getFile().getName()
                    + " don't cross to the border");
            bordersDetected = false;
        }
        if (mesh2.getBorders().isEmpty()) {
            CustomFrame.appendToLog("Error : " + mesh2.getFile().getName()
                + " don't cross to the border");
            bordersDetected = false;
        }
        if (bordersDetected) {
            final Set<Border[]> couples = pairBorders(mesh1.getBorders(),
                    mesh2.getBorders());
            final List<Face> newFaces = new ArrayList();
            CustomFrame.appendToLog("Pairing the borders");
            int compatibleLinear = 0;
            int compatibleCircular = 0;
            for (Border[] couple : couples) {
                if (couple[0].similarityTo(couple[1]) != 0) {
                    if (couple[0].isCircular()) compatibleCircular++;
                    else compatibleLinear++;
                    couple[0].alignOn(couple[1]);
                    newFaces.addAll(Linker.createFacesBetween(couple[0].getVertexSequence(), couple[1].getVertexSequence(), couple[0].isCircular()));
                }
            }
            if (newFaces.isEmpty()) {
                CustomFrame.appendToLog("No borders have been found compatible.");
            } else {
                CustomFrame.appendToLog(compatibleLinear+" linear and "+compatibleCircular+" circular borders have been merged");
                try {
                    ObjWriter.exportFusion(mesh1, mesh2, newFaces);
                } catch (IOException ex) {
                    IJ.handleException(ex);
                }
            }
            mesh1.unloadEverything();
            mesh2.unloadEverything();
            Runtime.getRuntime().gc();
        }
    }

    /**
     * Pair the borders of the two meshes switch their distances. Distances are
     * computed with the lenght of the border and their position. Also check
     * that the borders are the same type (linear can not be paired with a
     * circular border).
     *
     * @param borders1 , the borders of the first mesh.
     * @param borders2 , the borders of the second mesh.
     * @return a set of paired borders.
     */
    private static Set<Border[]> pairBorders(List<Border> borders1,
            List<Border> borders2) {
        Set result = new HashSet();
        boolean interchanged = false;
        if (borders1.size() > borders2.size()) {
            final List<Border> tmp = borders1;
            borders1 = borders2;
            borders2 = tmp;
            interchanged = true;
        }
        final List<List<Border>> orderCloserFromB1s = orderCloserBorders(borders1,
                borders2);
        if (orderCloserFromB1s != null) {
            for (int i = 0; i < orderCloserFromB1s.size(); i++) {
                final Border border1 = borders1.get(i);
                final Border border2 = orderCloserFromB1s.get(i).get(0);
                final Border[] borderArray = new Border[2];
                if (interchanged) {
                    borderArray[0] = border2;
                    borderArray[1] = border1;
                } else {
                    borderArray[0] = border1;
                    borderArray[1] = border2;
                }
                result.add(borderArray);
            }
        }
        return result;
    }

    /**
     * For each border1, order the borders2 in an array switch their distance to
     * the border1.
     *
     * @param borders1 , the reference borders.
     * @param borders2 , the borders ordered.
     * @return a list containing the borders ordered by distance.
     */
    private static List orderCloserBorders(final List<Border> borders1,
            final List<Border> borders2) {
        final List<List> result = new ArrayList();
        for (Border border1 : borders1) {
            final List<Border> distances = new ArrayList();
            for (Border border2 : borders2) {
                if (distances.isEmpty()) {
                    distances.add(border2);
                } else {
                    int i = 0;
                    final double distance = border2.similarityTo(border1);
                    while (i < distances.size() && distance
                            < distances.get(i).similarityTo(border1)) {
                        i++;
                    }
                    distances.add(i, border2);
                }
            }
            if (!distances.isEmpty())
                result.add(distances);
        }
        return result.isEmpty()?null:result;
    }

    public static void workOnCubes(String workingDir) {
        DialogContentManager.CUBE_A.detectMeshBorders();
        DialogContentManager.CUBE_B.detectMeshBorders();
        DialogContentManager.CUBE_C.detectMeshBorders();
        DialogContentManager.CUBE_D.detectMeshBorders();
        DialogContentManager.CUBE_E.detectMeshBorders();
        DialogContentManager.CUBE_F.detectMeshBorders();
        DialogContentManager.CUBE_G.detectMeshBorders();
        DialogContentManager.CUBE_H.detectMeshBorders();

        java.util.List<Couple> couples = createCouples();

        File saveDirectory = new File(workingDir + "SOC - AutoMerge Result");
        saveDirectory.mkdir();

        int i = 1;
        while (couples.size() > 0) {
            Set<Mesh> meshToFuse = new HashSet();
            Set<Couple> coupleToFuse = new HashSet();
            getCouplesAndMeshToFuse(meshToFuse, coupleToFuse, couples, couples.get(0));

            List<Vertex> vertices = new ArrayList();
            List<Face> faces = new ArrayList();
            String message = "The meshes ";
            for (Mesh mesh: meshToFuse) {
                try {
                    mesh.reload();
                } catch (ParseException ex) {
                    IJ.handleException(ex);
                } catch (IOException ex) {
                    IJ.handleException(ex);
                }
                message = message.concat(mesh.toString() + ", ");
                mesh.incremVertices(vertices.size());
                vertices.addAll(mesh.getVertices());
                faces.addAll(mesh.getFaces());
                mesh.unloadEverything();
            }
            Runtime.getRuntime().gc();
            message = message.concat("are compatible");
            CustomFrame.appendToLog(message);

            List<Vertex> endPoints = new ArrayList();
            List<Face> endFaces = new ArrayList();
            String toPrint = null;
            for (Couple couple:coupleToFuse) {
                if (toPrint == null) {
                    toPrint = couple.getMesh1().toString() + " with " + couple.getMesh2().toString();
                } else {
                    toPrint = toPrint.concat("  "+couple.getMesh1().toString() + " with " + couple.getMesh2().toString());
                    CustomFrame.appendToLog(toPrint);
                    toPrint = null;
                }
                couple.alignFlats();
                couple.merge();
                endPoints.addAll(couple.getEndPoints());
                endFaces.addAll(couple.getEndFaces());
                faces.addAll(couple.getNewFaces());
            }
            if (toPrint != null) {
                CustomFrame.appendToLog(toPrint);
            }
            faces.addAll(getFillHoles(endPoints, endFaces));
            try {
                String newName = "Mesh-"+i+".obj";
                if (!ObjWriter.AUTOSAVE) {
                    final GenericDialog gDial = new GenericDialog("Enter the name of the "
                        + "new mesh");
                    gDial.addStringField("Name", newName, 30);
                    gDial.hideCancelButton();
                    gDial.showDialog();
                    newName = gDial.getNextString();
                }
                ObjWriter.writeResult(vertices, faces, newName, saveDirectory);
            } catch (IOException ex) {
                CustomFrame.appendToLog("Error while writing the new file");
                IJ.handleException(ex);
            }
            couples.removeAll(coupleToFuse);
            i++;
        }
    }

    private static List<Face> getFillHoles(List<Vertex> endPoints, List<Face> endFaces) {
        List<Face> result = new ArrayList();
        Set<Vertex> alreadyChecked = new HashSet();

        for (Vertex startVertex: endPoints) {
            List<Vertex> quatuor = new ArrayList();
            if (alreadyChecked.add(startVertex)) {
                boolean notFind = true;
                quatuor.add(startVertex);
                Vertex currentVertex = null, prevVertex = startVertex;
                for (Face face: endFaces) {
                    if (face.include(prevVertex)) {
                        currentVertex = face.getFirstNeighbour(prevVertex);
                        if (!endPoints.contains(currentVertex)) {
                            currentVertex = face.getSecondNeighbour(prevVertex);
                        }
                        notFind = false;
                        break;
                    }
                }
                // End of initialisation

                while (currentVertex != startVertex && !notFind) {
                    quatuor.add(currentVertex);
                    alreadyChecked.add(currentVertex);
                    notFind = true;
                    for (Face face: endFaces) {
                        if (face.include(currentVertex) && !face.include(prevVertex)) {
                            prevVertex = currentVertex;
                            currentVertex = face.getFirstNeighbour(prevVertex);
                            if (!endPoints.contains(currentVertex)) {
                                currentVertex = face.getSecondNeighbour(prevVertex);
                            }
                            notFind = false;
                            break;
                        }
                    }
                }
                if (!notFind) {
                    result.add(new Face(quatuor.get(0), quatuor.get(1), quatuor.get(3)));
                    result.add(new Face(quatuor.get(1), quatuor.get(2), quatuor.get(3)));
                }
            }
        }
        return result;
    }

    private static void getCouplesAndMeshToFuse(Set<Mesh> meshResult, Set<Couple> coupleResult,
            List<Couple> couples, Couple initCouple) {
        Mesh mesh1 = initCouple.getMesh1();
        Mesh mesh2 = initCouple.getMesh2();
        if (!meshResult.contains(mesh1)) {
            meshResult.add(mesh1);
            for (Couple currCouple: couples) {
                if (currCouple.contain(mesh1)) {
                    coupleResult.add(currCouple);
                    getCouplesAndMeshToFuse(meshResult, coupleResult, couples, currCouple);
                }
            }
        }
        if (!meshResult.contains(mesh2)) {
            meshResult.add(mesh2);
            for (Couple currCouple: couples) {
                if (currCouple.contain(mesh2)) {
                    coupleResult.add(currCouple);
                    getCouplesAndMeshToFuse(meshResult, coupleResult, couples, currCouple);
                }
            }
        }
    }

    private static java.util.List<Couple> createCouples() {
        java.util.List<Couple> result= new ArrayList();

        result.addAll(getCouples(DialogContentManager.CUBE_A, DialogContentManager.CUBE_B, 'R', 'L'));
        result.addAll(getCouples(DialogContentManager.CUBE_C, DialogContentManager.CUBE_D, 'R', 'L'));
        result.addAll(getCouples(DialogContentManager.CUBE_A, DialogContentManager.CUBE_C, 'F', 'B'));
        result.addAll(getCouples(DialogContentManager.CUBE_B, DialogContentManager.CUBE_D, 'F', 'B'));

        result.addAll(getCouples(DialogContentManager.CUBE_E, DialogContentManager.CUBE_F, 'R', 'L'));
        result.addAll(getCouples(DialogContentManager.CUBE_G, DialogContentManager.CUBE_H, 'R', 'L'));
        result.addAll(getCouples(DialogContentManager.CUBE_E, DialogContentManager.CUBE_G, 'F', 'B'));
        result.addAll(getCouples(DialogContentManager.CUBE_F, DialogContentManager.CUBE_H, 'F', 'B'));

        result.addAll(getCouples(DialogContentManager.CUBE_A, DialogContentManager.CUBE_E, 'D', 'U'));
        result.addAll(getCouples(DialogContentManager.CUBE_B, DialogContentManager.CUBE_F, 'D', 'U'));
        result.addAll(getCouples(DialogContentManager.CUBE_C, DialogContentManager.CUBE_G, 'D', 'U'));
        result.addAll(getCouples(DialogContentManager.CUBE_D, DialogContentManager.CUBE_H, 'D', 'U'));

        keepOneCouplePerBorder(result);
        return result;
    }

    private static void keepOneCouplePerBorder(List<Couple> couples) {
        Set<Couple> toRemove = new HashSet();
        Set<FlatBorder> bordersToCheck = new HashSet();

        // Creation of the list of all the flats seen in the couples
        for (Couple couple: couples) {
            bordersToCheck.add(couple.getFlat1());
            bordersToCheck.add(couple.getFlat2());
        }

        // Now we will look for the flats that appear more than once, and remove
        // the couple where they have the weakest similarity
        for (FlatBorder currentFlat: bordersToCheck) {
            List<Couple> flatCouples = new ArrayList();

            // Adding to a list all the couples of the current flat
            for (Couple couple: couples) {
                if (couple.contain(currentFlat)) {
                    flatCouples.add(couple);
                }
            }

            // If the current flat appear more than one time
            if (flatCouples.size() > 1) {
                Couple bestSimilarityCouple = flatCouples.get(0);
                double bestSimilarity = bestSimilarityCouple.getSimilarity();
                int i = 0;
                // Look for the couple with the best affinity
                while (i < flatCouples.size()) {
                    if (flatCouples.get(i).getSimilarity() > bestSimilarity) {
                        bestSimilarityCouple = flatCouples.get(i);
                        bestSimilarity = bestSimilarityCouple.getSimilarity();
                    }
                    i++;
                }
                // All couples are added to the remove, except the one with the best affinity
                flatCouples.remove(bestSimilarityCouple);
                toRemove.addAll(flatCouples);
            }
        }
        couples.removeAll(toRemove);
    }

    private static java.util.List<Couple> getCouples(Cube cube1, Cube cube2,
            char split1, char split2) {
        java.util.List<Couple> result = new ArrayList();
        java.util.List<FlatBorder> flats1 = null;
        java.util.List<FlatBorder> flats2 = null;
        for (Mesh mesh1 : cube1.getMeshes()) {
            switch (split1) {
                case 'R':
                    flats1 = mesh1.getRightFlats();
                    break;
                case 'L':
                    flats1 = mesh1.getLeftFlats();
                    break;
                case 'U':
                    flats1 = mesh1.getUpFlats();
                    break;
                case 'D':
                    flats1 = mesh1.getDownFlats();
                    break;
                case 'F':
                    flats1 = mesh1.getFrontFlats();
                    break;
                case 'B':
                    flats1 = mesh1.getBackFlats();
                    break;
            }
            for (Mesh mesh2 : cube2.getMeshes()) {
                switch (split2) {
                    case 'R':
                        flats2 = mesh2.getRightFlats();
                        break;
                    case 'L':
                        flats2 = mesh2.getLeftFlats();
                        break;
                    case 'U':
                        flats2 = mesh2.getUpFlats();
                        break;
                    case 'D':
                        flats2 = mesh2.getDownFlats();
                        break;
                    case 'F':
                        flats2 = mesh2.getFrontFlats();
                        break;
                    case 'B':
                        flats2 = mesh2.getBackFlats();
                        break;
                }
                java.util.List<Couple> tmpList = new ArrayList();
                tmpList.addAll(createCouples(flats1, flats2));
                removeUncompatibleCouples(tmpList);
                result.addAll(tmpList);
            }
        }
        return result;
    }

    private static void removeUncompatibleCouples(List<Couple> couples) {
        List<Couple> toRemove = new ArrayList();
        for (Couple couple: couples) {
            if (!couple.compatible()) {
                toRemove.add(couple);
            }
        }
        couples.removeAll(toRemove);
    }


    private static java.util.List<Couple> createCouples(java.util.List<FlatBorder> flats1,
            java.util.List<FlatBorder> flats2) {
        java.util.List<Couple> result = new ArrayList();
        for (FlatBorder flat1: flats1) {
            for (FlatBorder flat2: flats2) {
                result.add(new Couple(flat1, flat2));
            }
        }
        return result;
    }
}
