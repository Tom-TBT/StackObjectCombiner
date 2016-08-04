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
import java.util.ArrayList;
import java.util.Collection;
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

    /**
     * The faces to who the vertex belong.
     */
    private final transient Set<Face> faces;

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
        this.faces = new HashSet();
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
     * Recursive function to add every neighbor and their neighbor
     * to the garbage of the mesh the vertices belong to.
     * @param garbage , the garbage where vertices are added.
     * @param primers, the set containing all the vertices that can be added.
     */
    public final void addNeighbourToGarbage(final Set garbage, final Set primers) {
        garbage.add(this);
        for (Vertex neighbour : this.getNeighbours()) {
            if (!garbage.contains(neighbour) && primers.contains(neighbour)) {
                neighbour.addNeighbourToGarbage(garbage, primers);
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
        for (Vertex candidat : this.getNeighbours()) {
            if (candidat.id == idNeighbour) {
                result = candidat;
                break;
            }
        }
        return result;
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
     * Find the closer vertex to this vertex between two candidates.
     * @param candidate1 , the first candidate.
     * @param candidate2 , the second candidate.
     * @return the closer of the two vertex.
     */
    public final Vertex whichCloser(final Vertex candidate1
            , final Vertex candidate2) {
        Vertex result;
        if (candidate2 != null && candidate1 != null
               && (this.distanceTo(candidate2) < this.distanceTo(candidate1))) {
            result = candidate2;
        } else {
            result = candidate1;
        }
        return result;
    }

    /**
     * This method check if the Vertex belong to a border or not. This is
     * checked by regarding if their is a circularity in his neighbours.
     * @return true if it belong to a border, else false.
     */
    public final boolean belongToBorder() {
        boolean result = true;
        final List<Vertex> tmpNeighbours = new ArrayList();
        final List<Face> facesRemaining = new ArrayList();
        tmpNeighbours.addAll(this.getNeighbours());
        facesRemaining.addAll(this.faces);

        final Vertex firstVertex = tmpNeighbours.get(0);
        Vertex currentVertex = firstVertex;
        Face currentFace = firstVertex.getFaceIncluding(facesRemaining);
        facesRemaining.remove(currentFace);
        while (currentFace != null) {
            currentVertex = currentFace.getThirdVertex(this, currentVertex);
            currentFace = currentVertex.getFaceIncluding(facesRemaining);
            facesRemaining.remove(currentFace);
            if (currentVertex.equals(firstVertex)) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * Give the face containing the vertex. The face is only search in a subset.
     * @param facesRemaining , the subset of faces.
     * @return the face containing the vertex.
     */
    public final Face getFaceIncluding(final Collection<Face> facesRemaining) {
        Face result = null;
        for (Face face : facesRemaining) {
            if (face.include(this)) {
                result = face;
                break;
            }
        }
        return result;
    }

    public final boolean isLogicalNext(final Vertex vertex) {
        boolean result = false;
        for (Face face: this.faces) {
            if (face.include(vertex)) {
                result = face.vertexFollows(this, vertex);
                break;
            }
        }
        return result;
    }

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
    /**
     * Getter of the attribute neighbours.
     * @return the neighbours of the vertex.
     */
    public final Set<Vertex> getNeighbours() {
        Set<Vertex> neighbSet = new HashSet();
        for (Face face : this.getFaces()) {
            neighbSet.add(face.getFirstNeighbour(this));
            neighbSet.add(face.getSecondNeighbour(this));
        }
        return neighbSet;
    }

    public final void addUnique(Vertex neighb1, Vertex neighb2) {
        //this.faces.add(face);
        if (!this.uniqueNeighbours.add(neighb1)) {
            this.uniqueNeighbours.remove(neighb1);
        }
        if (!this.uniqueNeighbours.add(neighb2)) {
            this.uniqueNeighbours.remove(neighb2);
        }
    }

    public final void addFace(Face face) {
        this.faces.add(face);
    }

    public final boolean isBorderVertex() {
        boolean result = !this.uniqueNeighbours.isEmpty();
        if (!result) {
            this.uniqueNeighbours = null;
        }
        return result;
    }

    /**
     * Getter of the attribute faces.
     * @return the faces of the vertex.
     */
    public final Set<Face> getFaces() {
        return faces;
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
}
