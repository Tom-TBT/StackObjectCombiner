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
import java.util.ArrayList;
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
     * @param allSplits , the splits.
     * @throws java.text.ParseException
     * @throws java.io.IOException
     */
    static void moveMeshes()
            throws ParseException, IOException {
        int nbMoved = 0;
        for (Mesh mesh : DialogContentManager.A_MESHES) {
            if (!mesh.isMoved()) {
                mesh.importMesh(false);
                mesh.shift(new ArrayList());
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " has been shifted");
                nbMoved++;
            } else {
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " is already shifted");
            }
            mesh.unload();
        }
        for (Mesh mesh : DialogContentManager.B_MESHES) {
            if (!mesh.isMoved()) {
                mesh.importMesh(false);
                List splits = new ArrayList();
                splits.add(DialogContentManager.WIDTH_SPLIT);
                mesh.shift(splits);
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " has been shifted");
                nbMoved++;
            } else {
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " is already shifted");
            }
            mesh.unload();
        }
        for (Mesh mesh : DialogContentManager.C_MESHES) {
            if (!mesh.isMoved()) {
                mesh.importMesh(false);
                List splits = new ArrayList();
                splits.add(DialogContentManager.HEIGHT_SPLIT);
                mesh.shift(splits);
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " has been shifted");
                nbMoved++;
            } else {
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " is already shifted");
            }
            mesh.unload();
        }
        for (Mesh mesh : DialogContentManager.D_MESHES) {
            if (!mesh.isMoved()) {
                mesh.importMesh(false);
                List splits = new ArrayList();
                splits.add(DialogContentManager.HEIGHT_SPLIT);
                splits.add(DialogContentManager.WIDTH_SPLIT);
                mesh.shift(splits);
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " has been moved");
                nbMoved++;
            } else {
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " is already shifted");
            }
            mesh.unload();
        }
        for (Mesh mesh : DialogContentManager.E_MESHES) {
            if (!mesh.isMoved()) {
                mesh.importMesh(false);
                List splits = new ArrayList();
                splits.add(DialogContentManager.DEPTH_SPLIT);
                mesh.shift(splits);
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " has been shifted");
                nbMoved++;
            } else {
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " is already shifted");
            }
            mesh.unload();
        }
        for (Mesh mesh : DialogContentManager.F_MESHES) {
            if (!mesh.isMoved()) {
                mesh.importMesh(false);
                List splits = new ArrayList();
                splits.add(DialogContentManager.DEPTH_SPLIT);
                splits.add(DialogContentManager.WIDTH_SPLIT);
                mesh.shift(splits);
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " has been moved");
                nbMoved++;
            } else {
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " is already shifted");
            }
            mesh.unload();
        }
        for (Mesh mesh : DialogContentManager.G_MESHES) {
            if (!mesh.isMoved()) {
                mesh.importMesh(false);
                List splits = new ArrayList();
                splits.add(DialogContentManager.DEPTH_SPLIT);
                splits.add(DialogContentManager.HEIGHT_SPLIT);
                mesh.shift(splits);
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " has been moved");
                nbMoved++;
            } else {
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " is already shifted");
            }
            mesh.unload();
        }
        for (Mesh mesh : DialogContentManager.H_MESHES) {
            if (!mesh.isMoved()) {
                mesh.importMesh(false);
                List splits = new ArrayList();
                splits.add(DialogContentManager.WIDTH_SPLIT);
                splits.add(DialogContentManager.HEIGHT_SPLIT);
                splits.add(DialogContentManager.DEPTH_SPLIT);
                mesh.shift(splits);
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " has been moved");
                nbMoved++;
            } else {
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " is already shifted");
            }
            mesh.unload();
        }
        CustomFrame.appendToLog(nbMoved + " meshes have been shifted.");
    }
    static void unshiftMeshes() throws ParseException, IOException {
        int nbMoved = 0;
        final List<Mesh> ALL_MESHES = new ArrayList();
        ALL_MESHES.addAll(DialogContentManager.A_MESHES);
        ALL_MESHES.addAll(DialogContentManager.B_MESHES);
        ALL_MESHES.addAll(DialogContentManager.C_MESHES);
        ALL_MESHES.addAll(DialogContentManager.D_MESHES);
        ALL_MESHES.addAll(DialogContentManager.E_MESHES);
        ALL_MESHES.addAll(DialogContentManager.F_MESHES);
        ALL_MESHES.addAll(DialogContentManager.G_MESHES);
        ALL_MESHES.addAll(DialogContentManager.H_MESHES);
        for (Mesh mesh : ALL_MESHES) {
            if (mesh.isMoved() && !mesh.isMerged()) {
                mesh.importMesh(false);
                mesh.unshift();
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " has been unshifted");
                mesh.exportMesh(0,0,0);
                nbMoved++;
            } else {
                CustomFrame.appendToLog(mesh.getFile().getName()
                        + " is not shifted");
            }
            mesh.unload();
        }
        CustomFrame.appendToLog(nbMoved + " meshes have been unshifted.");
    }
}
