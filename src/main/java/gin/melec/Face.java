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
    int idVertex1;

    /**
     * The id of the second vertex of this face.
     */
    int idVertex2;

    /**
     * The id of the third vertex of this face.
     */
    int idVertex3;

    /**
     * Public constructor of a face, taking id of three vertex.
     * @param idVertex1 , first vertex of the face.
     * @param idVertex2 , second vertex of the face.
     * @param idVertex3 , third vertex of the face.
     */
    public Face(final int idVertex1, final int idVertex2,
            final int idVertex3) {
        this.idVertex1 = idVertex1;
        this.idVertex2 = idVertex2;
        this.idVertex3 = idVertex3;
    }

    @Override
    public final String toString() {
        return "f" + " " + idVertex1 + " " + idVertex2 + " " + idVertex3;
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
    final int getSecondNeighbour(final int idOrigin) {
        int result;
        if (idOrigin == idVertex3) {
            result = idVertex2;
        } else {
            result = idVertex3;
        }
        return result;
    }



}
