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
     * Code the upper part of the left split.
     */
    protected char UP_LEFT_SPLIT = '0';
    /**
     * Code the left part of the upper split.
     */
    protected char LEFT_UPPER_SPLIT = '1';
    /**
     * Code the middle part of the upper split.
     */
    protected char MIDDLE_UPPER_SPLIT = '2';
    /**
     * Code the middle part of the left split.
     */
    protected char MIDDLE_LEFT_SPLIT = '3';
    /**
     * Code the upper part of the right split.
     */
    protected char UPPER_RIGHT_SPLIT = '4';
    /**
     * Code the right part of the upper split.
     */
    protected char RIGHT_UPPER_SPLIT = '5';
    /**
     * Code the middle part of the right split.
     */
    protected char MIDDLE_RIGHT_SPLIT = '6';
    /**
     * Code the left part of the lower split.
     */
    protected char LEFT_LOWER_SPLIT = '7';
    /**
     * Code the middle part of the lower split.
     */
    protected char MIDDLE_LOWER_SPLIT = '8';
    /**
     * Code the right part of the lower split.
     */
    protected char RIGHT_LOWER_SPLIT = '9';
    /**
     * Code the lower part of the left split.
     */
    protected char LOWER_LEFT_SPLIT = 'A';
    /**
     * Code the lower part of the right split.
     */
    protected char LOWER_RIGHT_SPLIT = 'B';

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





}
