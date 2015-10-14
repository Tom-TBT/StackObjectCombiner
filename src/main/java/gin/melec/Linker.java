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

import ij.IJ;
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

    private static final int INCREM = 10;

    private Linker() {
    }

    public static Linker getInstance() {
        return LinkerHolder.INSTANCE;
    }

    private static class LinkerHolder {

        private static final Linker INSTANCE = new Linker();
    }

    /**
     * This method choose between 4 vertex the two that will form a link, to cut
     * a quadrilateral in two triangles.
     *
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
     *
     * @param origin , the origin border.
     * @param destination , the destination border.
     * @return a set of links between the two borders.
     */
    public static final List createFacesBetween(Border origin,
            Border destination) {

        // We always want to link first the border that has the fewer vertices,
        // to the border that had the more vertices.
        boolean inverted = false;
        if (origin.getVertexSequence().size()
                > destination.getVertexSequence().size()) {
            inverted = true;
            final Border tmp = origin;
            origin = destination;
            destination = tmp;
        }
        // The borders are aligned.
        destination.alignOn(origin);

        final TreeSet<Link> links = new TreeSet();
        List<Vertex> origSequence = origin.getVertexSequence();
        List<Vertex> destSequence = destination.getVertexSequence();
        Vertex prevOrigSpot;
        Vertex nextOrigSpot = origin.getFirstVertex();
        Vertex prevDestSpot;
        Vertex nextDestSpot = destination.getFirstVertex();
        // add new next link
        links.add(new Link(nextOrigSpot, nextDestSpot));
        int j = INCREM;
        boolean notFinished = true;
        while (notFinished) {
            if (j > origSequence.size()) {
                j = origSequence.size() - 1;
                notFinished = false;
            }
            // Creating the subsets
            prevOrigSpot = nextOrigSpot;
            nextOrigSpot = origSequence.get(j);
            prevDestSpot = nextDestSpot;
            nextDestSpot = nextOrigSpot.findCloserIn(destSequence.
                    subList(destSequence.
                            indexOf(prevDestSpot), destSequence.size()));

            // add new next link
            links.add(new Link(nextOrigSpot, nextDestSpot));

            List<Vertex> subListDest = new ArrayList(destSequence.
                    subList(destSequence.indexOf(prevDestSpot) + 1,
                            destSequence.indexOf(nextDestSpot)));
            List<Vertex> subListOrig = new ArrayList(origSequence.
                    subList(origSequence.indexOf(prevOrigSpot), j + 1));

            // getLinks form by the two subsets
            links.addAll(getLinksFromSubsets(subListOrig, subListDest));
            j += INCREM;
            IJ.log(Integer.toString(j));
        }
        SetIndexToLinks(links, origSequence, destSequence);

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
                // The if mean that the links have iterate on the two borders
                // at the same time -> A link is missing
                final List<Vertex> vertexToLink = findVertexLinked(origCurrent,
                        origPrevious, destCurrent, destPrevious);
                newLinks.add(new Link(vertexToLink.get(0), vertexToLink.get(1)));

            }
            origPrevious = origCurrent;
            destPrevious = destCurrent;
        }
