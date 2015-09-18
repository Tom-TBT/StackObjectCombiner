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
public class Linker {

    private Linker() {
    }

    public static Linker getInstance() {
        return LinkerHolder.INSTANCE;
    }

    private static class LinkerHolder {

        private static final Linker INSTANCE = new Linker();
    }

    /**
     * This method choose between 4 vertex the two that will form a link, to
     * cut a quadrilateral in two triangles.
     * @param ori1 , the first vertex candidate as origin.
     * @param ori2 , the second vertex candidate as origin.
     * @param dest1 , the first vertex candidate as destination.
     * @param dest2 , the second vertex candidate as destination.
     * @return a list composed of two vertex, index 0 containing the origin and
     * index 1 containing the destination.
     */
    private static List findVertexLinked(final Vertex ori1, final Vertex ori2,
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

    /**
     * Link the border origin to the border destination. Every link made will
     * have as origin a vertex from the origin border, and as destination a
     * vertex from the destination border.
     * @param origin , the origin border.
     * @param destination , the destination border.
     * @return a set of links between the two borders.
     */
    public static final Set linkTo(final Border origin,
            final Border destination) {
        final TreeSet<Link> links = new TreeSet();

        // Linking the origin vertices to the destination vertices
        for (Vertex vertex : origin.getVertexSequence()) {
            Vertex linked = destination.getFirstVertex();
            for (Vertex candidat : destination.getVertexSequence()) {
                if (vertex.distanceTo(candidat) < vertex.distanceTo(linked)) {
                    linked = candidat;
                }
            }
            links.add(new Link(vertex, linked,
                    origin.getVertexSequence().indexOf(vertex),
                    destination.getVertexSequence().indexOf(linked)));
        }
        // Linking the destination vertices to the origin vertices
        Iterator<Vertex> it = destination.getVertexSequence().iterator();
        Vertex previousVertex = it.next();
        Vertex currentVertex = it.next();
        while (it.hasNext()) {
            if (!Linker.containVertex(currentVertex, links)) {
                List<Vertex> candidates = Linker.findCandidates(previousVertex
                        , links);
                Vertex linked = currentVertex.whichCloser(candidates.get(0)
                        , candidates.get(1));
                links.add(new Link(linked, currentVertex,
                    origin.getVertexSequence().indexOf(linked),
                    destination.getVertexSequence().indexOf(currentVertex)));
            }
            previousVertex = currentVertex;
            currentVertex = it.next();
        }

        final Set<Link> newLinks = new HashSet();
        Vertex origCurrent, origPrevious, destCurrent, destPrevious;
        Link linkCurrent;
        final Iterator<Link> linkIterator = links.iterator();
        origPrevious = origin.getFirstVertex();
        destPrevious = destination.getFirstVertex();
        while (linkIterator.hasNext()) {
            linkCurrent = linkIterator.next();
            origCurrent = linkCurrent.getOrigin();
            destCurrent = linkCurrent.getDestination();
            if (!origCurrent.equals(origPrevious)
                    && !destCurrent.equals(destPrevious)) {
                final List<Vertex> vertexToLink = findVertexLinked(origCurrent,
                        origPrevious, destCurrent, destPrevious);
                newLinks.add(new Link(vertexToLink.get(0), vertexToLink.get(1),
                        origin.getVertexSequence().indexOf(vertexToLink.get(0)),
                        destination.getVertexSequence()
                        .indexOf(vertexToLink.get(1))));

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

    /**
     * Used once links have been made from one border to an other. After this,
     * their is still some vertex to link, but it can't be link in a way that
     * links cross each other. So this method find the vertex that can be
     * linked.
     * @param vertex , the previous vertex which is already linked.
     * @param links , the set of the links.
     * @return a list of the vertex that are linkable.
     */
    static List findCandidates(Vertex vertex, TreeSet<Link> links) {
        List<Vertex> candidates = new ArrayList();

        Iterator<Link> it = links.descendingIterator();
        Link currentLink = it.next();
        Vertex candidate1 = null, candidate2 = currentLink.getOrigin();
        while(it.hasNext()) {
            currentLink = it.next();
            candidate1 = currentLink.getOrigin();
            if (currentLink.getDestination().equals(vertex)) {
                break;
            }
        }
        candidates.add(candidate1);
        candidates.add(candidate2);

        return candidates;
    }

    /**
     * Return a boolean that indicates if the set of links contain as origin or
     * destination the vertex given.
     * @param vertex , the vertex to check.
     * @param links , the set of links to check.
     * @return true if the vertex is in a link, else false.
     */
    private static boolean containVertex(final Vertex vertex,
            final Set<Link> links) {
        boolean result = false;
        for (Link link : links) {
            if (link.getOrigin().equals(vertex)
                    || link.getDestination().equals(vertex)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Export the links of the set into faces.
     * @param links , the set where the links are stocked.
     * @param idShift , the shift to apply to the destination's id in faces.
     * @return the faces created from the links.
     */
    public static List exportLinks(final Set links, final int idShift) {
        final List<Face> newFaces = new ArrayList();

        Vertex origCurrent, origPrevious, destCurrent, destPrevious;
        Link linkCurrent;
        Iterator<Link> linkIterator = links.iterator();
        Link tmpLink = linkIterator.next();
        origPrevious = tmpLink.getOrigin();
        destPrevious = tmpLink.getDestination();
        linkIterator = links.iterator(); // Reset the iterator
        while (linkIterator.hasNext()) {
            linkCurrent = linkIterator.next();
            origCurrent = linkCurrent.getOrigin();
            destCurrent = linkCurrent.getDestination();
            if (!origCurrent.equals(origPrevious)) {
                destCurrent.incrementId(idShift);
                newFaces.add(new Face(origCurrent, origPrevious, destCurrent));
            } else if (!destCurrent.equals(destPrevious)) {
                destCurrent.incrementId(idShift);
                destPrevious.incrementId(idShift);
                newFaces.add(new Face(origCurrent, destPrevious, destCurrent));
            }
            origPrevious = origCurrent;
            destPrevious = destCurrent;
        }
        return newFaces;
    }
}
