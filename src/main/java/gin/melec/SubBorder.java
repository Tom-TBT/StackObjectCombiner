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
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class SubBorder extends Border implements Serializable {

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
    private boolean circular;

    public SubBorder() {

    }

        /**
     * This method prepare the attributes of the border. This set up for example
     * the lenght, or the position of the center of the border. These parameters
     * are used for automatic matching of the borders.
     */
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
    }

    /**
     * This method take a border as reference, and align the border on. Can then
     * change the first vertex of the border, and the order of the sequence.
     *
     * @param border , the border on which this border is aligned.
     */
    public final void alignOn(Border border) {
        final Vertex firstVertex2 = border.getFirstVertex();
        Vertex firstVertex1 = this.getFirstVertex();
        Vertex previousVertex1, nextVertex1, nextVertex2;

        //TODO if (this.isCircular) {
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

    public double getStraightLenght() {
        return straightLenght;
    }

    public void setStraightLenght(double straightLenght) {
        this.straightLenght = straightLenght;
    }

    public double getCumulLenght() {
        return cumulLenght;
    }

    public void setCumulLenght(double cumulLenght) {
        this.cumulLenght = cumulLenght;
    }

    public Vertex getCenter() {
        return center;
    }

    public void setCenter(Vertex center) {
        this.center = center;
    }

    public boolean isCircular() {
        return circular;
    }

    public void setCircular(boolean isCircular) {
        this.circular = isCircular;
    }

}
