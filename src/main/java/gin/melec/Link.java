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

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Link implements Comparable<Link> {

    /**
     * The vertex from where the link is initiate. In a set of links, the origin
     * always come from the same border.
     */
    private final Vertex origin;

    /**
     * The destination of the link. In a set of links, the destination always
     * come from the same border.
     */
    private final Vertex destination;

    /**
     * The index of the origin in the border list.
     */
    private final int indexOrigin;
    /**
     * The index of the destination in the border list.
     */
    private final int indexDestination;

    /**
     * Public constructor of a link. A link is formed with two vertex : a vertex
     * from where the link originate (origin), and a second, the destination.
     * The index are the index in their sequence, and they are used to compare
     * two links (interface comparable).
     * @param origin , the vertex from where the link originate.
     * @param destination , the second vertex linked.
     * @param indexOrigin , the index of the origin in his sequence.
     * @param indexDestination , the index of the destination in his sequence.
     */
    public Link(final Vertex origin, final Vertex destination,
            final int indexOrigin, final int indexDestination) {
        this.origin = origin;
        this.destination = destination;
        this.indexOrigin = indexOrigin;
        this.indexDestination = indexDestination;
    }

    /**
     * Getter of the origin.
     * @return the origin of the link.
     */
    public Vertex getOrigin() {
        return origin;
    }
    /**
     * Getter of the destination.
     * @return the destination of the link.
     */
    public Vertex getDestination() {
        return destination;
    }

    /**
     * Getter of the indexOrigin.
     * @return the index of the origin in his sequence.
     */
    public int getIndexOrigin() {
        return indexOrigin;
    }

    /**
     * Getter of the indexDestination.
     * @return the index of the destination in his sequence.
     */
    public int getIndexDestination() {
        return indexDestination;
    }

    @Override
    public final int compareTo(final Link link) {
        int resultat;
        int link1bigger, link1smaller, link2bigger, link2smaller;
        if (this.indexOrigin > this.indexDestination) {
            link1bigger = this.indexOrigin;
            link1smaller = this.indexDestination;
        } else {
            link1bigger = this.indexDestination;
            link1smaller = this.indexOrigin;
        }
        if (link.indexOrigin > link.indexDestination) {
            link2bigger = link.indexOrigin;
            link2smaller = link.indexDestination;
        } else {
            link2bigger = link.indexDestination;
            link2smaller = link.indexOrigin;
        }

        if (link1bigger == link2bigger) {
            resultat = link1smaller - link2smaller;
        } else {
            resultat = link1bigger - link2bigger;
        }
        return resultat;
    }

    @Override
    public final int hashCode() {
        int hash = 5;
        hash = 19 * hash + (this.origin != null ? this.origin.hashCode() : 0);
        hash = 19 * hash + (this.destination != null
                ? this.destination.hashCode() : 0);
        hash = 19 * hash + this.indexOrigin;
        hash = 19 * hash + this.indexDestination;
        return hash;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Link other = (Link) obj;
        if ((this.origin != other.origin
                || this.destination != other.destination)
                && (this.origin != other.destination
                || this.destination != other.origin)) {
            return false;
        }
        if (this.destination != other.destination
                && this.destination != other.origin) {
            return false;
        }
        return true;
    }
}
