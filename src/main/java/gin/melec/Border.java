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

import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Border {

    protected static int TAIL_SIZE = 4;

    /**
     * The split that initiate this border.
     */
    private AbstractSplit split;

    /**
     * The sequence of vertex forming the border.
     */
    private LinkedList<Vertex> vertexSequence;

    /**
     * The real lenght of the border.
     */
    private double cumulLenght;

    /**
     * The center of the border.
     */
    private Vertex center;

    /**
     * The center of the border.
     */
    private boolean circular;

    private double area;

    private Connector connector;


    /**
     * Public constructor for the border. This constructor is used when the
     * border need to be detected. The constructor look at the primers of the
     * given mesh to build the new border.
     *
     * @param mesh , the mesh from which a new border is created.
     * @param split , the split used to initiate the border.
     */
    public Border(final Mesh mesh, final AbstractSplit split) {
        this.vertexSequence = new LinkedList();
        this.split = split;

        Vertex firstVertex = null, secondVertex = null;
        firstVertex = split.findCloserVertex();
        if (firstVertex == null) {
            return;
        }
        CustomFrame.appendToLog("Detecting new border for " + mesh.getFile().getName());
        vertexSequence.add(firstVertex);
        for (final Vertex vertex : firstVertex.getNeighbours()) {
            if (vertex.belongToBorder() && firstVertex.isLogicalNext(vertex)) {
                secondVertex = vertex;
                break;
            }
        }
        vertexSequence.add(secondVertex);
        mesh.getGarbage().add(getSecondLastVertex());
        mesh.getGarbage().add(getLastVertex());
    }

    private Border() {
        this.vertexSequence = new LinkedList();
    }

    protected Border(List<Vertex> vertexList, int startSequence, int endSequence, Edge stopEdge) {
        this.vertexSequence = new LinkedList();
        this.connector = new Connector(vertexList.get(startSequence), this, stopEdge);
        int i = startSequence;
        while ( i < endSequence) {
            this.vertexSequence.add(vertexList.get(i));
            i++;
        }
    }

    /**
     * Getter for the split of the border.
     *
     * @return the split of the border.
     */
    public final AbstractSplit getSplit() {
        return split;
    }

    /**
     * Getter for the vertex sequence of the border.
     *
     * @return the vertex sequence of the border
     */
    public final List<Vertex> getVertexSequence() {
        return vertexSequence;
    }

    /**
     * Add the given vertex to the end of the border.
     *
     * @param vertex , the vertex to add.
     */
    public final void addNextVertex(final Vertex vertex) {
        if (vertex != null) {
            this.vertexSequence.add(vertex);
        }
    }

    /**
     * Getter for the first vertex of the border's vertex sequence.
     *
     * @return the first vertex of this border.
     */
    public final Vertex getFirstVertex() {
        Vertex result;
        if (vertexSequence.isEmpty()) {
            result = null;
        } else {
            result = vertexSequence.getFirst();
        }
        return result;
    }

    /**
     * Getter for the last vertex of the border's vertex sequence.
     *
     * @return the last vertex of this border.
     */
    public final Vertex getLastVertex() {
        Vertex result;
        if (vertexSequence.isEmpty()) {
            result = null;
        } else {
            result = vertexSequence.getLast();
        }
        return result;
    }

    /**
     * Getter for the second last vertex of the border's vertex sequence.
     *
     * @return the second last vertex of this border.
     */
    public final Vertex getSecondLastVertex() {
        Vertex result;
        if (vertexSequence.isEmpty()) {
            result = null;
        } else {
            result = vertexSequence.get(vertexSequence.size() - 2);
        }
        return result;
    }

    void setSplit(AbstractSplit split) {
        this.split = split;
    }

    /**
     * Divide the border into sub-borders. A border can be decompose from 1 to
     * many subborders, in case of a multiple crossing of the split.
     *
     * @return a list of the separated borders.
     */
    public final List separateSubBorders() {
        boolean isCircular = true;
        List result = new ArrayList();
        // Moving the first vertex to an outside vertex if it exist.
        for (Vertex vertex : this.vertexSequence) {
            if (!this.split.isClose(vertex)) {
                this.changeFirstVertex(vertex);
                isCircular = false;
                //break;
            }
        }
        if (isCircular) { // the border is circular so no need to split it
            this.prepare();
            result.add(this);
        }
        else {
            Border currentSubBorder = null;
            for (Vertex vertex : this.vertexSequence) {
                if (currentSubBorder == null && this.split.isClose(vertex)) {
                    currentSubBorder = new Border();
                    currentSubBorder.addNextVertex(vertex);
                } else if (currentSubBorder != null) {
                    if (this.split.isClose(vertex)) {
                        currentSubBorder.addNextVertex(vertex);
                    } else {
                        currentSubBorder.removeTails();
                        if (currentSubBorder.vertexSequence.size() > 0) {
                            // if the tails removing remove everything, we don't keep it
                            currentSubBorder.setSplit(this.split);
                            currentSubBorder.prepare();
                            result.add(currentSubBorder);
                        }
                        currentSubBorder = null;
                    }
                }
            }
            if (currentSubBorder != null) {
                if (!isCircular) {
                    currentSubBorder.removeTails();
                }
                currentSubBorder.setSplit(this.split);
                currentSubBorder.prepare();
                result.add(currentSubBorder);
            }
        }

        return result;
    }

    /**
     * This method prepare the attributes of the border. This set up for example
     * the lenght, or the position of the center of the border. These parameters
     * are used for automatic matching of the borders.
     */
    private void prepare() {
        cumulLenght = 0;
        float x = 0, y = 0, z = 0;
        for (int i = 0; i < vertexSequence.size() - 1; i++) {
            cumulLenght += vertexSequence.get(i)
                    .distanceTo(vertexSequence.get(i + 1));
            x += vertexSequence.get(i).getX();
            y += vertexSequence.get(i).getY();
            z += vertexSequence.get(i).getZ();
        }
        x += vertexSequence.getLast().getX();
        y += vertexSequence.getLast().getY();
        z += vertexSequence.getLast().getZ();
        x = x / vertexSequence.size();
        y = y / vertexSequence.size();
        z = z / vertexSequence.size();
        this.center = new Vertex(0, x, y, z);

        this.circular = this.getFirstVertex().getNeighbours()
                .contains(this.getLastVertex());

//        if (this.computeArea()) {
//
//        }
        if (this.isCircular() && this.isClockwise()) {
            this.revertSequence();
        }
    }

    private boolean computeArea() {
        this.area = 0;
        Vertex v1 = this.getVertexSequence().get(0);
        Vertex v2;
        for (int i = 1; i < this.getVertexSequence().size() - 1; i++) {
            v2 = this.getVertexSequence().get(i);
            Line2D.Float segment = this.split.getSegment(v1,v2);
            this.area += (segment.getX1() + segment.getX2()) *
                    (segment.getY1() - segment.getY2());
            v1 = v2;
        }
        v2 = this.getVertexSequence().get(0);
        Line2D.Float segment = this.split.getSegment(v1,v2);
        this.area += (segment.getX1() + segment.getX2()) *
                (segment.getY1() - segment.getY2());
        if (area < 0) {
            area = (-area);
            return false;
        }
        return true;
    }

    /**
     * Compute the distance between two borders. The distance depend of the
     * lenght of the two borders, the distance between their first and last
     * vertex if the border is not circular, and it finally depend of the
     * distance between their centers.
     *
     * @param border , the border to compare to this one.
     * @return the distance between the two borders.
     */
    protected final double distanceTo(final Border border) {
        double result = 0;

        result += Math.abs(this.cumulLenght - border.cumulLenght);
        result += this.center.distanceTo(border.center);
        if (!this.circular && !border.circular) {
            // Compute the distance between the first plus the last vertex of
            // the two borders.
            result += Math.min(this.getFirstVertex().
                    distanceTo(border.getFirstVertex())
                    + this.getLastVertex()
                    .distanceTo(border.getLastVertex()), // And
                    this.getFirstVertex()
                    .distanceTo(border.getLastVertex())
                    + this.getLastVertex()
                    .distanceTo(border.getFirstVertex()));
        }
        return result;
    }

    /**
     * Change the first vertex of the border's vertex sequence. Used when
     * borders to join need to be aligned (in term of vertex sequence). Used
     * only on circular borders.
     */
    private void changeFirstVertex(final Vertex vertex) {
        final LinkedList endSequence = new LinkedList<Vertex>();
        final LinkedList newStartSequence = new LinkedList<Vertex>();

        Iterator<Vertex> it = this.vertexSequence.iterator();
        while (it.hasNext()) {
            final Vertex currentVertex = it.next();
            if (currentVertex.equals(vertex)) {
                newStartSequence.add(currentVertex);
                break;
            } else {
                endSequence.add(currentVertex);
            }
        }
        while (it.hasNext()) {
            newStartSequence.add(it.next());
        }
        newStartSequence.addAll(endSequence);

        this.vertexSequence = newStartSequence;
    }

    /**
     * Revert the sequence of the border. Used when borders to join need to be
     * aligned (in term of vertex sequence).
     */
    public final void revertSequence() {
        final LinkedList newSequence = new LinkedList<Vertex>();
        for (Iterator it = this.vertexSequence.descendingIterator();
                it.hasNext();) {
            newSequence.add(it.next());
        }
        this.vertexSequence = newSequence;
        if (this.isCircular()) {
            // The previous first vertex need to be put back in 1st
            this.vertexSequence.add(this.vertexSequence.removeFirst());
        }
    }

    public final void addStartingSequence(final List<Vertex> firstFragment) {
        for (int i = 0; i < firstFragment.size(); i++) {
            this.vertexSequence.add(i, firstFragment.get(i));
        }
    }

    /**
     * This method take a border as reference, and align the border on. Can then
     * change the first vertex of the border, and the order of the sequence. If
     * the borders are circular, the first vertex of the border given is used as
     * a reference and the first vertex of the sequence of this border is
     * choosed as the closer vertex to the reference. If the borders are linear,
     * it just check if this border need to be inverted.
     *
     * @param border , the border on which this border is aligned.
     */
    public final void alignOn(Border border) {
        Vertex firstVertexThis = this.getFirstVertex();
        final Vertex firstVertexRef = border.getFirstVertex();

        if (this.isCircular() && border.isCircular()) {
            for (Vertex candidate : this.vertexSequence) {
                if (candidate.distanceTo(firstVertexRef)
                        < firstVertexThis.distanceTo(firstVertexRef)) {
                    firstVertexThis = candidate;
                }
            }
            this.changeFirstVertex(firstVertexThis);
//            border.isClockwise();
        } else if (!this.isCircular() && !border.isCircular()) {
            if (this.getFirstVertex().distanceTo(border.getFirstVertex())
                    > this.getLastVertex().distanceTo(border.getFirstVertex())) {
                this.revertSequence();
            }
        } else {
            // TODO Exception, if two borders are linear and circular.
        }

    }

    private boolean isClockwise() {
//        List<Vector2D> twodPoints = new ArrayList<Vector2D>();
//
//        int i = 0, increment = 10;
        boolean isHorizontalPlane = this.getSplit() instanceof HeightSplit;

        // Creating the shape
        Path2D shape = new Path2D.Double();
        if (isHorizontalPlane) {
            shape.moveTo(this.getFirstVertex().getX(),
                    this.getFirstVertex().getZ());
        } else {
            shape.moveTo(this.getFirstVertex().getY(),
                    this.getFirstVertex().getZ());
        }
        int i = 1;
        while (i < vertexSequence.size()) {
            if (isHorizontalPlane) {
                shape.lineTo(vertexSequence.get(i).getX(),
                        vertexSequence.get(i).getZ());
            } else {
                shape.lineTo(vertexSequence.get(i).getY(),
                        vertexSequence.get(i).getZ());
            }
            i++;
        }
        shape.closePath();
        // Shape created

        PathIterator shapePath = shape.getPathIterator(null);
        Area area = new Area(shape);
        PathIterator areaPath = area.getPathIterator(null);
        // The pathIterator of a created area is always read reverse-clockwise

        double shapeCoord[] = new double[6];
        double areaCoord[] = new double[6];
        shapePath.currentSegment(shapeCoord);
        int index = 0;
        boolean result = false;
        while (!areaPath.isDone() && index < vertexSequence.size()) {
            areaPath.currentSegment(areaCoord);
            if (areaCoord[0] == shapeCoord[0] && areaCoord[1] == shapeCoord[1]) {
                shapePath.next();
                shapePath.currentSegment(shapeCoord);
                if (index == vertexSequence.size() - 1) {
                    // If it is the last point, we start over
                    areaPath = area.getPathIterator(null);
                    areaPath.currentSegment(areaCoord);
                } else {
                    areaPath.next();
                    areaPath.currentSegment(areaCoord);
                }
                // test if the two iterator iterate in the same direction
                result = areaCoord[0] == shapeCoord[0]
                        && areaCoord[1] == shapeCoord[1];
                break;
            }
            index++;
            areaPath.next();
        }
        return !result;
    }

    /**
     * A method that remove the tails of a border. Only used on linear border.
     * @param currentSubBorder , the border for which the tails are removed.
     */
    protected final void removeTails() {
        List<Vertex> sequence = getVertexSequence();
        Set trash = new HashSet();
        int i, j;
        i = 0;
        j = TAIL_SIZE;
        while (j > 0) {
            trash.add(sequence.get(i));
            i++;
            j--;
        }
        i = sequence.size() - 1;
        j = TAIL_SIZE;
        while (j > 0) {
            trash.add(sequence.get(i));
            i--;
            j--;
        }
        sequence.removeAll(trash);
    }

    /**
     * Method that indicate if the border is circular or not.
     *
     * @return true if the border is circular, else false (and the border is
     * linear).
     */
    public final boolean isCircular() {
        return circular;
    }

    protected final double getCumulLenght() {
        return this.cumulLenght;
    }

    public Vertex getCenter() {
        return center;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }
}
