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
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Face implements Comparable<Face> {

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
     * @param id1 , first vertex of the face.
     * @param id2 , second vertex of the face.
     * @param id3 , third vertex of the face.
     */
    public Face(final Vertex v1, final Vertex v2,
            final Vertex v3) {
        final List<Vertex> sortingList = new ArrayList();
        sortingList.add(v1);
        sortingList.add(v2);
        sortingList.add(v3);
        Collections.sort(sortingList);
        this.vertex1 = sortingList.get(0);
        this.vertex2 = sortingList.get(1);
        this.vertex3 = sortingList.get(2);
    }

    @Override
    public final String toString() {
        return "f" + " " + vertex1.getId() + " " + vertex2.getId() + " " + vertex3.getId();
    }

    /**
     * TODO kill this one.
     *
     * @param increment
     * @return
     */
    String toIncrementString(int increment) {
        return "f" + " " + (vertex1.getId() + increment) + " " + (vertex2.getId() + increment) + " " + (vertex3.getId() + increment);
    }

    /**
     * This method search the first neighbour of the given vertex (by it's id).
     *
     * @param idOrigin , the id of the vertex for which we search neighbour.
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
     * This method search the second neighbour of the given vertex (by it's id).
     *
     * @param idOrigin , the id of the vertex for which we search neighbour.
     * @return the id of the second neighbour.
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

    Vertex getThirdVertex(Vertex v1, Vertex v2) {
        Vertex result;
        if (!this.vertex1.equals(v1) && !this.vertex1.equals(v2)) {
            result = this.vertex1;
        } else if (!this.vertex2.equals(v1) && !this.vertex2.equals(v2)) {
            result = this.vertex2;
        } else {
            result = this.vertex3;
        }
        return result;
    }

    /**
     * Check if the face contain or not the vertex given by it's id.
     *
     * @param idVertex , the id of the vertex to check.
     * @return true if the face contain the vertex, else false.
     */
    public final boolean include(final Vertex vertex) {
        boolean result;
        if (vertex.equals(this.vertex1) || vertex.equals(this.vertex2)
                || vertex.equals(this.vertex3)) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public final int compareTo(final Face face) {
        int result;
        if (this.vertex1.getId() == face.vertex1.getId()) {
            if (this.vertex2.getId() == face.vertex2.getId()) {
                result = this.vertex3.getId() - face.vertex3.getId();
            } else {
                result = this.vertex2.getId() - face.vertex2.getId();
            }
        } else {
            result = this.vertex1.getId() - face.vertex1.getId();
        }
        return result;
    }

    @Override
    public final int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.vertex1.getId();
        hash = 17 * hash + this.vertex2.getId();
        hash = 17 * hash + this.vertex3.getId();
        return hash;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Face other = (Face) obj;
        if (!this.vertex1.equals(other.vertex1)) {
            return false;
        }
        if (!this.vertex2.equals(other.vertex2)) {
            return false;
        }
        if (!this.vertex3.equals(other.vertex3)) {
            return false;
        }
        return true;
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
}
