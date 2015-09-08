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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
     * The split that initiate this border.
     */
    AbstractSplit split;

    /**
     * The sequence of vertex forming the border.
     */
    List vertexSequence;

    /**
     * Public constructor for the border.
     */
    public Border(final Mesh mesh) {
        this.vertexSequence = new LinkedList();

        // Search the next first vertex
        this.firstVertex = null;
        Vertex candidat = null;
        float distanceTmp, distanceFirstVertex = 0;
        for (Object obj : mesh.splits) {
            final AbstractSplit currentSplit = (AbstractSplit) obj;
            candidat = currentSplit.findCloserVertex(mesh.primers);
            distanceTmp = currentSplit.distanceTo(candidat);
            if (firstVertex == null || distanceTmp < distanceFirstVertex) {
                distanceFirstVertex = distanceTmp;
                firstVertex = candidat;
                this.split = currentSplit;
            }
        }
        mesh.findNeighboors(firstVertex);
        this.scndLastVertexAdded = firstVertex;
        lastVertexAdded = this.split.findCloserVertex(firstVertex.neighbours);
        this.vertexSequence.add(scndLastVertexAdded);
        this.vertexSequence.add(lastVertexAdded);

        mesh.garbage.add(firstVertex);
        mesh.garbage.add(lastVertexAdded);
    }

    public Border() {
        this.vertexSequence = new LinkedList();
    }

    /**
     * Add the given vertex to the end of the border.
     * @param vertex , the vertex to add.
     */
    public final void addNextVertex(final Vertex vertex) {
        if (vertex != null) {
            if (this.vertexSequence.isEmpty()) {
                this.firstVertex = vertex;
            }
            this.vertexSequence.add(vertex);
            scndLastVertexAdded = lastVertexAdded;
            lastVertexAdded = vertex;
        }
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
    }

}
