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
public class Vertex {

    /**
     * The id number of the vertex.
     */
    int id;
    /**
     * The x coordonate of the vertex.
     */
    float x;

    /**
     * The y coordonate of the vertex.
     */
    float y;

    /**
     * The z coordonate of the vertex.
     */
    float z;

    /**
     * The previous vertex on the edge of the object.
     */
    Vertex previousVertex;

    /**
     * The next vertex on the edge of the object.
     */
    Vertex nextVertex;

    /**
     * Public constructor of a vertex.
     * @param x , x coordonate for the vertex.
     * @param y , y coordonate for the vertex.
     * @param z , z coordonate for the vertex.
     */
    public Vertex(final int id, final float x, final float y, final float z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "v " + this.x + " " + this.y + " " + this.z;
    }



}
