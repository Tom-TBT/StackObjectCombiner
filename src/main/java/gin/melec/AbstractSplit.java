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
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * Represent the plane passing between two parts of a 3D image.
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public abstract class AbstractSplit {

    /**
     * The maximal distance to the split from which a vertex does no longer
     * belong to the border.
     */
    protected static double WINDOW = 4;

    /**
     * The position of the split.
     */
    protected double position;

    private Edge upEdge, rightEdge, downEdge, leftEdge;

    private List<FlatBorder> flatBorders;

    public AbstractSplit() {
        this.position = 0;
    }

    public AbstractSplit(double position) {
        this.position = position;
    }

    public AbstractSplit(AbstractSplit original) {
        this.position = original.position;
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
     * Create the flat borders from the borders located inside the mesh.
     * @param mesh , the mesh for which we create the flatBorders.
     */
    protected final void storeFlatBorders(Mesh mesh){
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
            FlatBorder currentFlat = new FlatBorder(mesh);
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
        if (this.flatBorders == null) {
            this.flatBorders = new ArrayList();
        }
        this.flatBorders.addAll(result);
    }

    public List<FlatBorder> getFlatBorders() {
        return this.flatBorders;
    }

    /**
     * Add a flat border to the list of this split.
     * It's a temporary list used to transfert then to the concerned mesh.
     * @param flat, the flat to add to the list.
     */
    public void addFlatBorder(FlatBorder flat) {
        if (this.flatBorders == null) {
            this.flatBorders = new ArrayList();
        }
        this.flatBorders.add(flat);
    }

    /**
     * Clear the temporary flat border list.
     */
    public void clearFlatBorders() {
        this.flatBorders = null;
    }

    /**
     * Give the edge that come next to the edge given in parameter. The edges are
     * given in an anticlockwise clockwise manner.
     * @param edge, the preceding edge.
     * @return edge following the edgegiven in parameter.
     */
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

    /**
     * Give the 2D vector, by removing the unnecessary dimension of the vertex.
     * @param vertex, the vertex from which we create the 2D vector.
     * @return a 2D representation of the vertex.
     */
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

    /**
     * From the list of primers, retrieve a vertex that is close to this split.
     * @param primers, the primers of the mesh.
     * @return a vertex close to this split.
     */
    public Vertex findStarter(List<Vertex> primers) {
        Vertex result = null;
        for (Vertex v: primers) {
            if(this.isClose(v)) {
                result = v;
                break;
            }
        }
        return result;
    }
}
