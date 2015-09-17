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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Border {

    /**
     * The split that initiate this border.
     */
    private AbstractSplit split;

    /**
     * The sequence of vertex forming the border.
     */
    protected LinkedList<Vertex> vertexSequence;

    /**
     * The sub borders of a border.
     */
    private List<SubBorder> subBorders;

    /**
     * Public constructor for the border. This constructor is used when the
     * border need to be detected. The constructor look at the primers of the
     * given mesh to build the new border.
     *
     * @param mesh , the mesh from which a new border is created.
     */
    public Border(final Mesh mesh) {
        this.vertexSequence = new LinkedList();
        this.subBorders = new ArrayList();

        Vertex currentVertex = null;
        Vertex firstVertex = null, secondVertex = null;
        for (AbstractSplit currentSplit : mesh.getSplits()) {
            currentVertex = currentSplit.findCloserVertex(mesh.getPrimers());
            if (currentVertex.belongToBorder()) {
                firstVertex = currentVertex;
                this.split = currentSplit;
                break;
            } else {
                mesh.getPrimers().remove(currentVertex);
            }
        }
        vertexSequence.add(firstVertex);
        mesh.setNeighbourhoodToVertex(firstVertex);
        for (final Vertex vertex : firstVertex.getNeighbours()) {
            if (vertex.belongToBorder()) {
                secondVertex = vertex;
                break;
            }
        }
        vertexSequence.add(secondVertex);
        mesh.setNeighbourhoodToVertex(secondVertex);
        mesh.getGarbage().add(getSecondLastVertex());
        mesh.getGarbage().add(getLastVertex());
    }

    /**
     * A simple constructor which is used when a border is only loaded.
     */
    public Border() {
        this.vertexSequence = new LinkedList();
        this.subBorders = new ArrayList();
    }

    /**
     * Getter for the split of the border.
     *
     * @return the split of the border.
     */
    public final AbstractSplit getSplit() {
        return split;
    }

    /**
     * Getter for the vertex sequence of the border.
     *
     * @return the vertex sequence of the border
     */
    public final List<Vertex> getVertexSequence() {
        return vertexSequence;
    }

    /**
     * Add the given vertex to the end of the border.
     *
     * @param vertex , the vertex to add.
     */
    public final void addNextVertex(final Vertex vertex) {
        if (vertex != null) {
            this.vertexSequence.add(vertex);
        }
    }

    /**
     * Getter for the first vertex of the border's vertex sequence.
     *
     * @return the first vertex of this border.
     */
    public final Vertex getFirstVertex() {
        Vertex result;
        if (vertexSequence.isEmpty()) {
            result = null;
        } else {
            result = vertexSequence.getFirst();
        }
        return result;
    }

    /**
     * Getter for the last vertex of the border's vertex sequence.
     *
     * @return the last vertex of this border.
     */
    public final Vertex getLastVertex() {
        Vertex result;
        if (vertexSequence.isEmpty()) {
            result = null;
        } else {
            result = vertexSequence.getLast();
        }
        return result;
    }

    /**
     * Getter for the second last vertex of the border's vertex sequence.
     *
     * @return the second last vertex of this border.
     */
    public final Vertex getSecondLastVertex() {
        Vertex result;
        if (vertexSequence.isEmpty()) {
            result = null;
        } else {
            result = vertexSequence.get(vertexSequence.size() - 2);
        }
        return result;
    }

    /**
     * Divide the border into sub-borders. A border can be decompose from 1 to
     * many subborders, in case of a multiple crossing of the split.
     */
    public final void separateSubBorders() {
        boolean isCircular = true;
        // Moving the first vertex to an outside vertex if it exist.
        for (Vertex vertex : this.vertexSequence) {
            if (!this.split.isClose(vertex)) {
                this.changeFirstVertex(vertex);
                isCircular = false;
            }
        }
        SubBorder currentSubBorder = null;

        for (Vertex vertex : this.vertexSequence) {
            if (currentSubBorder == null && this.split.isClose(vertex)) {
                currentSubBorder = new SubBorder();
                currentSubBorder.setCircular(isCircular);
                currentSubBorder.addNextVertex(vertex);
            } else if (currentSubBorder != null) {
                if (this.split.isClose(vertex)) {
                    currentSubBorder.addNextVertex(vertex);
                } else {
                    currentSubBorder.prepare();
                    subBorders.add(currentSubBorder);
                    currentSubBorder = null;
                }
            }
        }
        if (currentSubBorder != null) {
            currentSubBorder.prepare();
            subBorders.add(currentSubBorder);
        }

    }

    /**
     * Change the first vertex of the border's vertex sequence. Used when
     * borders to join need to be aligned (in term of vertex sequence). Used
     * only on circular borders.
     */
    public final void changeFirstVertex(final Vertex vertex) {
        final LinkedList endSequence = new LinkedList<Vertex>();
        final LinkedList newStartSequence = new LinkedList<Vertex>();

        Iterator<Vertex> it = this.vertexSequence.iterator();
        while (it.hasNext()) {
            final Vertex currentVertex = it.next();
            if (currentVertex.equals(vertex)) {
                newStartSequence.add(currentVertex);
                break;
            } else {
                endSequence.add(currentVertex);
            }
        }
        while (it.hasNext()) {
            newStartSequence.add(it.next());
        }
        newStartSequence.addAll(endSequence);

        this.vertexSequence = newStartSequence;
    }
}
