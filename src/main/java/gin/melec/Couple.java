/*
 * Copyright (C) 2016 ImageJ
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package gin.melec;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Couple implements Comparable<Couple>{
    private Border border1;

    private Border border2;

    private double distance;

    protected Couple(final Border b1, final Border b2) {
        border1 = b1;
        border2 = b2;

        this.computeDistance();
    }

    private void computeDistance() {
        double result = 0;

        double cumulLenghtDistance = Math.abs((2*(border1.getCumulLenght()
                - border2.getCumulLenght()))/(border1.getCumulLenght()
                + border2.getCumulLenght()))*100;

        
        this.distance = result;
    }

    protected double getDistance() {
        return distance;
    }

    @Override
    public int compareTo(Couple c) {
        return (int)Math.floor(this.distance - c.distance);
    }
}
