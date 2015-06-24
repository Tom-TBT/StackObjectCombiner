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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Limiter {

    /**
     * The value to compare for a vertical limit will be in x (1 in the table).
     */
    private static final int VERTICAL_LIMIT = 1;
    /**
     * The value to compare for a vertical limit will be in y (2 in the table).
     */
    private static final int HORIZONTAL_LIMIT = 2;

    /**
     * Code the upper part of the left split.
     */
    private char UPPER_LEFT_SPLIT = '0';
    /**
     * Code the left part of the upper split.
     */
    private char LEFT_UPPER_SPLIT = '1';
    /**
     * Code the middle part of the upper split.
     */
    private char MIDDLE_UPPER_SPLIT = '2';
    /**
     * Code the middle part of the left split.
     */
    private char MIDDLE_LEFT_SPLIT = '3';
    /**
     * Code the upper part of the right split.
     */
    private char UPPER_RIGHT_SPLIT = '4';
    /**
     * Code the right part of the upper split.
     */
    private char RIGHT_UPPER_SPLIT = '5';
    /**
     * Code the middle part of the right split.
     */
    private char MIDDLE_RIGHT_SPLIT = '6';
    /**
     * Code the left part of the lower split.
     */
    private char LEFT_LOWER_SPLIT = '7';
    /**
     * Code the middle part of the lower split.
     */
    private char MIDDLE_LOWER_SPLIT = '8';
    /**
     * Code the right part of the lower split.
     */
    private char RIGHT_LOWER_SPLIT = '9';
    /**
     * Code the lower part of the left split.
     */
    private char LOWER_LEFT_SPLIT = 'A';
    /**
     * Code the lower part of the right split.
     */
    private char LOWER_RIGHT_SPLIT = 'B';

    /**
     * Private constructor to let this class utilitary.
     */
    private Limiter() {
    }

    public static Limiter getInstance() {
        return LimiterHolder.INSTANCE;
    }

    private static class LimiterHolder {

        private static final Limiter INSTANCE = new Limiter();
    }

    /**
     * For a given object, find his vertex close to the split postition, in X.
     * @param meshPath , the path of the given object.
     * @param splitPosition , the position of the split
     * @throws IOException
     */
    public static void findVerticalBorder(final String meshPath,
            final int splitPosition) throws IOException {
        final Mesh mesh = ObjReader.readBorderMesh(meshPath, splitPosition,
                VERTICAL_LIMIT);
        makeVerticalBorder(mesh, splitPosition);
    }

    /**
     * For a given object, find his vertex close to the split postition, in Y.
     * @param meshPath , the path of the given object.
     * @param splitPosition , the position of the split
     * @throws IOException
     */
    public static void findHorizontalBorder(final String meshPath,
            final int splitPosition) throws IOException {
        final Mesh mesh = ObjReader.readBorderMesh(meshPath, splitPosition,
                HORIZONTAL_LIMIT);
        ObjWriter.replaceMesh(meshPath+"Bordered", mesh);
    }


    public static void makeVerticalBorder(final Mesh mesh ,
            final int splitPosition) {
        // TODO traitement garbage, multi border,...  while (!mesh.vertices.isEmpty()) {
            Vertex currentVertex = mesh.newBorderX(splitPosition);
            while (currentVertex != null) {
                currentVertex = currentVertex.findNextVertexX(splitPosition, mesh);
                mesh.currentBorder.addNextVertex(currentVertex);
            }
            currentVertex = mesh.currentBorder.firstVertex;
            while (currentVertex != null) {
                currentVertex = currentVertex.findNextVertexX(splitPosition, mesh);
                mesh.currentBorder.addPreviousVertex(currentVertex);
            }
            //Traitement garbage ...
        //}
    }
}
