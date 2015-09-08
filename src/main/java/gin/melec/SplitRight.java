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

import static gin.melec.AbstractSplit.WINDOW;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class SplitRight extends AbstractSplit {


    /**
     * Public constructor for a split with a position.
     * @param position , the position of the border.
     */
    public SplitRight(final int position) {
        super(position);
    }

    @Override
    public final List findBorderVertices(final List vertices) {
final List closeVertices = new ArrayList();
        for (final Iterator it = vertices.iterator(); it.hasNext();) {
            final Vertex vertex = (Vertex) it.next();
            if (vertex.x > this.position - WINDOW) {
                closeVertices.add(vertex);
            }
        }
        return closeVertices;
    }

    @Override
    protected final Vertex findCloserVertex(final Collection collection) {
        Vertex result = null;
        for (final Iterator it = collection.iterator(); it.hasNext();) {
            final Vertex candidat = (Vertex) it.next();
            if (result == null || candidat.x > result.x) {
                result = candidat;
            }
        }
        return result;
    }

    @Override
    protected float distanceTo(final Vertex vertex) {
        return Math.abs(this.position - vertex.x);
    }

    @Override
    protected final int xPosition() {
        return position;
    }

    @Override
    protected final int yPosition() {
        return 0;
    }

}
