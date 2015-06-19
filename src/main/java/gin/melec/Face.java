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
     * @param idVertex1
     * @param idVertex2
     * @param idVertex3
     */
    public Face (final int idVertex1, final int idVertex2, final int idVertex3) {
        this.idVertex1 = idVertex1;
        this.idVertex2 = idVertex2;
        this.idVertex3 = idVertex3;
    }

    @Override
    public String toString() {
        return "f" + " " + idVertex1 + " " + idVertex2 + " " + idVertex3;
    }



}
