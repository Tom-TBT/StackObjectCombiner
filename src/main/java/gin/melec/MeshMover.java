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

import java.io.IOException;
import java.text.ParseException;

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
     * @param allSplits , the splits.
     * @throws java.text.ParseException
     * @throws java.io.IOException
     */
    public static void moveMeshes()
            throws ParseException, IOException {
        int nbMoved = 0;
        //final List<List> allMeshes = DialogContentManager.ALL_MESHES;
        //final List<AbstractSplit> allSplits = DialogContentManager.ALL_SPLITS;
        for (Object obj : DialogContentManager.A_MESHES) {
            Mesh mesh = (Mesh) obj;
            if (!mesh.isMoved()) {
                mesh.importMesh();
                mesh.shift(null);
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " has been shifted");
                mesh.exportMesh();
                nbMoved++;
            } else {
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " is already shifted");
            }
            mesh.clear();
        }
        for (Object obj : DialogContentManager.B_MESHES) {
            Mesh mesh = (Mesh) obj;
            if (!mesh.isMoved()) {
                mesh.importMesh();
                mesh.shift(DialogContentManager.LEFT_SPLIT);
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " has been moved");
                mesh.exportMesh();
                nbMoved++;
            } else {
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " is already shifted");
            }
            mesh.clear();
        }
        for (Object obj : DialogContentManager.C_MESHES) {
            Mesh mesh = (Mesh) obj;
            if (!mesh.isMoved()) {
                mesh.importMesh();
                mesh.shift(DialogContentManager.UP_SPLIT);
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " has been moved");
                mesh.exportMesh();
                nbMoved++;
            } else {
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " is already shifted");
            }
            mesh.clear();
        }
        for (Object obj : DialogContentManager.D_MESHES) {
            Mesh mesh = (Mesh) obj;
            if (!mesh.isMoved()) {
                mesh.importMesh();
                mesh.shift(DialogContentManager.LEFT_SPLIT);
                mesh.shift(DialogContentManager.UP_SPLIT);
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " has been moved");
                mesh.exportMesh();
                nbMoved++;
            } else {
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " is already shifted");
            }
            mesh.clear();
        }
        CustomFrame.appendToLog(nbMoved + " meshes have been moved.");
        CustomFrame.appendToLog("-----------------------");
    }
}
