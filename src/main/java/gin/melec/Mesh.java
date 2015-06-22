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
public class Mesh {

    List vertices;

    List faces;


    public Mesh() {
        this.faces = new ArrayList<Face>();
        this.vertices = new ArrayList<Vertex>();
    }

    /**
     * Add a vertex to the mesh.
     * @param vertex , the vertex to add
     */
    public void addVertex(final Vertex vertex) {
        this.vertices.add(vertex);
    }

    /**
     * Add a face to the mesh.
     * @param face , the face to add
     */
    public void addFace(final Face face) {
        this.faces.add(face);
    }

    public Vertex takeCloserVertexX(int splitPosition) {
        Vertex vertex = (Vertex)this.vertices.get(0);
        for(Object element : this.vertices) {
            Vertex currentVert = (Vertex) element;
            if (vertex.distanceToBorderX(splitPosition)
                    > currentVert.distanceToBorderX(splitPosition)) {
                vertex = currentVert;
            }
        }
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
            final Vertex vertex1 = (Vertex)
                    this.vertices.get(face.idVertex1 - 1);
            final Vertex vertex2 = (Vertex)
                    this.vertices.get(face.idVertex2 - 1);
            final Vertex vertex3 = (Vertex)
                    this.vertices.get(face.idVertex3 - 1);
            vertex1.neighbours.add(vertex2);
            vertex1.neighbours.add(vertex3);
            vertex2.neighbours.add(vertex1);
            vertex2.neighbours.add(vertex3);
            vertex3.neighbours.add(vertex1);
            vertex3.neighbours.add(vertex2);
        }
    }
}
