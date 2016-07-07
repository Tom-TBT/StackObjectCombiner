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

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public abstract class AbstractSplit{

    /**
     * The maximal distance to the split from which a vertex does no longer
     * belong to the border.
     */
    protected static double WINDOW = 4;

    /**
     * The position of the split.
     */
    protected double position;

    protected Set<Vertex> primers;

    private Edge upEdge, rightEdge, downEdge, leftEdge;

    private List<FlatBorder> flatBorders;

    public AbstractSplit() {
        this.position = 0;
        this.primers = new TreeSet();
    }

    public AbstractSplit(double position) {
        this.position = position;
         this.primers = new TreeSet();
    }

    public AbstractSplit(AbstractSplit original) {
        this.position = original.position;
        this.primers = new TreeSet();
    }

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
        this.primers.addAll(closeVertices);
        return closeVertices;
    }

    /**
     * Getter for the attribute position.
     *
     * @return the position of the border.
     */
    public final double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }


    /**
     * A protected method called to search in a collection the closer vertex to
     * the split.
     *
     * @param collection , the collection containing the vertices.
     * @return the distance of the closest vertex to the split.
     */
    protected final Vertex findCloserVertex() {
        Vertex result = null;
        while (result == null && primers.size() > 0) {
            for (Vertex candidat : primers) {
                if (result == null
                        || this.distanceTo(candidat) < this.distanceTo(result)) {
                    result = candidat;
                }
            }
            if (!result.belongToBorder()) {
                primers.remove(result);
                result = null;
            }
        }

        return result;
    }

    protected void removeAlreadyLookedPrimers(final Set primersLeft) {
        Set tmpSet = new HashSet();
        for (Vertex v:this.primers) {
            if (!primersLeft.contains(v)) {
                tmpSet.add(v);
            }
        }
        this.primers.removeAll(tmpSet);
    }

    protected final void clearPrimers(final Set garbage) {
        this.primers.removeAll(garbage);
    }

    protected final void storeFlatBorders(){
        List<FlatBorder> result = new ArrayList();
        boolean connectorFound = true;
        Connector firstConn;
        do {
            Edge currentEdge;
            firstConn = this.downEdge.getFirst();
            if (firstConn == null) {
                firstConn = this.leftEdge.getFirst();
                if (firstConn == null) {
                    firstConn = this.upEdge.getFirst();
                    if (firstConn == null) {
                        firstConn = this.rightEdge.getFirst();
                        if (firstConn == null) {
                            break;
                        }
                    }
                }
            }
            FlatBorder currentFlat = new FlatBorder();
            currentFlat.addElement(firstConn.getSequence());
            Vertex currVertex = firstConn.getLastVertex();
            currentEdge = firstConn.getNextEdge();
            do {
                currVertex = currentEdge.getNext(currVertex);
                if (currVertex == firstConn) {
                    result.add(currentFlat);
                    break;
                }
                if (currVertex instanceof Connector) {
                    Connector connector = (Connector) currVertex;
                    currentEdge.removeConnector(connector);
                    currentEdge = connector.getNextEdge();
                    currentFlat.addElement(connector.getSequence());
                    currVertex = connector.getLastVertex();
                } else {
                    currentEdge = getNextEdge(currentEdge);
                    currentFlat.addElement(currVertex);
                }

            } while(true);
            currentEdge.removeConnector(firstConn);
        } while(connectorFound);
        this.flatBorders = result;
    }

    public List<FlatBorder> getFlatBorders() {
        return this.flatBorders;
    }

    public void addFlatBorder(FlatBorder flat) {
        if (this.flatBorders == null) {
            this.flatBorders = new ArrayList();
        }
        this.flatBorders.add(flat);
    }

    public void clearFlatBorders() {
        this.flatBorders = null;
    }

    private Edge getNextEdge(Edge edge) {
        Edge result;
        if (edge == upEdge) {
            result = leftEdge;
        } else if (edge == leftEdge) {
            result = downEdge;
        } else if (edge == downEdge) {
            result = rightEdge;
        } else {
            result = upEdge;
        }
        return result;
    }

    /**
     * Give the position of the split in his x Position.
     *
     * @return the position of the split.
     */
    protected abstract double xPosition();

    /**
     * Give the position of the split in his y Position.
     *
     * @return the position of the split.
     */
    protected abstract double yPosition();

    /**
     * Give the position of the split in his y Position.
     *
     * @return the position of the split.
     */
    protected abstract double zPosition();

    /**
     * Give the distance between the split and the vertex depending the axe of
     * the split.
     *
     * @param vertex , the vertex mesured.
     * @return the distance.
     */
    protected abstract double distanceTo(final Vertex vertex);

    /**
     * A method that indicate if the given vertex is close to the split.
     *
     * @param vertex , the vertex to check.
     * @return true if the vertex is close, else false.
     */
    protected abstract boolean isClose(final Vertex vertex);

    protected abstract Vector2D getVector(final Vertex vertex);

    public Edge getUpEdge() {
        return upEdge;
    }

    public void setUpEdge(Edge upEdge) {
        this.upEdge = upEdge;
    }

    public Edge getRightEdge() {
        return rightEdge;
    }

    public void setRightEdge(Edge rightEdge) {
        this.rightEdge = rightEdge;
    }

    public Edge getDownEdge() {
        return downEdge;
    }

    public void setDownEdge(Edge downEdge) {
        this.downEdge = downEdge;
    }

    public Edge getLeftEdge() {
        return leftEdge;
    }

    public void setLeftEdge(Edge leftEdge) {
        this.leftEdge = leftEdge;
    }

}
