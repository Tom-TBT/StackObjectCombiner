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

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Vertex implements Comparable<Vertex>, Serializable {

    private static DecimalFormat df = new DecimalFormat("##.##");
    private static DecimalFormatSymbols dfs = new DecimalFormatSymbols();

    static {
        df.setMaximumFractionDigits(3);
        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);
    }

    /**
     * The id number of the vertex.
     */
    private int id;
    /**
     * The x coordonate of the vertex.
     */
    private float x;

    /**
     * The y coordonate of the vertex.
     */
    private float y;

    /**
     * The z coordonate of the vertex.
     */
    private float z;

    /**
     * The list of the neighbours of this vertex.
     */
    private Set<Vertex> neighbours;

    /**
     * The faces to who the vertex belong.
     */
    private Set<Face> faces;

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
        this.faces = new HashSet();
        this.neighbours = new HashSet();
    }

    /**
     * Return the string caracterizing the vertex in a .obj file.
     *
     * @return , a string describing the vertex.
     */
    @Override
    public final String toString() {
        return "v " + df.format(this.x) + " " + df.format(this.y) + " "
                + df.format(this.z);
    }

    /**
     * Return the string caracterizing the vertex with it's ID.
     *
     * @return , a string describing the vertex with it's ID.
     */
    public final String toIdString() {
        return "v " + df.format(this.x) + " " + df.format(this.y) + " "
                + df.format(this.z) + " " + this.id;
    }

    /**
     * Recursive function to add every neighbor and their neighbor
     * to the garbage of the mesh the vertices belong to.
     * @param garbage , the garbage where vertices are added.
     */
    public final void addNeighborToGarbage(final Set garbage, final Set primers) {
        garbage.add(this);
        for (Vertex neighbor : this.neighbours) {
            if (!garbage.contains(neighbor) && primers.contains(neighbor)) {
                neighbor.addNeighborToGarbage(garbage, primers);
            }
        }
    }

    /**
     * Give the neighbour of this vertex with the given id.
     * @param idNeighbour , the id of the neighbour.
     * @return the vertex with the corresponding id.
     */
    public final Vertex getNeighbour(final int idNeighbour) {
        Vertex result = null;
        for (Vertex candidat : this.neighbours) {
            if (candidat.id == idNeighbour) {
                result = candidat;
                break;
            }
        }
        return result;
    }

    public final double distanceTo(final Vertex vertex) {
        return Math.sqrt(Math.pow((double) this.x - (double) vertex.x, 2)
                + Math.pow((double) this.y - (double) vertex.y, 2)
                + Math.pow((double) this.z - (double) vertex.z, 2));
    }

    @Override
    public final int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.id;
        hash = 89 * hash + Float.floatToIntBits(this.x);
        hash = 89 * hash + Float.floatToIntBits(this.y);
        hash = 89 * hash + Float.floatToIntBits(this.z);
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
        final Vertex other = (Vertex) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }


    @Override
    public final int compareTo(final Vertex vertex) {
        return this.id - vertex.id;
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Set<Vertex> getNeighbours() {
        return neighbours;
    }

    public Set<Face> getFaces() {
        return faces;
    }
}
