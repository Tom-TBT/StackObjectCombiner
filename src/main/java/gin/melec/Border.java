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
     * Public constructor for the border.
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

    public Border() {
        this.vertexSequence = new LinkedList();
    }

    public AbstractSplit getSplit() {
        return split;
    }

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

    public final Vertex getFirstVertex() {
        final Vertex result;
        if (vertexSequence.isEmpty()) {
            result = null;
        } else {
            result = vertexSequence.getFirst();
        }
        return result;
    }

    public final Vertex getLastVertex() {
        final Vertex result;
        if (vertexSequence.isEmpty()) {
            result = null;
        } else {
            result = vertexSequence.getLast();
        }
        return result;
    }

    public final Vertex getSecondLastVertex() {
        final Vertex result;
        if (vertexSequence.isEmpty()) {
            result = null;
        } else {
            result = vertexSequence.get(vertexSequence.size() - 2);
        }
        return result;
    }
}
