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

import java.util.Arrays;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Face implements Comparable<Face> {

    /**
     * The id of the first vertex of this face.
     */
    private final int idVertex1;

    /**
     * The id of the second vertex of this face.
     */
    private final int idVertex2;

    /**
     * The id of the third vertex of this face.
     */
    private final int idVertex3;

    /**
     * Public constructor of a face, taking id of three vertex.
     * @param id1 , first vertex of the face.
     * @param id2 , second vertex of the face.
     * @param id3 , third vertex of the face.
     */
    public Face(final int id1, final int id2,
            final int id3) {
        final int[] idArray = new int[3];
        idArray[0] = id1; idArray[1] = id2; idArray[2] = id3;
        Arrays.sort(idArray);
        this.idVertex1 = idArray[0];
        this.idVertex2 = idArray[1];
        this.idVertex3 = idArray[2];
    }

    @Override
    public final String toString() {
        return "f" + " " + idVertex1 + " " + idVertex2 + " " + idVertex3;
    }


    String toIncrementString(int increment) {
        return "f" + " " + (idVertex1+increment) + " " + (idVertex2+increment) + " " + (idVertex3+increment);
    }

    /**
     * This method search the first neighbour of the given vertex (by it's id).
     * @param idOrigin , the id of the vertex for which we search neighbour.
     * @return the id of the first neighbour.
     */
    final int getFirstNeighbour(final int idOrigin) {
        int result;
        if (idOrigin == idVertex1) {
            result = idVertex2;
        } else {
            result = idVertex1;
        }
        return result;
    }
    /**
     * This method search the second neighbour of the given vertex (by it's id).
     * @param idOrigin , the id of the vertex for which we search neighbour.
     * @return the id of the second neighbour.
     */
    public final int getSecondNeighbour(final int idOrigin) {
        int result;
        if (idOrigin == idVertex3) {
            result = idVertex2;
        } else {
            result = idVertex3;
        }
        return result;
    }

    public final boolean include(final int idVertex) {
        boolean result;
        if (idVertex == idVertex1 || idVertex == idVertex2
                || idVertex == idVertex3) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public final int compareTo(final Face face) {
        int result;
        if (this.idVertex1 == face.idVertex1) {
            if (this.idVertex2 == face.idVertex2) {
                result = this.idVertex3 - face.idVertex3;
            } else {
                result = this.idVertex2 - face.idVertex2;
            }
        } else {
            result = this.idVertex1 - face.idVertex1;
        }
        return result;
    }

    @Override
    public final int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.idVertex1;
        hash = 17 * hash + this.idVertex2;
        hash = 17 * hash + this.idVertex3;
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
        if (this.idVertex1 != other.idVertex1) {
            return false;
        }
        if (this.idVertex2 != other.idVertex2) {
            return false;
        }
        if (this.idVertex3 != other.idVertex3) {
            return false;
        }
        return true;
    }

    public final int getIdVertex1() {
        return idVertex1;
    }

    public final int getIdVertex2() {
        return idVertex2;
    }

    public final int getIdVertex3() {
        return idVertex3;
    }
}
