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
public class SplitUp extends AbstractSplit {

    /**
     * Public constructor for a split with a position.
     * @param position , the position of the border.
     */
    public SplitUp(final int position) {
        super(position);
    }

    @Override
    protected float distanceTo(final Vertex vertex) {
        return Math.abs(this.position - vertex.y);
    }

    @Override
    protected final int xPosition() {
        return 0;
    }

    @Override
    protected final int yPosition() {
        return position;
    }

    @Override
    protected final boolean isClose(final Vertex vertex) {
        return vertex.y < (this.position + WINDOW);
    }

}
