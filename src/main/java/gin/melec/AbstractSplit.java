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

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public abstract class AbstractSplit implements Serializable {

    /**
     * The maximal distance to the split from which a vertex does no longer
     * belong to the border.
     */
    protected static final double WINDOW = 4;

    private static final double TAIL_LIMIT = 2;

    /**
     * The position of the split.
     */
    protected long position;

    /**
     * Find the vertices who belong to the border.
     *
     * @param vertices , the list of the vertex to filter.
     * @return the list of the vertex belonging to the border.
     */
    public final Set findLimitVertices(final Set<Vertex> vertices) {
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
     *
     * @return the position of the border.
     */
    public final long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
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
    protected abstract long xPosition();

    /**
     * Give the position of the split in his y Position.
     *
     * @return the position of the split.
     */
    protected abstract long yPosition();

    /**
     * Give the distance between the split and the vertex depending the axe of
     * the split.
     *
     * @param vertex , the vertex mesured.
     * @return the distance.
     */
    protected abstract float distanceTo(final Vertex vertex);

    /**
     * A method that indicate if the given vertex is close to the split.
     *
     * @param vertex , the vertex to check.
     * @return true if the vertex is close, else false.
     */
    protected abstract boolean isClose(final Vertex vertex);

    /**
     * A method that remove the tails of a border. Only used on linear border.
     * @param currentSubBorder , the border for which the tails are removed.
     */
    protected final void removeTails(final Border currentSubBorder) {
        List<Vertex> sequence = currentSubBorder.getVertexSequence();
        Set trash = new HashSet();
        int i;
        i = 0;
        while (i < sequence.size()) {
            Vertex current = sequence.get(i);
            if (this.distanceTo(current) > TAIL_LIMIT) {
                trash.add(current);
            } else {
                break;
            }
            i++;
        }
        i = sequence.size() - 1;
        while (i >= 0) {
            Vertex current = sequence.get(i);
            if (this.distanceTo(current) > TAIL_LIMIT) {
                trash.add(current);
            } else {
                break;
            }
            i--;
        }
        sequence.removeAll(trash);
    }
}
