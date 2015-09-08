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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class SplitUp extends AbstractSplit {

    /**
     * Public constructor for a split with a position.
     * @param position , the position of the border.
     */
    public SplitUp(final int position) {
        super(position);
    }

    @Override
    public final List findBorderVertices(final List vertices) {
        final List closeVertices = new ArrayList();
        for (final Iterator it = vertices.iterator(); it.hasNext();) {
            final Vertex vertex = (Vertex) it.next();
            if (vertex.y < this.position + WINDOW) {
                closeVertices.add(vertex);
            }
        }
        return closeVertices;
    }

    @Override
    public final Vertex createVirtual(final Vertex original) {
        return new Vertex(0, original.x, original.y - 1, original.z);
    }

    @Override
    protected final Vertex findCloserVertex(final Collection collection) {
        Vertex chosenOne = null;
        for (final Iterator it = collection.iterator(); it.hasNext();) {
            final Vertex vertex = (Vertex) it.next();
            if (chosenOne == null || vertex.y < chosenOne.y) {
                chosenOne = vertex;
            }
        }
        return chosenOne;
    }

    @Override
    protected final int xPosition() {
        return 0;
    }

    @Override
    protected final int yPosition() {
        return position;
    }

}
