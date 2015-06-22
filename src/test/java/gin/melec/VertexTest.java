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

import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class VertexTest {

    public VertexTest() {
    }

    /**
     * Test of toString method, of class Vertex.
     */
    @org.junit.Test
    public void testToString() {
        System.out.println("toString");
        final Vertex instance = new Vertex(1450, 27.9f, 45, 74.3f);
        final String expResult = "v 27.9 45.0 74.3";
        String result = instance.toString();
        assertEquals(expResult, result);
    }

    /**
     * Test of toIdString method, of class Vertex.
     */
    @org.junit.Test
    public void testToIdString() {
        System.out.println("toIdString");
        final Vertex instance = new Vertex(1450, 27.9f, 45, 74.3f);
        String expResult = "v 27.9 45.0 74.3 1450";
        String result = instance.toIdString();
        assertEquals(expResult, result);
    }

    /**
     * Test of distanceOnYZ method, of class Vertex.
     */
    @org.junit.Test
    public void testDistanceOnYZ() {
        System.out.println("distanceOnYZ");
        Vertex ver = new Vertex(666, 14, 9, 19);
        Vertex instance = new Vertex(667, 12, 12, 23);
        float expResult = 5.0F;
        float result = instance.distanceOnYZ(ver);
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of distanceOnXZ method, of class Vertex.
     */
    @org.junit.Test
    public void testDistanceOnXZ() {
        System.out.println("distanceOnXZ");
        Vertex ver = new Vertex(668, 14, 87, 9);
        Vertex instance = new Vertex(669, 17, 65, 5);
        float expResult = 5.0F;
        float result = instance.distanceOnXZ(ver);
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of distanceOnX method, of class Vertex.
     */
    @org.junit.Test
    public void testDistanceOnX() {
        System.out.println("distanceOnX");
        Vertex ver = new Vertex(665, 13.8f, 87, 9);
        Vertex instance = new Vertex(664, 14.5f, 87, 9);
        float expResult = 0.7F;
        float result = instance.distanceOnX(ver);
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of distanceOnY method, of class Vertex.
     */
    @org.junit.Test
    public void testDistanceOnY() {
        System.out.println("distanceOnX");
        Vertex ver = new Vertex(665, 13.8f, 87, 9);
        Vertex instance = new Vertex(664, 14.5f, 95, 9);
        float expResult = 8.0F;
        float result = instance.distanceOnY(ver);
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of distanceToBorderX method, of class Vertex.
     */
    @org.junit.Test
    public void testDistanceToBorderX() {
        System.out.println("distanceToBorderX");
        int splitPosition = 300;
        Vertex instance = new Vertex(664, 299.2f, 95, 9);
        float expResult = 0.8F;
        float result = instance.distanceToBorderX(splitPosition);
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of distanceToBorderY method, of class Vertex.
     */
    @org.junit.Test
    public void testDistanceToBorderY() {
        System.out.println("distanceToBorderX");
        int splitPosition = 200;
        Vertex instance = new Vertex(664, 299.2f, 199, 9);
        float expResult = 1.0F;
        float result = instance.distanceToBorderY(splitPosition);
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of findNextX method, of class Vertex.
     */
    @org.junit.Test
    public void testFindNextX() {

        // Initialization of the mesh : 5 vertex for the border, 5 just behind
        // and 4 other behind.

        Vertex border1, border2, border3, border4, border5;
        Vertex close1, close2, close3, close4, close5;
        Vertex behind1, behind2, behind3, behind4;
        border1 = new Vertex(1, 299.08f, 15, 64);
        border2 = new Vertex(2, 299.52f, 12, 61);
        border3 = new Vertex(3, 299.32f, 9, 64);
        border4 = new Vertex(4, 299.18f, 6, 67);
        border5 = new Vertex(5, 299.24f, 3, 71);
        close1 = new Vertex(6, 298.70f, 14, 64);
        close2 = new Vertex(7, 298.90f, 11.5f, 62);
        close3 = new Vertex(8, 299.10f, 8.8f, 63); // Closer
        close4 = new Vertex(9, 298.99f, 6, 66);
        close5 = new Vertex(10, 299.10f, 3, 70);
        behind1 = new Vertex(10, 298.10f, 13, 63);
        behind2 = new Vertex(10, 298.05f, 12.5f, 61);
        behind3 = new Vertex(10, 297.13f, 9, 65);
        behind4 = new Vertex(10, 297.50f, 5, 69);

        // Constructing the neighborhood.
        border1.neighbours.add(border2); border1.neighbours.add(close1);
        border2.neighbours.add(border1); border2.neighbours.add(close1);
        border2.neighbours.add(border3); border2.neighbours.add(close2);
        border3.neighbours.add(border2); border3.neighbours.add(border4);
        border3.neighbours.add(close2); border3.neighbours.add(close3);
        border4.neighbours.add(border3); border4.neighbours.add(border5);
        border4.neighbours.add(close3); border4.neighbours.add(close4);
        border4.neighbours.add(close5);
        border5.neighbours.add(border4); border5.neighbours.add(close5);
        close1.neighbours.add(border1); close1.neighbours.add(border2);
        close1.neighbours.add(close2); close1.neighbours.add(behind1);
        close2.neighbours.add(close1); close2.neighbours.add(close3);
        close2.neighbours.add(border2); close2.neighbours.add(border3);
        close2.neighbours.add(behind1); close2.neighbours.add(behind2);
        close3.neighbours.add(close2); close3.neighbours.add(close4);
        close3.neighbours.add(border3); close3.neighbours.add(border4);
        close3.neighbours.add(behind2); close3.neighbours.add(behind3);
        close4.neighbours.add(close3); close4.neighbours.add(close5);
        close4.neighbours.add(border4); close4.neighbours.add(behind3);
        close4.neighbours.add(behind4);
        close5.neighbours.add(close4); close5.neighbours.add(border4);
        close5.neighbours.add(border5); close5.neighbours.add(behind4);
        behind1.neighbours.add(close1); behind1.neighbours.add(close2);
        behind1.neighbours.add(behind2);
        behind2.neighbours.add(behind1); behind2.neighbours.add(close2);
        behind2.neighbours.add(close3); behind2.neighbours.add(behind3);
        behind2.neighbours.add(close3); behind2.neighbours.add(close4);
        behind2.neighbours.add(behind2); behind2.neighbours.add(behind4);
        behind4.neighbours.add(close4); behind4.neighbours.add(close5);
        behind4.neighbours.add(behind3);

        System.out.println("findNextX");
        int splitPosition = 300;
        // Test in the starting conditions : This is the first vertex, he have
        // no previousVertex. This vertex is on top.
        Vertex instance = border1;
        Vertex expResult = border2;
        Vertex result = instance.findNextX(splitPosition);
        assertEquals(expResult, result);

        // Test in the starting conditions : This is the first vertex, he have
        // no previousVertex. This vertex is on middle.
        instance = border3;
        expResult = border2;
        result = instance.findNextX(splitPosition);
        assertEquals(expResult, result);

        // Test in the routine. The vertex has a previousVertex.
        border3.previousVertex = border2;
        instance = border3;
        expResult = border4;
        result = instance.findNextX(splitPosition);
        assertEquals(expResult, result);
        border3.previousVertex = null;

        // Test at the end. We have reach the end and their should be no next.
        border5.previousVertex = border4;
        instance = border5;
        expResult = null;
        result = instance.findNextX(splitPosition);
        assertEquals(expResult, result);
        border5.previousVertex = null;
    }

    /**
     * Test of findNextY method, of class Vertex.
     */
    @org.junit.Test
    public void testFindNextY() {
        System.out.println("findNextY");
        int splitPosition = 0;
        Mesh mesh = null;
        Vertex instance = null;
        Vertex expResult = null;
        Vertex result = instance.findNextY(splitPosition, mesh);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
