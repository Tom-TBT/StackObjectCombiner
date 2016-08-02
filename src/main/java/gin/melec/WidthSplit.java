/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gin.melec;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
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
    protected Vector2D getVector(Vertex vertex) {
        return new Vector2D(vertex.getY(), vertex.getZ());
    }
}
