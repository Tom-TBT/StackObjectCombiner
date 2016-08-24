/*
 * Copyright (C) 2016 ImageJ
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package gin.melec;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 *
 * @author tom
 */
public class FlatBorder{

    private List elements;

    private CustomArea customArea;

    private AbstractSplit split;

    private final Mesh parentMesh;

    public FlatBorder(Mesh mesh) {
        this.elements = new ArrayList();
        this.customArea = null;
        this.parentMesh = mesh;
    }

    public void addElement(Object element) {
        if (element instanceof List) {
            List listElement = (List)element;
            if (listElement.get(0) instanceof Vertex) {
                this.elements.add(element);
            }
        } else if (element instanceof Vertex) {
            this.elements.add(element);
        }
    }

    /**
     * Get a simple sequence of the flat border. It takes all the vertices of
     * the lists and add to it the in between corner vertices.
     * @return a single sequence representation of the flat border.
     */
    private List<Vertex> getSingleSequence() {
        List<Vertex> result = new ArrayList();
        for (Object o : this.elements) {
            if (o instanceof Vertex) {
                result.add((Vertex)o);
            } else {
                List<Vertex> tmpList = (List)o;
                int i;
                for(i=0; i < tmpList.size(); i++){
                    result.add(tmpList.get(i));
                }
            }
        }
        return result;
    }

    /**
     * Convert a sequence of vertex into a 2D Vector sequence. (because a flat
     * border is actually flat !! o_O)
     * @param seq
     * @return
     */
    private List<Vector2D> getVectors(List<Vertex> seq) {
        List<Vector2D> result = new ArrayList();
        for (Vertex v: seq) {
            result.add(split.getVector(v));
        }
        return result;
    }

    /**
     * Create an area from this vertex sequence and compute some geometrical
     * properties: Perimeter, Barycentre and Area.
     * @param split
     */
    public void computeProperties(final AbstractSplit split) {
        this.split = split;
        List<Vertex> singleSequence = getSingleSequence();
        List<Vector2D> vectors = getVectors(singleSequence);
        this.customArea = new CustomArea(vectors);
        if (this.customArea.getSize() < 0) { // The sequence need to be reverted
            this.revertElements();
            singleSequence = getSingleSequence();
            vectors = getVectors(singleSequence);
            this.customArea = new CustomArea(vectors);
        }
        this.customArea.computeProperties(vectors);
    }

    /**
     * Revert the elements of the flat border.
     */
    private void revertElements() {
        List newElements = new ArrayList();
        for (Object o:this.elements) {
            if (o instanceof Vertex) {
                newElements.add(0, o);
            } else {
                List revertedList = new ArrayList();
                List oldList = (List) o;
                for (int i = oldList.size()-1; i >=0; i--) {
                    revertedList.add(oldList.get(i));
                }
                newElements.add(0,revertedList);
            }
        }
        this.elements = newElements;
        while(this.elements.get(0) instanceof Vertex) {
            this.elements.add(this.elements.get(0));
            this.elements.remove(0);
            // We want to push to the end of the sequence the corners. The
            // sequence must start with a real vertex
        }
    }

    public Point2D getBarycenter() {
        return this.customArea.barycentre;
    }

    public double getSize() {
        return this.customArea.getSize();
    }

    public double getPerimeter() {
        return this.customArea.perimeter;
    }

    public Rectangle2D getRectangle() {
        return this.customArea.getBounds();
    }

    public CustomArea getCustomArea() {
        return this.customArea;
    }

    public List getElements() {
        return this.elements;
    }

    /**
     * Align this flat onto a reference flat border.
     * @param referenceFlat , the flat reference on which we align this flat.
     */
    public void alignOn(FlatBorder referenceFlat) {
        List<Vertex> refList = (List)referenceFlat.elements.get(0);
        Vertex refVertex = refList.get(0);

        double minDistance = -1;
        List firstElement = null;
        for (Object o: this.elements) {
            if (o instanceof List) {
                List<Vertex> candidateList = (List)o;
                Vertex candidate = candidateList.get(0);
                if (minDistance == -1 || candidate.distanceTo(refVertex) < minDistance) {
                    minDistance = candidate.distanceTo(refVertex);
                    firstElement = candidateList;
                }
            }
        }
        this.setStartingList(firstElement);
    }

    private void setStartingList(List startingList) {
        while (!this.elements.get(0).equals(startingList)) {
            this.elements.add(this.elements.get(0));
            this.elements.remove(0);
        }
    }

    public Mesh getParentMesh() {
        return parentMesh;
    }

    @Override
    public String toString() {
        int compteur = 0;
        if (this.elements.size() == 1) {
            return "(1 circular)";
        } else {
            for (Object o: this.elements) {
                if (o instanceof List) {
                    compteur++;
                }
            }
            return "("+compteur+" linear)";
        }
    }
}
