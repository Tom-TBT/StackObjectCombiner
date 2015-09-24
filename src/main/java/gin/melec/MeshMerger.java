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
import java.util.List;

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

    static void work(final List<List> allMeshes) {
        Mesh mesh1, mesh2;
        String choices[];
        choices = getChoices(allMeshes);
        if (choices.length < 2) {
            IJ.showMessage("Their is not enough meshes to merge.\n"
                    + "See the documentation for more informations.");
        } else {
            GenericDialog gDial = new GenericDialog("Choose_meshes");
            gDial.addChoice("Part 1", choices, choices[0]);
            gDial.addChoice("Part 2", choices, choices[1]);
            gDial.showDialog();
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
                IJ.error("Two meshes from a same part can't be merged");
                return;
            }
            IJ.showMessage("On construction !");
        }
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
