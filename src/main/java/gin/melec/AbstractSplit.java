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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public abstract class AbstractSplit {

    /**
     * The maximal distance to the split from which a vertex does no longer
     * belong to the border.
     */
    protected static final int WINDOW = 2;

    /**
     * The position of the split.
     */
    protected int position;

    /**
     * Public constructor for a split with a position.
     *
     * @param position , the position of the border.
     */
    public AbstractSplit(final int position) {
        this.position = position;
    }

    /**
     * Find the vertices who belong to the border.
     *
     * @param vertices , the list of the vertex to filter.
     * @return the list of the vertex belonging to the border.
     */
    public final Set findBorderVertices(final Set<Vertex> vertices) {
        final TreeSet<Vertex> closeVertices = new TreeSet();
        for (Vertex vertex : vertices) {
            if (this.isClose(vertex)) {
                closeVertices.add(vertex);
            }
        }
        return closeVertices;
    }

    /**
     * Getter for the attribute position.
     * @return the position of the border.
     */
    public final int getPosition() {
        return position;
    }

    /**
     * A protected method called to search in a collection the closer vertex to
     * the split.
     *
     * @param collection , the collection containing the vertices.
     * @return the distance of the closest vertex to the split.
     */
    protected final Vertex findCloserVertex(final Set<Vertex> collection) {
        Vertex result = null;
        for (Vertex candidat : collection) {
            if (result == null
                    || this.distanceTo(candidat) < this.distanceTo(result)) {
                result = candidat;
            }
        }
        return result;
    }

    /**
     * Give the position of the split in his x Position.
     *
     * @return the position of the split.
     */
    protected abstract int xPosition();

    /**
     * Give the position of the split in his y Position.
     *
     * @return the position of the split.
     */
    protected abstract int yPosition();

    /**
     * Give the distance between the split and the vertex depending the axe of
     * the split.
     *
     * @param vertex , the vertex mesured.
     * @return the distance.
     */
    protected abstract float distanceTo(final Vertex vertex);

    /**
     * Take the borders, remove the vertex that are not close to the split, and
     * create new borders.
     * @param borders , the borders to refine.
     * @return refined borders.
     */
    protected final Set refineBorders(final Set<Border> borders) {
        final Set result = new HashSet();
        for (Border border : borders) {
            final List<Vertex> sortedBorder = new ArrayList();
            // Creation of a new list starting with an outside vertex.
            for (final Iterator<Vertex> it =
                    border.getVertexSequence().iterator(); it.hasNext();) {
                Vertex vertex = it.next();
                if (this.isClose(vertex)) {
                    sortedBorder.add(vertex);
                } else {
                    int i = 0;
                    sortedBorder.add(i, vertex);
                    while (it.hasNext()) {
                        i++;
                        vertex = it.next();
                        sortedBorder.add(i, vertex);
                    }
                }
            }
            Border currentBorder = null;
            for (Vertex vertex : sortedBorder) {
                if (currentBorder == null && this.isClose(vertex)) {
                    currentBorder = new Border();
                    currentBorder.addNextVertex(vertex);
                } else if (currentBorder != null) {
                    if (this.isClose(vertex)) {
                        currentBorder.addNextVertex(vertex);
                    } else {
                        currentBorder.prepare();
                        result.add(currentBorder);
                        currentBorder = null;
                    }
                }
            }
            if (currentBorder != null) {
                currentBorder.prepare();
                result.add(currentBorder);
            }
        }
        return result;
    }

    /**
     * A method that indicate if the given vertex is close to the split.
     * @param vertex , the vertex to check.
     * @return true if the vertex is close, else false.
     */
    protected abstract boolean isClose(final Vertex vertex);
}
