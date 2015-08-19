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

import java.util.HashSet;
import java.util.Set;

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
    public float x;

    /**
     * The y coordonate of the vertex.
     */
    public float y;

    /**
     * The z coordonate of the vertex.
     */
    public float z;

    /**
     * The list of the neighbours of this vertex.
     */
    Set neighbours;

    /**
     * Public constructor of a vertex.
     *
     * @param id , id of the vertex.
     * @param x , x coordonate for the vertex.
     * @param y , y coordonate for the vertex.
     * @param z , z coordonate for the vertex.
     */
    public Vertex(final int id, final float x, final float y, final float z) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;

        this.neighbours = new HashSet();
    }

    /**
     * Return the string caracterizing the vertex in a .obj file.
     *
     * @return , a string describing the vertex.
     */
    @Override
    public String toString() {
        return "v " + this.x + " " + this.y + " " + this.z;
    }

    /**
     * Like toString but add also the id of the vertex.
     *
     * @return , a string describing the vertex.
     */
    public String toIdString() {
        return "v " + this.x + " " + this.y + " " + this.z + " " + this.id;
    }

    /**
     * Distance on the plan Y/Z of this vertex to an other one.
     *
     * @param ver , the vertex compared to.
     * @return , the distance on the plan Y/Z.
     */
    public float distanceOnYZ(final Vertex ver) {
        double distance = Math.sqrt(Math.pow(Math.abs(this.y - ver.y), 2)
                + Math.pow(Math.abs(this.z - ver.z), 2.0));

        return (float) distance;
    }

    /**
     * Distance on the plan X/Z of this vertex to an other one.
     *
     * @param ver , the vertex compared to.
     * @return , the distance on the plan X/Z.
     */
    public float distanceOnXZ(final Vertex ver) {
        double distance = Math.sqrt(Math.pow(Math.abs(this.x - ver.x), 2)
                + Math.pow(Math.abs(this.z - ver.z), 2.0));

        return (float) distance;
    }

    /**
     * Distance of this vertex and an other one only on the X axe.
     *
     * @param ver , the vertex compared to.
     * @return , the distance on the axe X.
     */
    public float distanceOnX(final Vertex ver) {
        return Math.abs(this.x - ver.x);
    }

    /**
     * Distance of this vertex and an other one only on the Y axe.
     *
     * @param ver , the vertex compared to.
     * @return , the distance on the axe Y.
     */
    public float distanceOnY(final Vertex ver) {
        return Math.abs(this.y - ver.y);
    }

    /**
     * Distance of this vertex to the border (vertical border).
     * @param splitPosition , the position of the border (x).
     * @return  , the distance between the vertex and the border.
     */
    public float distanceToBorderX(int splitPosition) {
        return Math.abs(this.x - splitPosition);
    }

    /**
     * Distance of this vertex to the border (horizontal border).
     * @param splitPosition , the position of the border (y).
     * @return  , the distance between the vertex and the border.
     */
    public float distanceToBorderY(int splitPosition) {
        return Math.abs(this.y - splitPosition);
    }

    /**
     * Recursive function to add every neighbor and their neighbor
     * to the garbage of the mesh the vertices belong to.
     * @param garbage , the garbage where vertices are added.
     */
    final void addNeighborToGarbage(final Set garbage) {
        garbage.add(this);
        for (Object element : this.neighbours) {
            final Vertex neighbor = (Vertex) element;
            if (!garbage.contains(neighbor)) {
                neighbor.addNeighborToGarbage(garbage);
            }
        }
    }
}