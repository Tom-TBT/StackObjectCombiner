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

    public static final Set linkTo(final Border origin,
            final Border destination) {
        final Set<Link> links = new TreeSet();

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
        for (Vertex vertex : destination.getVertexSequence()) {
            Vertex linked = origin.getFirstVertex();
            for (Vertex candidat : origin.getVertexSequence()) {
                if (vertex.distanceTo(candidat) < vertex.distanceTo(linked)) {
                    linked = candidat;
                }
            }
            links.add(new Link(linked, vertex,
                    origin.getVertexSequence().indexOf(linked),
                    destination.getVertexSequence().indexOf(vertex)));
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
                newFaces.add(new Face(origCurrent.getId(),
                        origPrevious.getId(), destCurrent.getId() + idShift));
            } else if (!destCurrent.equals(destPrevious)) {
                newFaces.add(new Face(origCurrent.getId(), destPrevious.getId()
                        + idShift, destCurrent.getId() + idShift));
            }
            origPrevious = origCurrent;
            destPrevious = destCurrent;
        }
        return newFaces;
    }
}
