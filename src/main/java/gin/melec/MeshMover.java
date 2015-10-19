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
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class MeshMover {

    private MeshMover() {
    }

    public static MeshMover getInstance() {
        return MeshMoverHolder.INSTANCE;
    }

    private static class MeshMoverHolder {

        private static final MeshMover INSTANCE = new MeshMover();
    }

    /**
     * This method is called when the user want to move the meshes of his
     * repertory.
     * @param allMeshes , the meshes of the repertory.
     */
    public static void moveMeshes(final List<List> allMeshes) {
        int nbMoved = 0;
        for (List<Mesh> meshes : allMeshes) {
            for (Mesh mesh : meshes) {
                    if (!mesh.isMoved()) {
                        mesh.importMesh();
                        if (!mesh.isMoved()) {
                            mesh.shift();
                            mesh.exportMesh();
                            nbMoved++;
                        }
                    }
            }
        }
        IJ.showMessage(nbMoved + " meshes have been moved.");
    }
}
