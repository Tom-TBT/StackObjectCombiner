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

    private final List elements;

    private CustomArea customArea;

    public FlatBorder() {
        this.elements = new ArrayList();
        this.customArea = null;
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

    public void printBorder() {
        System.out.println();
        for (Object obj: this.elements) {
            if (obj instanceof List) {
                List<Vertex> list = (List) obj;
                for (Vertex vertex: list) {
                    System.out.println(vertex);
                }
            } else {
                System.out.println(obj);
            }
        }
    }

    private List<Vertex> getSingleSequence() {
        List<Vertex> result = new ArrayList();
        for (Object o : this.elements) {
            if (o instanceof Vertex) {
                result.add((Vertex)o);
            } else {
                List<Vertex> tmpList = (List)o;
                if (tmpList.size() < 20) {
                    //if the set is too small, no need to reduce it
                    result.addAll(tmpList);
                } else {
                    int i;
                    result.add(tmpList.get(0));
                    for(i=1; i < tmpList.size()-10; i=i+1){ // TODO Parametrize precision
                        result.add(tmpList.get(i));
                    }
//                    if (i != tmpList.size() + 4) { //
//                        result.add(tmpList.get(tmpList.size()-1));
//                    }
                }

            }
        }
        return result;
    }

    private List<Vector2D> getVectors(List<Vertex> seq, AbstractSplit split) {
        List<Vector2D> result = new ArrayList();
        for (Vertex v: seq) {
            result.add(split.getVector(v));
        }
        return result;
    }

    private List<Vector2D> revertVectors(List<Vector2D> vectors) {
        List<Vector2D> result = new ArrayList();
        for(int i = vectors.size()-1; i >=0; i--) {
            result.add(vectors.get(i));
        }
        return result;
    }

    public void computeProperties(final AbstractSplit split) {
        List<Vertex> singleSequence = getSingleSequence();
        List<Vector2D> vectors = getVectors(singleSequence, split);
        this.customArea = new CustomArea(vectors);
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
}
