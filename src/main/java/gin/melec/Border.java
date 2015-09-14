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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Border {

    /**
     * The split that initiate this border.
     */
    private AbstractSplit split;

    /**
     * The sequence of vertex forming the border.
     */
    private LinkedList<Vertex> vertexSequence;

    /**
     * The distance between the first and the last vertex of the border.
     */
    private double straightLenght;

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
    private boolean isCircular;

    /**
     * Public constructor for the border.
     */
    public Border(final Mesh mesh) {
        this.vertexSequence = new LinkedList();

        Vertex currentVertex = null;
        Vertex candidat = null;
        float distanceTmp, distanceFirstVertex = 0;
        for (AbstractSplit currentSplit : mesh.getSplits()) {
            currentVertex = currentSplit.findCloserVertex(mesh.getPrimers());
            distanceTmp = currentSplit.distanceTo(currentVertex);
            if (getFirstVertex() == null || distanceTmp < distanceFirstVertex) {
                distanceFirstVertex = distanceTmp;
                candidat = currentVertex;
                this.split = currentSplit;
            }
        }
        vertexSequence.add(candidat);

        mesh.setFacesToVertex(getFirstVertex());
        vertexSequence.add(this.split.findCloserVertex(
                getFirstVertex().getNeighbours()));

        mesh.getGarbage().add(getSecondLastVertex());
        mesh.getGarbage().add(getLastVertex());
    }

    public Border() {
        this.vertexSequence = new LinkedList();
    }

    public AbstractSplit getSplit() {
        return split;
    }

    public List<Vertex> getVertexSequence() {
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

    public final void prepare() {
        straightLenght = getFirstVertex().distanceTo(getLastVertex());

        cumulLenght = 0;
        float x = 0, y = 0, z = 0;
        for (int i = 0; i < vertexSequence.size() - 1; i++) {
            cumulLenght += vertexSequence.get(i)
                    .distanceTo(vertexSequence.get(i + 1));
            x += vertexSequence.get(i).getX();
            y += vertexSequence.get(i).getY();
            z += vertexSequence.get(i).getZ();
        }
        x = x / vertexSequence.size();
        y = y / vertexSequence.size();
        z = z / vertexSequence.size();
        this.center = new Vertex(0, x, y, z);
    }

    public final Vertex getFirstVertex() {
        final Vertex result;
        if (vertexSequence.isEmpty()) {
            result = null;
        } else {
            result = vertexSequence.getFirst();
        }
        return result;
    }

    public final Vertex getLastVertex() {
        final Vertex result;
        if (vertexSequence.isEmpty()) {
            result = null;
        } else {
            result = vertexSequence.getLast();
        }
        return result;
    }

    public final Vertex getSecondLastVertex() {
        final Vertex result;
        if (vertexSequence.isEmpty()) {
            result = null;
        } else {
            result = vertexSequence.get(vertexSequence.size() - 2);
        }
        return result;
    }

    public final void revertSequence() {
        final LinkedList newSequence = new LinkedList<Vertex>();
        for (Iterator it = this.vertexSequence.descendingIterator();
                it.hasNext();) {
            newSequence.add(it.next());
        }
        this.vertexSequence = newSequence;
    }

    public final void changeFirstVertex(final Vertex vertex) {
        final LinkedList endSequence = new LinkedList<Vertex>();
        final LinkedList newStartSequence = new LinkedList<Vertex>();

        Iterator<Vertex> it = this.vertexSequence.iterator();
        while (it.hasNext()) {
            Vertex currentVertex = it.next();
            if (currentVertex == vertex) {
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

    public final void alignOn(Border border) {
        final Vertex firstVertex2 = border.getFirstVertex();
        Vertex firstVertex1 = this.getFirstVertex();
        Vertex previousVertex1, nextVertex1, nextVertex2;

        //if (this.isCircular) {
        for (Iterator<Vertex> it = this.vertexSequence.iterator();
                it.hasNext();) {
            Vertex candidate = it.next();
            if (candidate.distanceTo(firstVertex2)
                    < firstVertex1.distanceTo(firstVertex2)) {
                firstVertex1 = candidate;
            }
        }
        if (firstVertex1 != this.getFirstVertex()) {
            previousVertex1 = this.vertexSequence.get(this.vertexSequence.indexOf(firstVertex1) - 1);
        } else {
            previousVertex1 = this.vertexSequence.getLast();
        }
        if (firstVertex1 != this.vertexSequence.getLast()) {
            nextVertex1 = this.vertexSequence.get(this.vertexSequence.indexOf(firstVertex1) + 1);
        } else {
            nextVertex1 = this.vertexSequence.getFirst();
        }
        nextVertex2 = border.getVertexSequence().get(1);

        if (nextVertex1.distanceTo(nextVertex2)
                > previousVertex1.distanceTo(nextVertex2)) {
            this.revertSequence();
        }
        this.changeFirstVertex(firstVertex1);
        //}

    }

    public final Set linkTo(Border border) {
        Set<Link> links = new TreeSet();

        for (Vertex vertex : this.getVertexSequence()) {
            Vertex linked = border.getFirstVertex();
            for (Vertex candidat : border.getVertexSequence()) {
                if (vertex.distanceTo(candidat) < vertex.distanceTo(linked)) {
                    linked = candidat;
                }
            }
            links.add(new Link(vertex, linked,
                    this.getVertexSequence().indexOf(vertex),
                    border.getVertexSequence().indexOf(linked)));
        }

        for (Vertex vertex : border.getVertexSequence()) {
            Vertex linked = this.getFirstVertex();
            for (Vertex candidat : this.getVertexSequence()) {
                if (vertex.distanceTo(candidat) < vertex.distanceTo(linked)) {
                    linked = candidat;
                }
            }
            links.add(new Link(vertex, linked,
                    border.getVertexSequence().indexOf(vertex),
                    this.getVertexSequence().indexOf(linked)));
        }

        //removeDuplicates(links);
        Set<Link> newLinks = new HashSet();
        Vertex origCurrent, origPrevious, destCurrent, destPrevious;
        Link linkCurrent;
        Iterator<Link> linkIterator = links.iterator();
        origPrevious = this.getFirstVertex();
        destPrevious = border.getFirstVertex();
        while (linkIterator.hasNext()) {
            linkCurrent = linkIterator.next();
            origCurrent = linkCurrent.getOrigin();
            destCurrent = linkCurrent.getDestination();
            if (origCurrent != origPrevious && destCurrent != destPrevious) {
                final List<Vertex> vertexToLink = findVertexLinked(origCurrent,
                        origPrevious, destCurrent, destPrevious);
                newLinks.add(new Link(vertexToLink.get(0), vertexToLink.get(1),
                        this.getVertexSequence().indexOf(vertexToLink.get(0)),
                        border.getVertexSequence().indexOf(vertexToLink.get(1))));

            }
            origPrevious = origCurrent;
            destPrevious = destCurrent;
        }
//        if (this.isCircular) {
//            List<Vertex> vertexToLink = findVertexLinked(this.getFirstVertex(),
//                    this.getLastVertex(), border.getFirstVertex(),
//                    border.getLastVertex());
//            newLinks.add(new Link(vertexToLink.get(0),
//                    vertexToLink.get(1),
//                    this.getVertexSequence().indexOf(vertexToLink.get(0)),
//                    border.getVertexSequence().indexOf(vertexToLink.get(1))));
//        }
        links.addAll(newLinks);
        return links;
    }

    private List findVertexLinked(final Vertex ori1, final Vertex ori2,
            final Vertex dest1, final Vertex dest2) {
        final List result = new ArrayList();
        double distance1, distance2;
        distance1 = ori1.distanceTo(dest2);
        distance2 = ori2.distanceTo(dest1);

        if (distance1 < distance2) {
            result.add(ori1);
            result.add(dest2);
        } else {
            result.add(ori2);
            result.add(dest1);
        }
        return result;
    }

    public List exportLinks(Set links, int idShift) {
        List<Face> newFaces = new ArrayList();

        Vertex origCurrent, origPrevious, destCurrent, destPrevious;
        Link linkCurrent;
        Iterator<Link> linkIterator = links.iterator();
        origPrevious = linkIterator.next().getOrigin();
        destPrevious = linkIterator.next().getDestination();
        linkIterator = links.iterator(); // Reset the iterator
        while (linkIterator.hasNext()) {
            linkCurrent = linkIterator.next();
            origCurrent = linkCurrent.getOrigin();
            destCurrent = linkCurrent.getDestination();
            if (origCurrent != origPrevious) {
                newFaces.add(new Face(origCurrent.getId(),
                        origPrevious.getId(), destCurrent.getId() + idShift));
            } else if (destCurrent != destPrevious) {
                newFaces.add(new Face(origCurrent.getId(), destPrevious.getId()
                        + idShift, destCurrent.getId() + idShift));
            }
            origPrevious = origCurrent;
            destPrevious = destCurrent;
        }
        return newFaces;
    }
}
