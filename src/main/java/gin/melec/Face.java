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

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Face {

    /**
     * The id of the first vertex of this face.
     */
    private final Vertex vertex1;

    /**
     * The id of the second vertex of this face.
     */
    private final Vertex vertex2;

    /**
     * The id of the third vertex of this face.
     */
    private final Vertex vertex3;

    /**
     * Public constructor of a face, taking id of three vertex. Vertex are
     * ordered with their id.
     *
     * @param v1 , first vertex of the face.
     * @param v2 , second vertex of the face.
     * @param v3 , third vertex of the face.
     */
    public Face(final Vertex v1, final Vertex v2,
            final Vertex v3) {
        this.vertex1 = v1;
        this.vertex2 = v2;
        this.vertex3 = v3;
    }

    @Override
    public final String toString() {
        return "f" + " " + vertex1.getId() + " " + vertex2.getId() + " "
                + vertex3.getId();
    }

    /**
     * This method search the first neighbour of the given vertex (by it's id).
     *
     * @param vertex , the vertex for which we search neighbour.
     * @return the id of the first neighbour.
     */
    public final Vertex getFirstNeighbour(final Vertex vertex) {
        Vertex result;
        if (vertex.equals(this.vertex1)) {
            result = this.vertex2;
        } else {
            result = this.vertex1;
        }
        return result;
    }

    /**
     * This method search the second neighbour of the given vertex.
     *
     * @param vertex , the vertex for which we search neighbour.
     * @return the second neighbour.
     */
    public final Vertex getSecondNeighbour(final Vertex vertex) {
        Vertex result;
        if (vertex.equals(this.vertex3)) {
            result = this.vertex2;
        } else {
            result = this.vertex3;
        }
        return result;
    }

    /**
     * Check if the face contain or not the vertex given by it's id.
     *
     * @param vertex , the vertex to check.
     * @return true if the face contain the vertex, else false.
     */
    public final boolean include(final Vertex vertex) {
        return vertex.equals(this.vertex1) || vertex.equals(this.vertex2)
                || vertex.equals(this.vertex3);
    }

    /**
     * Getter of the id of the first vertex of this face.
     *
     * @return the id of the first vertex of this face.
     */
    public final Vertex getVertex1() {
        return vertex1;
    }

    /**
     * Getter of the id of the second vertex of this face.
     *
     * @return the id of the second vertex of this face.
     */
    public final Vertex getVertex2() {
        return vertex2;
    }

    /**
     * Getter of the id of the third vertex of this face.
     *
     * @return the id of the third vertex of this face.
     */
    public final Vertex getVertex3() {
        return vertex3;
    }

    /**
     * Function that looks for the orders of the vertices in the face. The order
     * give the normal vector of the face.
     * @param v1, the first vertex.
     * @param v2, the second vertex.
     * @return true if the vertex 2 follows the vertex 1.
     */
    public boolean vertexFollows(final Vertex v1, final Vertex v2) {
        return (this.vertex1 == v1 && this.vertex2 == v2) ||
                (this.vertex2 == v1 && this.vertex3 == v2) ||
                (this.vertex3 == v1 && this.vertex1 == v2);
    }
}
