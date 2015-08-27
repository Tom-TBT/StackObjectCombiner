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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Scene2Part extends AbstractScene {

    /**
     * The meshes in the upper left corner of the stack.
     */
    protected List upperLeftMeshes;
    /**
     * The meshes in the upper middle part of the stack.
     */
    protected List upperMiddleMeshes;
    /**
     * The left side of the left split.
     */
    protected Split leftSplitLeftSide;
    /**
     * The right side of the left split.
     */
    protected Split leftSplitRightSide;

    /**
     * Constructor for a scene divide in 9 parts.
     * @param pathname ,the path of the folder containing the meshes.
     * @param leftSplit , the coordonate(x) of the left vertical split.
     */
    public Scene2Part(final String pathname, final int leftSplit) {
        super(pathname);

        this.leftSplitLeftSide = new SplitLeft(leftSplit);
        this.leftSplitRightSide = new SplitRight(leftSplit);

        this.upperLeftMeshes = new ArrayList();
        this.upperMiddleMeshes = new ArrayList();

        this.sortFiles();
    }

    /**
     * Class the file in the list where they belong to.
     */
    @Override
    public final void sortFiles() {
        for (String fileList : this.workingDirectory.list()) {
        /* The position of the object is coded by a capital letter and
            an underscore.*/
            if (fileList.charAt(1) == '_') {
                switch (fileList.charAt(0)) {
                    case 'A':
                        this.upperLeftMeshes.add(fileList);
                        break;
                    case 'B':
                        this.upperMiddleMeshes.add(fileList);
                        break;
                    default: // The mesh is not supported
                        break;
                }
            }
        }
    }

    @Override
    final void shiftMeshes() {
        // The upperLeftMeshes are not shifted because they are already in place
        Mesh mesh;
        for (Object upperMiddleMeshe : this.upperMiddleMeshes) {
            final List splits = new ArrayList();
            splits.add(leftSplitLeftSide);
            mesh = loadMesh((String) upperMiddleMeshe, splits);
            mesh.shift();
            saveMesh((String) upperMiddleMeshe, mesh);
        }
    }

    @Override
    void createLimit() {
        Mesh mesh;

        List splits = new ArrayList();
        splits.add(leftSplitRightSide);
        for (Object upperLeftMeshe : upperLeftMeshes) {
            mesh = loadMesh((String) upperLeftMeshe, splits);
            mesh.createBorders();
        }

        splits = new ArrayList();
        splits.add(leftSplitLeftSide);
        for (Object upperMiddleMeshe : this.upperMiddleMeshes) {
            mesh = loadMesh((String) upperMiddleMeshe, splits);
            mesh.createBorders();
        }

    }
}
