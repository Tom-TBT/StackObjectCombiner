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
     * Public constructor for a mesh.
     */
    public Mesh() {
        this.faces = new ArrayList<Face>();
        this.vertices = new ArrayList<Vertex>();
        this.garbage = new HashSet();
        this.borders = new ArrayList();
    }

    /**
     * Add a vertex to the mesh.
     * @param vertex , the vertex to add
     */
    public final void addVertex(final Vertex vertex) {
        this.vertices.add(vertex);
    }

    /**
     * Add a face to the mesh.
     * @param face , the face to add
     */
    public final void addFace(final Face face) {
        this.faces.add(face);
    }

    /**
     * Search in the mesh the closest vertex to the split. This is usefull to
     * start a border because a new border comport obviously the closest vertex
     * to the border.
     * @param splitPosition , the position of the split.
     * @return vertex, the closest one in the mesh.
     */
    private final Vertex takeCloserVertexX(final int splitPosition) {
        Vertex vertex = (Vertex) this.vertices.get(0);
        for (final Iterator it = this.vertices.iterator(); it.hasNext();) {
            final Object element = it.next();
            final Vertex currentVert = (Vertex) element;
            if (vertex.distanceToBorderX(splitPosition)
                    > currentVert.distanceToBorderX(splitPosition)) {
                vertex = currentVert;
            }
        }
        this.garbage.add(vertex);
        return vertex;
    }

    /**
     * Search in the mesh the closest vertex to the split. This is usefull to
     * start a border because a new border comport obviously the closest vertex
     * to the border.
     * @param splitPosition , the position of the split.
     * @return vertex, the closest one in the mesh.
     */
    private final Vertex takeCloserVertexY(final int splitPosition) {
        Vertex vertex = (Vertex) this.vertices.get(0);
        for (final Iterator it = this.vertices.iterator(); it.hasNext();) {
            final Object element = it.next();
            final Vertex currentVert = (Vertex) element;
            if (vertex.distanceToBorderY(splitPosition)
                    > currentVert.distanceToBorderY(splitPosition)) {
                vertex = currentVert;
            }
        }
        this.garbage.add(vertex); // TODO Maybe remove this line
        return vertex;
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
     * @param id , the id of the vertex to find.
     * @return vertex with the given id.
     */
    public Vertex findVertex(final int id) {
        for (Object element : this.vertices) {
            final Vertex vertex = (Vertex) element;
            if (vertex.id == id) {
                return vertex;
            }
        }
        return null; // TODO Should never came here, but ugly
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
     * Create a new border for this mesh, and initialize it with the closest
     * vertex to the split.
     * @param splitPosition , the position of the split.
     * @return the first vertex.
     */
    public final Vertex newBorderX(final int splitPosition) {
        final Border border = new Border(this.takeCloserVertexX(splitPosition));
        this.borders.add(border);
        this.currentBorder = border;
        return currentBorder.firstVertex;
    }

    /**
     * Create a new border for this mesh, and initialize it with the closest
     * vertex to the split.
     * @param splitPosition , the position of the split.
     * @return the first vertex.
     */
    public final Vertex newBorderY(final int splitPosition) {
        final Border border = new Border(this.takeCloserVertexY(splitPosition));
        this.borders.add(border);
        this.currentBorder = border;
        return currentBorder.firstVertex;
    }

    /**
     * Accessor to the current border.
     * @return , the current border.
     */
    public final Border getCurrentBorder() {
        return currentBorder;
    }

}