//        if (origin.isCircular()) {  // If borders are circular, they need two
//                                    // more faces to close them.
//            List<Vertex> vertexToLink = findVertexLinked(this.getFirstVertex(),
//                    this.getLastVertex(), border.getFirstVertex(),
//                    border.getLastVertex());
//            newLinks.add(new Link(vertexToLink.get(0),
//                    vertexToLink.get(1),
//                    this.getVertexSequence().indexOf(vertexToLink.get(0)),
//                    border.getVertexSequence().indexOf(vertexToLink.get(1))));
//        }

        links.addAll(newLinks);
        SetIndexToLinks(links, origSequence, destSequence);

        if (inverted) {
            // If the borders were inverted, the links needs also to be inverted
            for (Link link : links) {
                link = new Link(link.getDestination(), link.getOrigin(),
                        link.getIndexDestination(), link.getIndexOrigin());
            }
        }
        return exportLinks(links);
    }

    /**
     * Used once links have been made from one border to an other. After this,
     * their is still some vertex to link, but it can't be link in a way that
     * links cross each other. So this method find the vertex that can be
     * linked.
     *
     * @param vertex , the previous vertex which is already linked.
     * @param links , the set of the links.
     * @return a list of the vertex that are linkable.
     */
    private static List findCandidates(Vertex vertex, TreeSet<Link> links) {
        final List<Vertex> candidates = new ArrayList();

        final Iterator<Link> it = links.descendingIterator();
        Link currentLink = it.next();
        Vertex candidate1 = null, candidate2 = currentLink.getOrigin();
        while (it.hasNext()) {
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
     *
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

    private static TreeSet<Link> getLinksFromSubsets(final List<Vertex> origin,
            List<Vertex> destination) {
        TreeSet<Link> result = new TreeSet();
        int i = 1, j = 0;
        while (i < origin.size() - 1) {
            Vertex prevOrigVertex = origin.get(i - 1);
            Vertex nextOrigVertex = origin.get(i);
            Vertex closerVertex = null;
            for (Vertex candidate : destination) {
                if (closerVertex == null) {
                    closerVertex = candidate;
                } else if (nextOrigVertex.distanceTo(candidate)
                        < nextOrigVertex.distanceTo(closerVertex)) {
                    closerVertex = candidate;
                }
            }
            result.add(new Link(nextOrigVertex, closerVertex));
            j = destination.indexOf(closerVertex);
            List<Vertex> subDestination = destination.subList(0, j);
            int k = 0;
            while (subDestination.size() > 0 && k < subDestination.size()) {
                Vertex vertexDest = subDestination.get(k);
                if (vertexDest.distanceTo(prevOrigVertex)
                        < vertexDest.distanceTo(nextOrigVertex)) {
                    // link to 1er et ajout ds set
                    result.add(new Link(prevOrigVertex, vertexDest));
                } else {
                    // All the remaining vertex need to be linked to the other
                    while (k < subDestination.size()) {
                        vertexDest = subDestination.get(k);
                        // link to 2eme et ajout ds set
                        result.add(new Link(nextOrigVertex, vertexDest));
                        k++;
                    }
                }
                k++;
            }
            destination = destination.subList(j, destination.size());
            i++;
        }

        return result;
    }

    /**
     * Export the links of the set into faces.
     *
     * @param links , the set where the links are stocked.
     * @return the faces created from the links.
     */
    public static List exportLinks(final Set links) {
        final List<Face> newFaces = new ArrayList();

        Vertex origCurrent, origPrevious, destCurrent, destPrevious;
        Link linkCurrent;
        Iterator<Link> linkIterator = links.iterator();
        final Link tmpLink = linkIterator.next();
        origPrevious = tmpLink.getOrigin();
        destPrevious = tmpLink.getDestination();
        linkIterator = links.iterator(); // Reset the iterator
        while (linkIterator.hasNext()) {
            linkCurrent = linkIterator.next();
            origCurrent = linkCurrent.getOrigin();
            destCurrent = linkCurrent.getDestination();
            if (!origCurrent.equals(origPrevious)) {
                newFaces.add(new Face(origCurrent, origPrevious, destCurrent));
            } else if (!destCurrent.equals(destPrevious)) {
                newFaces.add(new Face(origCurrent, destPrevious, destCurrent));
            }
            origPrevious = origCurrent;
            destPrevious = destCurrent;
        }
        return newFaces;
    }

    /**
     * Set the index of the vertex in the set of links. The index will allow the
     * set to order the links.
     *
     * @param links , the set of links.
     * @param origSequence , the sequence containing the origin of the links.
     * @param destSequence , the sequence containing the origin of the links.
     */
    private static void SetIndexToLinks(Set<Link> links,
            List<Vertex> origSequence, List<Vertex> destSequence) {
        for (Link link : links) {
            link.setIndexOrigin(origSequence.indexOf(link.getOrigin()));
            link.setIndexDestination(destSequence.indexOf(
                    link.getDestination()));
        }
    }
}
