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
public class Scene2Part extends Scene{

    /**
     * The meshes in the upper left corner of the stack.
     */
    private List upperLeftMeshes;
    /**
     * The meshes in the upper middle part of the stack.
     */
    private List upperMiddleMeshes;
    /**
     * The coordonate (x) of the left split.
     */
    private int leftSplit;

    /**
     * Constructor for a scene divide in 9 parts.
     * @param pathname ,the path of the folder containing the meshes.
     * @param leftSplit , the coordonate(x) of the left vertical split.
     */
    public Scene2Part(final String pathname, final int leftSplit) {
        super(pathname);

        this.leftSplit = leftSplit;

        this.upperLeftMeshes = new ArrayList();
        this.upperMiddleMeshes = new ArrayList();

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
                    default: // The mesh is not supported
                        break;
                }
            }
        }
    }

    @Override
    void shiftMeshes() {
        // The upperLeftMeshes are not shifted because they are already in place
        for (Object upperMiddleMeshe : this.upperMiddleMeshes) {
            try {
                Shifter.xTranslation(this.workingDirectory + "/"
                        + (String) upperMiddleMeshe,
                        leftSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
    }

    @Override
    void createLimit() {
        final String dirPath = this.workingDirectory + "/";
        for (int i = 0; i < upperLeftMeshes.size(); i++) {
            final Object upperLeftMeshe = upperLeftMeshes.get(i);
            try {
                Limiter.findVerticalBorder(dirPath + i + "A" + "0" + (String) upperLeftMeshe,
                        leftSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
        for (Object upperMiddleMeshe : this.upperMiddleMeshes) {
            try {
                Limiter.findVerticalBorder(dirPath + (String) upperMiddleMeshe,
                        leftSplit);
            } catch (IOException ex) {
                IJ.handleException(ex);
            }
        }
    }
}
