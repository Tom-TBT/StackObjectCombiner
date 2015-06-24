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
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Border {

    /**
     * The first vertex of the border's sequence.
     */
    Vertex firstVertex;

    /**
     * The last vertex added to the border.
     */
    Vertex lastVertexAdded;

    /**
     * The last vertex added to the border.
     */
    Vertex scndLastVertexAdded;

    /**
     * The sequence of vertex forming the border.
     */
    List vertexSequence;

    /**
     * The lenght of the border. Equals to the sum of all distances between
     * vertices.
     */
    float lengthSum;

    /**
     * The lenght of the border between the first and the last vertex.
     */
    float lenght;

    /**
     * Coordonates of the centre.
     */
    float centre; // TODO change center to x/y/z coordonnates system.

    /**
     * This flag is up when the border form a circle.
     */
    boolean isCircular;

    /**
     * Public constructor for the border.
     * @param firstVertex , the first vertex of the border.
     */
    public Border(final Vertex firstVertex) {
        this.firstVertex = firstVertex;
        this.lastVertexAdded = null;
        this.vertexSequence = new LinkedList();
        this.addNextVertex(firstVertex);
    }

    /**
     * Add the given vertex to the border.
     * @param vertex , the vertex to add.
     */
    public final void addNextVertex(final Vertex vertex) {
        if (vertex != null) {
            this.vertexSequence.add(vertex);
            scndLastVertexAdded = lastVertexAdded;
            lastVertexAdded = vertex;
        }
        // TODO add change for the circular flag
    }

    /**
     * Add the given vertex to the border, in first position.
     * @param vertex , the vertex to add.
     */
    public final void addPreviousVertex(final Vertex vertex) {
        if (vertex != null) {
            this.vertexSequence.add(0, vertex);
            scndLastVertexAdded = lastVertexAdded;
            lastVertexAdded = vertex;
        }
        // TODO add change for the circular flag
    }
}
