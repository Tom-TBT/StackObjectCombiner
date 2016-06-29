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

import java.awt.geom.Line2D;
import java.util.TreeSet;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class DepthSplit extends AbstractSplit{

    public DepthSplit() {
        this.position = 0;
    }

    public DepthSplit(double position) {
        this.position = position;
    }

    public DepthSplit(AbstractSplit original) {
        this.position = original.position;
        this.primers = new TreeSet();
    }

    @Override
    protected final double xPosition() {
        return 0;
    }

    @Override
    protected final double zPosition() {
        return position;
    }

    @Override
    protected final double yPosition() {
        return 0;
    }

    @Override
    protected final boolean isClose(final Vertex vertex) {
        return distanceTo(vertex) < WINDOW;
    }

    @Override
    protected double distanceTo(final Vertex vertex) {
        return Math.abs(this.position - vertex.getZ());
    }

    @Override
    protected Line2D.Float getSegment(Vertex vertex1, Vertex vertex2) {
        return new Line2D.Float(vertex1.getX(), vertex1.getY(), vertex2.getX(),
                vertex2.getY());
    }
}
