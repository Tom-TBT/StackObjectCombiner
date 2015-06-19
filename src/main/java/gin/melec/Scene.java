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

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public abstract class Scene {

    /**
     * The scene concern only one directory.
     */
    protected File workingDirectory;


    /**
     * Build a scene by opening the given directory and list all the file in it.
     * @param pathname , the pathname of the file.
     */
    public Scene(final String pathname) {
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





}
