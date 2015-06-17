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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Scene9Part extends Scene {

    /**
     * The meshes in the upper left corner of the stack.
     */
    private List upperLeftMeshes;
    /**
     * The meshes in the upper middle part of the stack.
     */
    private List upperMiddleMeshes;
    /**
     * The meshes in the upper right corner of the stack.
     */
    private List upperRightMeshes;
    /**
     * The meshes in the left part of the stack.
     */
    private List leftMeshes;
    /**
     * The meshes in the middle part of the stack.
     */
    private List middleMeshes;
    /**
     * The meshes in the right part of the stack.
     */
    private List rightMeshes;
    /**
     * The meshes in the lower left corner of the stack.
     */
    private List lowerLeftMeshes;
    /**
     * The meshes in the lower middle part of the stack.
     */
    private List lowerMiddleMeshes;
    /**
     * The meshes in the upper right corner of the stack.
     */
    private List lowerRightMeshes;
    /**
     * The coordonate (x) of the left split.
     */
    private int leftSplit;
    /**
     * The coordonate (x) of the right split.
     */
    private int rightSplit;
    /**
     * The coordonate (y) of the upper split.
     */
    private int upperSplit;
    /**
     * The coordonate (y) of the right split.
     */
    private int lowerSplit;

    /**
     * Constructor for a scene divide in 9 parts.
     * @param pathname ,the path of the folder containing the meshes.
     * @param leftSplit , the coordonate(x) of the left vertical split.
     * @param rightSplit , the coordonate(x) of the right vertical split.
     * @param upperSplit , the coordonate(y) of the upper horizontal split.
     * @param lowerSplit , the coordonate(y) of the upper horizontal split.
     * @param width , the width of the scene.
     * @param height , the height of the scene.
     * @param depth , the depth of the scene.
     */
    public Scene9Part(String pathname, int leftSplit, int rightSplit,
            int upperSplit, int lowerSplit, int width, int height, int depth){
        super(pathname, width, height, depth);

        this.leftSplit = leftSplit;
        this.rightSplit = rightSplit;
        this.upperSplit = upperSplit;
        this.lowerSplit = lowerSplit;

        this.upperLeftMeshes = new ArrayList();
        this.upperMiddleMeshes = new ArrayList();
        this.upperRightMeshes = new ArrayList();
        this.leftMeshes = new ArrayList();
        this.middleMeshes = new ArrayList();
        this.rightMeshes = new ArrayList();
        this.lowerLeftMeshes = new ArrayList();
        this.lowerMiddleMeshes = new ArrayList();
        this.lowerRightMeshes = new ArrayList();

        this.sortFiles(this.workingDirectory.list());

    }

    /**
     * Class the file in the list where they belong to.
     * @param fileList , the list of the file to class.
     */
    private void sortFiles(final String[] fileList) {
        for (String fileList1 : fileList) {
        /* The position of the object is coded by a capital letter and
            an underscore.*/
            if (fileList1.charAt(1) == '_') {
                switch (fileList1.charAt(0)) {
                    case 'A':
                        this.upperLeftMeshes.add(fileList1);
                        break;
                    case 'B':
                        this.upperMiddleMeshes.add(fileList1);
                        break;
                    case 'C':
                        this.upperRightMeshes.add(fileList1);
                        break;
                    case 'D':
                        this.leftMeshes.add(fileList1);
                        break;
                    case 'E':
                        this.middleMeshes.add(fileList1);
                        break;
                    case 'F':
                        this.rightMeshes.add(fileList1);
                        break;
                    case 'G':
                        this.lowerLeftMeshes.add(fileList1);
                        break;
                    case 'H':
                        this.lowerMiddleMeshes.add(fileList1);
                        break;
                    case 'I':
                        this.lowerRightMeshes.add(fileList1);
                        break;
                    default: // The mesh is not supported
                        break;
                }
            }
        }
    }

    @Override
    public void shiftMeshes() {
        // The upperLeftMeshes are not shifted because they are already in place
        for (Object upperMiddleMeshe : this.upperMiddleMeshes) {
            try {
                Shifter.xTranslation(this.workingDirectory + "/" + (String)
                        upperMiddleMeshe, leftSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
        for (Object upperRightMeshe : this.upperRightMeshes) {
            try {
                Shifter.xTranslation(this.workingDirectory + "/" + (String)
                        upperRightMeshe, rightSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
        for (Object leftMeshe : this.leftMeshes) {
            try {
                Shifter.yTranslation(this.workingDirectory + "/" + (String)
                        leftMeshe, upperSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
        for (Object middleMeshe : this.middleMeshes) {
            try {
                Shifter.xYTranslation(this.workingDirectory + "/" + (String)
                        middleMeshe, leftSplit, upperSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
        for (Object rightMeshe : this.rightMeshes) {
            try {
                Shifter.xYTranslation(this.workingDirectory + "/" + (String)
                        rightMeshe, rightSplit, upperSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
        for (Object lowerLeftMeshe : this.lowerLeftMeshes) {
            try {
                Shifter.yTranslation(this.workingDirectory + "/" + (String)
                        lowerLeftMeshe, lowerSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
        for (Object lowerMiddleMeshe : this.lowerMiddleMeshes) {
            try {
                Shifter.xYTranslation(this.workingDirectory + "/" + (String)
                        lowerMiddleMeshe, leftSplit, lowerSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
        for (Object lowerRightMeshe : this.lowerRightMeshes) {
            try {
                Shifter.xYTranslation(this.workingDirectory + "/" + (String)
                        lowerRightMeshe, rightSplit, lowerSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
    }

}
