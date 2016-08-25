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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Vertex implements Comparable<Vertex>{

    /**
     * A decimal format used to export vertex with only 3 fraction digits.
     */
    private static final DecimalFormat DF = new DecimalFormat("##.##");
    private static final DecimalFormatSymbols DFS = new DecimalFormatSymbols();

    static {
        DF.setMaximumFractionDigits(5);
        DFS.setDecimalSeparator('.');
        DF.setDecimalFormatSymbols(DFS);
    }

    /**
     * The id number of the vertex.
     */
    protected int id;
    /**
     * The x coordonate of the vertex.
     */
    protected float x;

    /**
     * The y coordonate of the vertex.
     */
    protected float y;

    /**
     * The z coordonate of the vertex.
     */
    protected float z;

    private Set<Vertex> uniqueNeighbours;

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
        this.uniqueNeighbours = new HashSet();
    }

    /**
     * Return the string caracterizing the vertex in a .obj file.
     *
     * @return , a string describing the vertex.
     */
    @Override
    public final String toString() {
        return "v " + DF.format(this.x) + " " + DF.format(this.y) + " "
                + DF.format(this.z);
    }

    /**
     * Compute the distance between this vertex and an other one.
     * @param vertex , the vertex to compute the distance to this vertex.
     * @return the distance between the two vertex.
     */
    public final double distanceTo(final Vertex vertex) {
        return Math.sqrt(Math.pow((double) this.x - (double) vertex.x, 2)
                + Math.pow((double) this.y - (double) vertex.y, 2)
                + Math.pow((double) this.z - (double) vertex.z, 2));
    }

    /**
     * Distance to the vertex, according only to two dimensions. The split give
     * the orientation of the plane, and so which dimension won't be used.
     * @param vertex
     * @param split
     * @return
     */
    public final double distanceTo(final Vertex vertex, AbstractSplit split) {
        double result = 0;
        if (split instanceof WidthSplit) {
            result = Math.sqrt(Math.pow((double) this.z - (double) vertex.z, 2)
                + Math.pow((double) this.y - (double) vertex.y, 2));
        } else if (split instanceof HeightSplit) {
            result = Math.sqrt(Math.pow((double) this.x - (double) vertex.x, 2)
                + Math.pow((double) this.z - (double) vertex.z, 2));
        } else if (split instanceof DepthSplit) {
            result = Math.sqrt(Math.pow((double) this.x - (double) vertex.x, 2)
                + Math.pow((double) this.y - (double) vertex.y, 2));
        }
        return result;
    }

    /**
     * Return the other unique vertex. A vertex that is creating a border have
     * 2 neighbours on the border. So this function retrieve the vertex that is not
     * in parameter.
     * @param previous, the other unique that we don't want to have.
     * @return the other unique.
     */
    public final Vertex getOtherUnique(Vertex previous) {
        Iterator<Vertex> it = this.uniqueNeighbours.iterator();
        Vertex result = it.next();
        if (previous == result) {
            result = it.next();
        }
        return result;
    }

    /**
     * Increment the Id of the vertex by the given shift. This is used when two
     * meshed are merged.
     * @param idShift , the shift to apply to the vertex's id.
     */
    public final void incrementId(final int idShift) {
        this.id += idShift;
    }

    /**
     * We add to a a vertex all his neighbours seen on the faces. If a vertex
     * receive twice the same vertex, it means that the given vertex is not part
     * of the border for this vertex. At the end of the reading of a mesh, if a
     * vertex still have neighbours, it means that this vertex is part of the
     * border and that it's neighbours are also part of the border.
     * @param neighb1, the first neighbour to add.
     * @param neighb2, the second neighbour to add.
     */
    public final void addUnique(Vertex neighb1, Vertex neighb2) {
        if (!this.uniqueNeighbours.add(neighb1)) {
            this.uniqueNeighbours.remove(neighb1);
        }
        if (!this.uniqueNeighbours.add(neighb2)) {
            this.uniqueNeighbours.remove(neighb2);
        }
    }

    public final boolean isBorderVertex() {
        boolean result = !this.uniqueNeighbours.isEmpty();
        if (!result) {
            this.uniqueNeighbours = null;
        }
        return result;
    }

    public final Set<Vertex> getUniqueNeighbours() {
        return this.uniqueNeighbours;
    }

    /**
     * Return the closer vertex from a list to this one.
     * @param vertexSequence , the list of vertex to be compared.
     * @return the closer vertex of this vertex.
     */
    public final Vertex findCloserIn(List<Vertex> vertexSequence) {
        Vertex result = null;
        for (Vertex vertex : vertexSequence) {
            if (result == null) {
                result = vertex;
            } else if (this.distanceTo(vertex) < this.distanceTo(result)){
                result = vertex;
            }
        }
        return result;
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

    /**
     * Getter of the attribute id.
     * @return the id of the vertex.
     */
    public final int getId() {
        return id;
    }
    /**
     * Getter of the attribute x.
     * @return the x of the vertex.
     */
    public final float getX() {
        return x;
    }
    /**
     * Setter of the attribute x.
     * @param x , the new value for the x of this vertex.
     */
    public final void setX(double x) {
        this.x = (float) x;
    }
    /**
     * Getter of the attribute y.
     * @return the y of the vertex.
     */
    public final float getY() {
        return y;
    }

    /**
     * Setter of the attribute y.
     * @param y , the new value for the y of this vertex.
     */
    public final void setY(double y) {
        this.y = (float) y;
    }
    /**
     * Getter of the attribute z.
     * @return the z of the vertex.
     */
    public final float getZ() {
        return z;
    }
    /**
     * Setter of the attribute z.
     * @param z , the new value for the z of this vertex.
     */
    public final void setZ(double z) {
        this.z = (float)z;
    }
}
