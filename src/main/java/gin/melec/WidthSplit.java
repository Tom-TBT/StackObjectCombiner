/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gin.melec;

import java.awt.geom.Line2D;
import java.util.TreeSet;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class WidthSplit extends AbstractSplit{

    public WidthSplit() {
        this.position = 0;
    }

    public WidthSplit(double position) {
        this.position = position;
    }

    public WidthSplit(AbstractSplit original) {
        this.position = original.position;
        this.primers = new TreeSet();
    }

    @Override
    protected final double xPosition() {
        return position;
    }

    @Override
    protected final double yPosition() {
        return 0;
    }

    @Override
    protected final double zPosition() {
        return 0;
    }

    @Override
    protected final boolean isClose(final Vertex vertex) {
        return distanceTo(vertex) < WINDOW;
    }

    @Override
    protected double distanceTo(final Vertex vertex) {
        return Math.abs(this.position - vertex.getX());
    }

    @Override
    protected Line2D.Float getSegment(Vertex vertex1, Vertex vertex2) {
        return new Line2D.Float(vertex1.getY(), vertex1.getZ(), vertex2.getY(),
                vertex2.getZ());
    }
}
