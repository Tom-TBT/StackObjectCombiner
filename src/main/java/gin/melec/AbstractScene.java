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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public abstract class AbstractScene {

    /**
     * The scene concern only one directory.
     */
    protected File workingDirectory;


    /**
     * Build a scene by opening the given directory and list all the file in it.
     * @param pathname , the pathname of the file.
     */
    public AbstractScene(final String pathname) {
        this.workingDirectory = new File(pathname);
    }

    /**
     * Getter for the name of the current working directory.
     * @return the name of the directory.
     */
    public final File getWorkingDirectory() {
        return workingDirectory;
    }

    /**
     * Method called to shift the meshes of the scene.
     */
    abstract void shiftMeshes();

    /**
     * Create for each object separated a list of the limit vertex.
     */
    abstract void createLimit();

    /**
     * List and put in the appropriate Array the names of the files.
     */
    public abstract void sortFiles();

    /**
     * Load the mesh of the given name with the given splits.
     * @param meshName , the name of the mesh.
     * @param splits , the splits of the mesh.
     * @return the wanted mesh.
     */
    final Mesh loadMesh(final String meshName, final List splits) {
        final Mesh mesh = new Mesh(splits);
        try {
            ObjReader.readMesh(this.workingDirectory + "/" + meshName,
                    mesh.vertices, mesh.faces);
        } catch (IOException ex) {
            Logger.getLogger(Scene2Part.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

        return mesh;
    }

    /**
     * Save the mesh given at the name given.
     * @param meshName , the name of the mesh.
     * @param mesh , the mesh to save.
     */
    final void saveMesh(final String meshName, final Mesh mesh) {
        try {
                ObjWriter.writeMesh(this.workingDirectory + "/"
                        + meshName, mesh);
            } catch (IOException ex) {
                Logger.getLogger(Scene2Part.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
    }



}
