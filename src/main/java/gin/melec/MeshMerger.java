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
import ij.gui.GenericDialog;
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

    public static void work(final List<List> allMeshes) {
        Mesh mesh1, mesh2;
        String choices[];
        choices = getChoices(allMeshes);
        if (choices.length < 2) {
            IJ.showMessage("Their is not enough meshes to merge.\n"
                    + choices.length + " meshes found.\n"
                    + "See the documentation for more informations.");
        } else {
            boolean notFinished = true;
            while (notFinished) {
                GenericDialog gDial = new GenericDialog("Choose_meshes");
                gDial.addChoice("Part 1", choices, choices[0]);
                gDial.addChoice("Part 2", choices, choices[1]);
                gDial.showDialog();
                if (gDial.wasCanceled()) {
                    return;
                }
                mesh1 = getMesh(gDial.getNextChoice(), allMeshes);
                mesh2 = getMesh(gDial.getNextChoice(), allMeshes);
                if ((allMeshes.get(0).contains(mesh1) && allMeshes.get(0).
                        contains(mesh2))
                        || (allMeshes.get(1).contains(mesh1) && allMeshes.get(1).
                        contains(mesh2))
                        || (allMeshes.get(2).contains(mesh1) && allMeshes.get(2).
                        contains(mesh2))
                        || (allMeshes.get(3).contains(mesh1) && allMeshes.get(3).
                        contains(mesh2))) {
                    IJ.error("Two meshes from a same part can't be merged\n"
                            + "See the documentation for more informations.");
                } else {
                    mesh1.importMesh();
                    IJ.log(mesh1.getFile().getName() + " imported");
                    mesh1.createBorders();
                    IJ.log(mesh1.getFile().getName() + " border created");
                    if (mesh1.getBorders().isEmpty()) {
                        IJ.showMessage(mesh1.getFile().getName()
                                + " don't cross to the border");
                    } else {
                        mesh2.importMesh();
                        IJ.log(mesh2.getFile().getName() + " imported");
                        mesh2.createBorders();
                        IJ.log(mesh2.getFile().getName() + " border created");
                        if (mesh2.getBorders().isEmpty()) {
                            IJ.showMessage(mesh2.getFile().getName()
                                    + " don't cross to the border");
                        } else {
                            final Set<Border[]> couples = pairBorders(mesh1.getBorders(),
                                    mesh2.getBorders());
                            final List<Face> newFaces = new ArrayList();
                            for (Border[] couple : couples) {
                                newFaces.addAll(Linker.createFacesBetween(couple[0], couple[1]));
                            }
                            exportFusion(mesh1,mesh2,newFaces);
                        }
                    }
                }
            }
        }
    }

    private static void exportFusion(Mesh mesh1, Mesh mesh2, List<Face> newFaces) {
        for (Vertex vertex : mesh1.getVertices()) {
            System.out.println(vertex);
        }
        for (Vertex vertex : mesh2.getVertices()) {
            vertex.incrementId(mesh1.getVertices().size());
            System.out.println(vertex);
        }
        for (Face face : mesh1.getFaces()) {
            System.out.println(face);
        }
        for (Face face : mesh2.getFaces()) {
            System.out.println(face);
        }
        for (Face face : newFaces) {
            System.out.println(face);
        }

    }

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

    private static String[] getChoices(List<List> allMeshes) {
        final List<String> choices = new ArrayList();
        for (List<Mesh> listMesh : allMeshes) {
            for (Mesh mesh : listMesh) {
                if (mesh.isMoved()) {
                    choices.add(mesh.toString());
                }
            }
        }
        return choices.toArray(new String[0]);
    }

    private static Mesh getMesh(String meshName, List<List> allMeshes) {
        Mesh result = null;
        for (List<Mesh> listMesh : allMeshes) {
            for (Mesh mesh : listMesh) {
                if (mesh.toString().equals(meshName)) {
                    result = mesh;
                    break;
                }
            }
        }
        return result;
    }
}
