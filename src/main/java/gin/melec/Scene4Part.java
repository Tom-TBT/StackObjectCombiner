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
public class Scene4Part extends Scene2Part{

    /**
     * The meshes in the left part of the stack.
     */
    protected List leftMeshes;
    /**
     * The meshes in the middle part of the stack.
     */
    protected List middleMeshes;
    /**
     * The coordonate (y) of the upper split.
     */
    protected int upperSplit;

    /**
     * Constructor for a scene divide in 4 parts.
     * @param pathname ,the path of the folder containing the meshes.
     * @param leftSplit , the coordonate(x) of the left vertical split.
     * @param upperSplit , the coordonate(y) of the upper horizontal split.
     */
    public Scene4Part(final String pathname, final int leftSplit,
            final int upperSplit) {
        super(pathname, leftSplit);

        this.upperSplit = upperSplit;

        this.leftMeshes = new ArrayList();
        this.middleMeshes = new ArrayList();
    }

    @Override
    public void sortFiles() {
        super.sortFiles();
        for (String fileList : this.workingDirectory.list()) {
        /* The position of the object is coded by a capital letter and
            an underscore.*/
            if (fileList.charAt(1) == '_') {
                switch (fileList.charAt(0)) {
                    case 'C':
                        this.leftMeshes.add(fileList);
                        break;
                    case 'D':
                        this.middleMeshes.add(fileList);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    void shiftMeshes() {
        super.shiftMeshes();
        for (Object leftMeshe : this.leftMeshes) {
            try {
                Shifter.yTranslation(this.workingDirectory + "/"
                        + (String) leftMeshe,
                        upperSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
        for (Object middleMeshe : this.middleMeshes) {
            try {
                Shifter.xYTranslation(this.workingDirectory + "/"
                        + (String) middleMeshe, leftSplit, upperSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
    }

    @Override
    void createLimit() {
        super.createLimit();
        final String dirPath = this.workingDirectory + "/";
        for (Object upperLeftMeshe : upperLeftMeshes) {
            try {
                Limiter.findHorizontalBorder(dirPath + (String) upperLeftMeshe,
                        upperSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
        for (Object upperMiddleMeshe : upperMiddleMeshes) {
            try {
                Limiter.findHorizontalBorder(dirPath + (String) upperMiddleMeshe,
                        upperSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
        for (Object leftMeshe : leftMeshes) {
            try {
                Limiter.findHorizontalBorder(dirPath + (String) leftMeshe,
                        upperSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
        for (Object middleMeshe : middleMeshes) {
            try {
                Limiter.findHorizontalBorder(dirPath + (String) middleMeshe,
                        upperSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
        for (Object leftMeshe : leftMeshes) {
            try {
                Limiter.findVerticalBorder(dirPath + (String) leftMeshe,
                        leftSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
        for (Object middleMeshe : middleMeshes) {
            try {
                Limiter.findVerticalBorder(dirPath + (String) middleMeshe,
                        leftSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
    }

}
