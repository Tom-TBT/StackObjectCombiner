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

import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import static java.lang.Double.NaN;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Couple{
    private final FlatBorder flat1;

    private final FlatBorder flat2;

    private double similarity;

    private boolean compatible;

    private final List<Face> newFaces;

    public static double MIN_AFFINITY = 0.8;

    protected Couple(final FlatBorder b1, final FlatBorder b2) {
        flat1 = b1;
        flat2 = b2;
        this.newFaces = new ArrayList();
        this.checkCompatible();
        if (compatible) {
            computeSimilarity();
        } else {
            similarity = 0;
        }
    }

    private void checkCompatible() {
        this.compatible = false;
        if (valueDistance(flat1.getPerimeter(), flat2.getPerimeter()) < 1-MIN_AFFINITY) {
            if (valueDistance(flat1.getSize(), flat2.getSize()) < 1-MIN_AFFINITY) {
                Rectangle2D rect1 = flat1.getRectangle();
                Rectangle2D rect2 = flat2.getRectangle();
                if (valueDistance(rect1.getWidth(), rect2.getWidth()) < 1-MIN_AFFINITY &&
                        valueDistance(rect1.getHeight(), rect2.getHeight()) < 1-MIN_AFFINITY) {
                    Point2D point1 = flat1.getBarycenter();
                    Point2D point2 = flat2.getBarycenter();
                    double factor = (rect1.getWidth()+rect2.getWidth())/(rect1.getHeight()+rect2.getHeight());
                    double distanceX = Math.abs(point1.getX() - point2.getX());
                    double distanceY = Math.abs(point1.getY() - point2.getY());
                    double distance = Math.sqrt(Math.pow(distanceX/factor, 2)+Math.pow(distanceY*factor, 2));
                    this.compatible = distance < (1-MIN_AFFINITY)*Math.sqrt(Math.pow((rect1.getWidth()+rect2.getWidth())/2, 2)+Math.pow((rect1.getHeight()+rect2.getHeight()), 2));
                }
            }
        }
    }

    /**
     * Return the distance of the two value in a %.
     * @param val1
     * @param val2
     * @return
     */
    private double valueDistance(double val1, double val2) {
        return (Math.abs(val1-val2)*2)/(val1+val2);
    }

    private void computeSimilarity() {
        Area intersection = (Area)flat1.getCustomArea().clone();
        intersection.intersect((CustomArea)flat2.getCustomArea());
        PathIterator pI = intersection.getPathIterator(null);
        double size = approxArea(pI);
        double sim1 = size/flat1.getSize();
        double sim2 = size/flat2.getSize();
        similarity = sim1<sim2 ? sim1:sim2;

    }

    public static double approxArea(PathIterator i) {
        double a = 0.0;
        double[] coords = new double[6];
        double startX = NaN, startY = NaN;
        Line2D segment = new Line2D.Double(NaN, NaN, NaN, NaN);
        while (! i.isDone()) {
            int segType = i.currentSegment(coords);
            double x = coords[0], y = coords[1];
            switch (segType) {
            case PathIterator.SEG_CLOSE:
                segment.setLine(segment.getX2(), segment.getY2(), startX, startY);
                a += hexArea(segment);
                startX = startY = NaN;
                segment.setLine(NaN, NaN, NaN, NaN);
                break;
            case PathIterator.SEG_LINETO:
                segment.setLine(segment.getX2(), segment.getY2(), x, y);
                a += hexArea(segment);
                break;
            case PathIterator.SEG_MOVETO:
                startX = x;
                startY = y;
                segment.setLine(NaN, NaN, x, y);
                break;
            default:
                throw new IllegalArgumentException("PathIterator contains curved segments");
            }
            i.next();
        }
        if (Double.isNaN(a)) {
            throw new IllegalArgumentException("PathIterator contains an open path");
        } else {
            return 0.5 * Math.abs(a);
        }
    }
    private static double hexArea(Line2D seg) {
        return seg.getX1() * seg.getY2() - seg.getX2() * seg.getY1();
    }

    public void alignFlats() {
        flat1.alignOn(flat2);
        int g=0;
    }

    public void merge() {
        List elements1 = flat1.getElements();
        List elements2 = flat2.getElements();
        for (int i = 0; i < elements1.size(); i++) {
            if (elements1.get(i) instanceof List && elements2.get(i) instanceof List) {
                List<Vertex> vertexList1 = (List)elements1.get(i);
                List<Vertex> vertexList2 = (List)elements2.get(i);
                boolean isCircular = elements1.size() == 1;
                this.newFaces.addAll(Linker.createFacesBetween(
                        vertexList1, vertexList2, isCircular));
            }
        }
    }

    protected boolean compatible() {
        return this.compatible;
    }

    public boolean contain(Mesh mesh) {
        return (this.flat1.getParentMesh().equals(mesh) ||
                this.flat2.getParentMesh().equals(mesh));
    }

    public boolean contain(FlatBorder flat) {
        return (this.flat1.equals(flat) ||
                this.flat2.equals(flat));
    }

    public Mesh getOther(Mesh mesh) {
        Mesh result = null;
        if (flat1.getParentMesh().equals(mesh)) {
            result = flat2.getParentMesh();
        } else if (flat2.getParentMesh().equals(mesh)) {
            result = flat1.getParentMesh();
        }
        return result;
    }

    public Mesh getMesh1() {
        return flat1.getParentMesh();
    }

    public Mesh getMesh2() {
        return flat2.getParentMesh();
    }

    public FlatBorder getFlat1() {
        return flat1;
    }

    public FlatBorder getFlat2() {
        return flat2;
    }

    public List<Face> getNewFaces() {
        return this.newFaces;
    }

    public double getSimilarity() {
        return this.similarity;
    }

    public List<Vertex> getEndPoints() {
        List<Vertex> result = new ArrayList();
        if (flat1.getElements().size() == 1) {
            // Only one element means it is circular, no need for hole filling.
            return result;
        }
        for (Object obj: this.flat1.getElements()) {
            if (obj instanceof List) {
                List<Vertex> currList = (List<Vertex>) obj;
                result.add(currList.get(0));
                result.add(currList.get(currList.size() - 1));
            }
        }
        for (Object obj: this.flat2.getElements()) {
            if (obj instanceof List) {
                List<Vertex> currList = (List<Vertex>) obj;
                result.add(currList.get(0));
                result.add(currList.get(currList.size() - 1));
            }
        }
        return result;
    }

    public List<Face> getEndFaces() {
        List<Vertex> endPoints = this.getEndPoints();
        List<Face> result = new ArrayList();

        if (!endPoints.isEmpty()) {
            for (Face face: this.newFaces) {
                Vertex v1 = face.getVertex1();
                Vertex v2 = face.getVertex2();
                Vertex v3 = face.getVertex3();

                if (endPoints.contains(v1) && endPoints.contains(v2)) {
                    result.add(face);
                } else if (endPoints.contains(v1) || endPoints.contains(v2)) {
                    if (endPoints.contains(v3)) {
                        result.add(face);
                    }
                }
            }
        }
        return result;
    }
}
