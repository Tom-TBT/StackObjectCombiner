/*  Inspired from
 *  http://paulbourke.net/geometry/polygonmesh/PolygonUtilities.java
 */
package gin.melec;

import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 *
 * @author tom
 */
public class CustomArea extends Area{

    Point2D barycentre;

    private double size;

    double perimeter;

    public CustomArea(List<Vector2D> vectors) {
        super();
        this.barycentre = null;
        this.size = 0;
        this.perimeter = 0;
        this.addArea(vectors);
        this.computeArea(vectors);
        this.computeBarycentre(vectors);
        this.computePerimeter(vectors);
    }

    private void addArea(List<Vector2D> vectors) {
        Path2D path = new Path2D.Double(Path2D.WIND_NON_ZERO);
        boolean firstPoint = true;

        for(Vector2D vector: vectors) {
            if (firstPoint) {
                firstPoint = false;
                path.moveTo(vector.getX(), vector.getY());
            } else {
                path.lineTo(vector.getX(), vector.getY());
            }
        }
        this.add(new Area(path));
    }

    private void computeArea(List<Vector2D> vectors) {
        int i, j, n = vectors.size();
        for (i = 0; i < n; i++) {
                j = (i + 1) % n;
                this.size += vectors.get(i).getX() * vectors.get(j).getY();
                this.size -= vectors.get(j).getX() * vectors.get(i).getY();
        }
        this.size /= 2.0;
    }

    private void computeBarycentre(List<Vector2D> vectors) {
        double cx = 0, cy = 0;
        double size = this.size;
        // could change this to Point2D.Float if you want to use less memory
        Point2D res = new Point2D.Double();
        int i, j, n = vectors.size();

        double factor = 0;
        for (i = 0; i < n; i++) {
            j = (i + 1) % n;
            factor = (vectors.get(i).getX() * vectors.get(j).getY()
                            - vectors.get(j).getX() * vectors.get(i).getY());
            cx += (vectors.get(i).getX() + vectors.get(j).getX()) * factor;
            cy += (vectors.get(i).getY() + vectors.get(j).getY()) * factor;
        }
        size *= 6.0f;
        factor = 1 / size;
        cx *= factor;
        cy *= factor;
        res.setLocation(cx, cy);
        this.barycentre = res;
    }

    private void computePerimeter(List<Vector2D> vectors) {
        int i, j, n = vectors.size();
        for (i = 0; i < n; i++) {
            j = (i + 1) % n;
            this.perimeter += Math.sqrt(Math.pow(vectors.get(i).getX()- vectors.get(j).getX(),2) +
                    Math.pow(vectors.get(i).getY()-vectors.get(j).getY(),2));
        }
    }

    public double getSize() {
        return Math.abs(this.size);
    }
}
