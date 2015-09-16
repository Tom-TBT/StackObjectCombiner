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
    private LinkedList<Vertex> vertexSequence;

    /**
     * The distance between the first and the last vertex of the border.
     */
    private double straightLenght;

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
    private boolean isCircular;

    /**
     * Public constructor for the border. This constructor is used when the
     * border need to be detected. The constructor look at the primers of the
     * given mesh to build the new border.
     * @param mesh , the mesh from which a new border is created.
     */
    public Border(final Mesh mesh) {
        this.vertexSequence = new LinkedList();

        Vertex currentVertex = null;
        Vertex candidat = null;
        float distanceTmp, distanceFirstVertex = 0;
        for (AbstractSplit currentSplit : mesh.getSplits()) {
            currentVertex = currentSplit.findCloserVertex(mesh.getPrimers());
            distanceTmp = currentSplit.distanceTo(currentVertex);
            if (getFirstVertex() == null || distanceTmp < distanceFirstVertex) {
                distanceFirstVertex = distanceTmp;
                candidat = currentVertex;
                this.split = currentSplit;
            }
        }
        vertexSequence.add(candidat);

        mesh.setFacesToVertex(getFirstVertex());
        vertexSequence.add(this.split.findCloserVertex(
                getFirstVertex().getNeighbours()));

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
     * @return the split of the border.
     */
    public AbstractSplit getSplit() {
        return split;
    }

    /**
     * Getter for the vertex sequence of the border.
     * @return the vertex sequence of the border
     */
    public List<Vertex> getVertexSequence() {
        return vertexSequence;
    }

    /**
     * Add the given vertex to the end of the border.
     * @param vertex , the vertex to add.
     */
    public final void addNextVertex(final Vertex vertex) {
        if (vertex != null) {
            this.vertexSequence.add(vertex);
        }
    }

    /**
     * This method prepare the attributes of the border. This set up for example
     * the lenght, or the position of the center of the border. These parameters
     * are used for automatic matching of the borders.
     */
    public final void prepare() {
        straightLenght = getFirstVertex().distanceTo(getLastVertex());

        cumulLenght = 0;
        float x = 0, y = 0, z = 0;
        for (int i = 0; i < vertexSequence.size() - 1; i++) {
            cumulLenght += vertexSequence.get(i)
                    .distanceTo(vertexSequence.get(i + 1));
            x += vertexSequence.get(i).getX();
            y += vertexSequence.get(i).getY();
            z += vertexSequence.get(i).getZ();
        }
        x = x / vertexSequence.size();
        y = y / vertexSequence.size();
        z = z / vertexSequence.size();
        this.center = new Vertex(0, x, y, z);
    }

    /**
     * Getter for the first vertex of the border's vertex sequence.
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
            Vertex currentVertex = it.next();
            if (currentVertex == vertex) {
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
     * This method take a border as reference, and align the border on. Can then
     * change the first vertex of the border, and the order of the sequence.
     * @param border , the border on which this border is aligned.
     */
    public final void alignOn(Border border) {
        final Vertex firstVertex2 = border.getFirstVertex();
        Vertex firstVertex1 = this.getFirstVertex();
        Vertex previousVertex1, nextVertex1, nextVertex2;

        //TODO if (this.isCircular) {
        for (Iterator<Vertex> it = this.vertexSequence.iterator();
                it.hasNext();) {
            Vertex candidate = it.next();
            if (candidate.distanceTo(firstVertex2)
                    < firstVertex1.distanceTo(firstVertex2)) {
                firstVertex1 = candidate;
            }
        }
        if (firstVertex1 != this.getFirstVertex()) {
            previousVertex1 = this.vertexSequence.get(this.vertexSequence.indexOf(firstVertex1) - 1);
        } else {
            previousVertex1 = this.vertexSequence.getLast();
        }
        if (firstVertex1 != this.vertexSequence.getLast()) {
            nextVertex1 = this.vertexSequence.get(this.vertexSequence.indexOf(firstVertex1) + 1);
        } else {
            nextVertex1 = this.vertexSequence.getFirst();
        }
        nextVertex2 = border.getVertexSequence().get(1);

        if (nextVertex1.distanceTo(nextVertex2)
                > previousVertex1.distanceTo(nextVertex2)) {
            this.revertSequence();
        }
        this.changeFirstVertex(firstVertex1);
        //}

    }


}
