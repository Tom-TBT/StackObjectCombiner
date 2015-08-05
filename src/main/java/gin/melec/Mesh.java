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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Mesh {

    private static int ANGLE_LIMIT = 230;

    /**
     * Contain the vertices composing this mesh.
     */
    List vertices;
    /**
     * Contain the faces composing this mesh.
     */
    List faces;
    /**
     * Contain the vertex already used to make a border. This garbage is
     * emptied when a border is formed, and the corresponding vertex are removed
     * from the mesh.
     */
    Set garbage;

    /**
     * List of borders of this mesh.
     */
    List borders;

    /**
     * Current border on which one can work with.
     */
    Border currentBorder;

    /**
     * Switch the position of the mesh and switch the type of scene, meshes can
     * have up to 6 different splits.
     */
    List splits;


    /**
     * Public constructor for a mesh.
     */
    public Mesh(List splits) {
        this.faces = new ArrayList<Face>();
        this.vertices = new ArrayList<Vertex>();
        this.garbage = new HashSet();
        this.borders = new ArrayList();
        this.splits = splits;
    }

    /**
     * Add to the neighborList of each vertex, the vertex that are connected to
     * it. It relies now to the fact that in the mesh, id are originally ordered
     * .
     */
    final void doNeighborhood() {
        for (Object element : this.faces) {
            final Face face = (Face) element;
            final Vertex vertex1 = findVertex(face.idVertex1);
            final Vertex vertex2 = findVertex(face.idVertex2);
            final Vertex vertex3 = findVertex(face.idVertex3);
            if (vertex1 != null && vertex2 != null) {
                vertex1.neighbours.add(vertex2);
                vertex2.neighbours.add(vertex1);
            }
            if (vertex1 != null && vertex3 != null) {
                vertex1.neighbours.add(vertex3);
                vertex3.neighbours.add(vertex1);
            }
            if (vertex2 != null && vertex3 != null) {
                vertex2.neighbours.add(vertex3);
                vertex3.neighbours.add(vertex2);
            }
        }
    }

    /**
     * Find the vertex with the given id.
     * @param vertexID , the id of the vertex to find.
     * @return vertex with the given id.
     */
    public final Vertex findVertex(final int vertexID) {
        Vertex result = null;
        for (Object element : this.vertices) {
            final Vertex vertex = (Vertex) element;
            if (vertex.id == vertexID) {
                result = vertex;
            }
        }
        return result;
    }

    /**
     * Once a border is finish, this method is called to add all vertex related
     * to the border to the garbage.
     */
    final void completeGarbage() {
        final List vertexToCheck = new ArrayList(this.garbage);
        for (Object element : vertexToCheck) {
            final Vertex vertex = (Vertex) element;
            vertex.addNeighborToGarbage(this.garbage);
        }
    }

    /**
     * Accessor to the current border.
     * @return , the current border.
     */
    public final Border getCurrentBorder() {
        return currentBorder;
    }

    /**
     * Find and return the next vertex of the current border.
     * @param split
     * @return
     */
    public final Vertex findNextVertex(final Split split) {
        Vertex nextVertex = null;
        AngleSystem system;
        double angleVertex = 360.0;

        system = new AngleSystem(this.currentBorder.lastVertexAdded,
                this.currentBorder.scndLastVertexAdded,
                split.createVirtual(this.currentBorder.lastVertexAdded));

        if (this.currentBorder.firstVertex
                .equals(this.currentBorder.lastVertexAdded)) {
            return null;
        }
        for (final Iterator it =
                this.currentBorder.lastVertexAdded.neighbours.iterator();
                it.hasNext();) {
            final Vertex candidate = (Vertex) it.next();
            if (!this.garbage.contains(candidate)) {
                this.garbage.add(candidate);

                final double angleCandidate = system.getAngle(candidate);
                if (nextVertex == null || (angleCandidate < angleVertex
                        && angleCandidate < ANGLE_LIMIT)) {
                    nextVertex = candidate;
                    angleVertex = angleCandidate;
                }
                // TODO Ajouter condition arret pour bordure linéaire
            }
            else if (candidate.equals(this.currentBorder.firstVertex)
                    && !this.currentBorder.scndLastVertexAdded
                            .equals(this.currentBorder.firstVertex)) {
                nextVertex = candidate;
                break;
            }
        }
        return nextVertex;
    }

    /**
     * Create all the borders of a mesh and put it in the list of borders.
     */
    public void createBorders() {
        for (final Iterator it = this.splits.iterator(); it.hasNext();) {
            final Split split = (Split) it.next();
            final List borderVertices = split.findBorderVertices(this.vertices);

            while (!borderVertices.isEmpty()) {
                searchBorder(split, borderVertices);
            }
        }
    }

    /**
     * Search in the list of vertices a border and add it to the list of
     * borders. Once the border is added, remove from the list of vertices the
     * one who belong to the border.
     * @param split
     * @param borderVertices
     */
    private void searchBorder(final Split split,
            final List borderVertices) {
        this.currentBorder = new Border();
        this.borders.add(currentBorder);

        split.initiateBorder(this);
        Vertex nextVertex = this.currentBorder.lastVertexAdded;
        while (nextVertex != null) {
            nextVertex = this.findNextVertex(split);
            this.currentBorder.addNextVertex(nextVertex);
        }
        nextVertex = this.currentBorder.firstVertex;
        while (nextVertex != null && !currentBorder.isCircular) {
            nextVertex = this.findNextVertex(split);
            this.currentBorder.addPreviousVertex(nextVertex);
        }
        this.completeGarbage();
        borderVertices.removeAll(this.garbage);
        this.garbage.clear();
    }

    /**
     * This method shift the mesh, depending of its own splits.
     */
    final void shift() {
        int deltaX = 0, deltaY = 0;
        for (final Iterator it = splits.iterator(); it.hasNext();) {
            final Split split = (Split) it.next();
            if (split.xPosition() > deltaX) {
                deltaX = split.xPosition();
            }
            if (split.yPosition() > deltaY) {
                deltaY = split.yPosition();
            }
        }
        for (final Iterator it = vertices.iterator(); it.hasNext();) {
            final Vertex vertex = (Vertex) it.next();
            vertex.x += deltaX;
            vertex.y += deltaY;
        }
    }

}
