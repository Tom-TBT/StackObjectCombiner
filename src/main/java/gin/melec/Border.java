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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Border implements Serializable {

    /**
     * The split that initiate this border.
     */
    private AbstractSplit split;

    /**
     * The sequence of vertex forming the border.
     */
    private LinkedList<Vertex> vertexSequence;

    /**
     * The real lenght of the border.
     */
    private double cumulLenght;

    /**
     * The center of the border.
     */
    private Vertex center;

    /**
     * The center of the border.
     */
    private boolean circular;

    /**
     * Public constructor for the border. This constructor is used when the
     * border need to be detected. The constructor look at the primers of the
     * given mesh to build the new border.
     *
     * @param mesh , the mesh from which a new border is created.
     */
    public Border(final Mesh mesh) {
        this.vertexSequence = new LinkedList();

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
            mesh.setNeighbourhoodToVertex(vertex);
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
    public final List separateSubBorders() {
        boolean isCircular = true;
        List result = new ArrayList();
        // Moving the first vertex to an outside vertex if it exist.
        for (Vertex vertex : this.vertexSequence) {
            if (!this.split.isClose(vertex)) {
                this.changeFirstVertex(vertex);
                isCircular = false;
            }
        }
        Border currentSubBorder = null;

        for (Vertex vertex : this.vertexSequence) {
            if (currentSubBorder == null && this.split.isClose(vertex)) {
                currentSubBorder = new Border();
                currentSubBorder.addNextVertex(vertex);
            } else if (currentSubBorder != null) {
                if (this.split.isClose(vertex)) {
                    currentSubBorder.addNextVertex(vertex);
                } else {
                    currentSubBorder.prepare();
                    result.add(currentSubBorder);
                    currentSubBorder = null;
                }
            }
        }
        if (currentSubBorder != null) {
            currentSubBorder.prepare();
            result.add(currentSubBorder);
        }
        return result;
    }

    /**
     * This method prepare the attributes of the border. This set up for example
     * the lenght, or the position of the center of the border. These parameters
     * are used for automatic matching of the borders.
     */
    private final void prepare() {
        cumulLenght = 0;
        float x = 0, y = 0, z = 0;
        for (int i = 0; i < vertexSequence.size() - 1; i++) {
            cumulLenght += vertexSequence.get(i)
                    .distanceTo(vertexSequence.get(i + 1));
            x += vertexSequence.get(i).getX();
            y += vertexSequence.get(i).getY();
            z += vertexSequence.get(i).getZ();
        }
        x += vertexSequence.getLast().getX();
        y += vertexSequence.getLast().getY();
        z += vertexSequence.getLast().getZ();
        x = x / vertexSequence.size();
        y = y / vertexSequence.size();
        z = z / vertexSequence.size();
        this.center = new Vertex(0, x, y, z);

        this.circular = this.getFirstVertex().getNeighbours()
                .contains(this.getLastVertex());
    }

    /**
     * Compute the distance between two borders. The distance depend of the
     * lenght of the two borders, the distance between their first and last
     * vertex if the border is not circular, and it finally depend of the
     * distance between their centers.
     *
     * @param border , the border to compare to this one.
     * @return the distance between the two borders.
     */
    protected final double distanceTo(final Border border) {
        double result = 0;

        result += Math.abs(this.cumulLenght - border.cumulLenght);
        result += this.center.distanceTo(border.center);
        if (!this.circular && !border.circular) {
            // Compute the distance between the first plus the last vertex of
            // the two borders.
            result += Math.min(this.getFirstVertex().
                    distanceTo(border.getFirstVertex())
                    + this.getLastVertex()
                    .distanceTo(border.getLastVertex()), // And
                    this.getFirstVertex()
                    .distanceTo(border.getLastVertex())
                    + this.getLastVertex()
                    .distanceTo(border.getFirstVertex()));
        }
        return result;
    }

    /**
     * Change the first vertex of the border's vertex sequence. Used when
     * borders to join need to be aligned (in term of vertex sequence). Used
     * only on circular borders.
     */
    private final void changeFirstVertex(final Vertex vertex) {
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

    /**
     * Revert the sequence of the border. Used when borders to join need to be
     * aligned (in term of vertex sequence).
     */
    public final void revertSequence() {
        final LinkedList newSequence = new LinkedList<Vertex>();
        for (Iterator it = this.vertexSequence.descendingIterator();
                it.hasNext();) {
            newSequence.add(it.next());
        }
        this.vertexSequence = newSequence;
        if (this.isCircular()) {
            // The previous first vertex need to be put back in 1st
            this.vertexSequence.add(this.vertexSequence.removeFirst());
        }
    }

    /**
     * This method take a border as reference, and align the border on. Can then
     * change the first vertex of the border, and the order of the sequence. If
     * the borders are circular, the first vertex of the border given is used as
     * a reference and the first vertex of the sequence of this border is
     * choosed as the closer vertex to the reference. If the borders are linear,
     * it just check if this border need to be inverted.
     *
     * @param border , the border on which this border is aligned.
     */
    public final void alignOn(Border border) {
        Vertex firstVertexThis = this.getFirstVertex();
        final Vertex firstVertexRef = border.getFirstVertex();
        Vertex previousVertexThis, nextVertexThis, nextVertexRef;

        if (this.isCircular() && border.isCircular()) {
            for (Vertex candidate : this.vertexSequence) {
                if (candidate.distanceTo(firstVertexRef)
                        < firstVertexThis.distanceTo(firstVertexRef)) {
                    firstVertexThis = candidate;
                }
            }
            if (firstVertexThis != this.getFirstVertex()) {
                previousVertexThis = this.vertexSequence.get(this.vertexSequence.indexOf(firstVertexThis) - 1);
            } else {
                previousVertexThis = this.vertexSequence.getLast();
            }
            if (firstVertexThis != this.vertexSequence.getLast()) {
                nextVertexThis = this.vertexSequence.get(this.vertexSequence.indexOf(firstVertexThis) + 1);
            } else {
                nextVertexThis = this.vertexSequence.getFirst();
            }
            nextVertexRef = border.getVertexSequence().get(1);

            if (nextVertexThis.distanceTo(nextVertexRef)
                    > previousVertexThis.distanceTo(nextVertexRef)) {
                this.revertSequence();
            }
            this.changeFirstVertex(firstVertexThis);
        } else if (!this.isCircular() && !border.isCircular()){
            if (this.getFirstVertex().distanceTo(border.getFirstVertex())
                    > this.getLastVertex().distanceTo(border.getFirstVertex())) {
                this.revertSequence();
            }
        } else {
            // TODO Exception, not two borders are linear or circular.
        }

    }

    public final boolean isCircular() {
        return circular;
    }
}
