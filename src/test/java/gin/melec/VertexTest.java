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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class VertexTest {

    public VertexTest() {
    }
    @Test
    public void testBelongToBorder() {
        Vertex v1 = new Vertex (1, 0, 0, 0);
        Vertex v2 = new Vertex (2, 1, 1, 0);
        Vertex v3 = new Vertex (3, -1, 1, 0);
        Vertex v4 = new Vertex (4, -1, -1, 0);
        Vertex v5 = new Vertex (5, 1, -1, 0);
        Face f1 = new Face(v1,v2,v3);
        Face f2 = new Face(v1,v3,v4);
        Face f3 = new Face(v1,v4,v5);
        Face f4 = new Face(v1,v5,v2);

        v1.getFaces().add(f1); v1.getFaces().add(f2); v1.getFaces().add(f3);
        v1.getFaces().add(f4); v2.getFaces().add(f1); v2.getFaces().add(f4);
        v3.getFaces().add(f1); v3.getFaces().add(f2); v4.getFaces().add(f2);
        v4.getFaces().add(f3); v5.getFaces().add(f3); v5.getFaces().add(f4);

        v1.getNeighbours().add(v2); v1.getNeighbours().add(v3);
        v1.getNeighbours().add(v4); v1.getNeighbours().add(v5);
        v2.getNeighbours().add(v3); v2.getNeighbours().add(v5);
        v3.getNeighbours().add(v2); v3.getNeighbours().add(v4);
        v4.getNeighbours().add(v3); v4.getNeighbours().add(v5);
        v5.getNeighbours().add(v2); v5.getNeighbours().add(v4);
        v2.getNeighbours().add(v1); v3.getNeighbours().add(v1);
        v4.getNeighbours().add(v1); v5.getNeighbours().add(v1);

        assertTrue(!v1.belongToBorder());
        assertTrue(v2.belongToBorder());
        assertTrue(v3.belongToBorder());
        assertTrue(v4.belongToBorder());
        assertTrue(v5.belongToBorder());
    }
}
