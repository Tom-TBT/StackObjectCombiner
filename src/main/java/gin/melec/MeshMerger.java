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

import ij.IJ;
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
            mesh1.importMesh();
            mesh2.importMesh();
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
                mesh1.createBorders(DialogContentManager.ACTIVE_SPLIT);
                final List<Border> tmpBorders = new ArrayList();
                for (Border border : mesh1.getBorders()) {
                    tmpBorders.addAll(border.separateSubBorders());
                }
                mesh1.addBorders(tmpBorders);
                CustomFrame.appendToLog(mesh1.getBorders().size() + " borders detected for "
                    + mesh1.getFile().getName());
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
                mesh2.createBorders(tmpSplit);
                final List<Border> tmpBorders = new ArrayList();
                for (Border border : mesh2.getBorders()) {
                    tmpBorders.addAll(border.separateSubBorders());
                }
                mesh2.addBorders(tmpBorders);
                CustomFrame.appendToLog(mesh2.getBorders().size() + " borders detected for "
                    + mesh2.getFile().getName());
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

        if (mesh1.getBorders().isEmpty()) {
            IJ.showMessage("Error 3 : " + mesh1.getFile().getName()
                    + " don't cross to the border");
        } else {

            if (mesh2.getBorders().isEmpty()) {
                IJ.showMessage("Error 3 : " + mesh2.getFile().getName()
                        + " don't cross to the border");
            } else {
                final Set<Border[]> couples = pairBorders(mesh1.getBorders(),
                        mesh2.getBorders());
                final List<Face> newFaces = new ArrayList();
                CustomFrame.appendToLog("Computing the new faces");
                for (Border[] couple : couples) {
                    if (couple[0].distanceTo(couple[1]) < 100) {
//                    double dis = couple[0].distanceTo(couple[1]);
                        couple[0].alignOn(couple[1]);
                        newFaces.addAll(Linker.createFacesBetween(couple[0].getVertexSequence(), couple[1].getVertexSequence(), couple[0].isCircular()));
                    }
                }
                try {
                    ObjWriter.exportFusion(mesh1, mesh2, newFaces);
                } catch (IOException ex) {
                    IJ.handleException(ex);
                }
                CustomFrame.appendToLog("Done");
                CustomFrame.appendToLog("-----------------------");
                mesh1.clear();
                mesh2.clear();
            }
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
                if ((border1.isCircular() && border2.isCircular())
                        || (!border1.isCircular() && !border2.isCircular())) {
                    if (distances.isEmpty()) {
                        distances.add(border2);
                    } else {
                        int i = 0;
                        final double distance = border2.distanceTo(border1);
                        while (i < distances.size() && distance
                                > distances.get(i).distanceTo(border1)) {
                            i++;
                        }
                        distances.add(i, border2);
                    }
                }
            }
            result.add(distances);
        }
        return result;
    }
}
