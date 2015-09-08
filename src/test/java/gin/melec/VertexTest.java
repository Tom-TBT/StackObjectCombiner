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
import static org.junit.Assert.assertEquals;


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
        float result = instance.distanceToSplitX(splitPosition);
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
        float result = instance.distanceToSplitY(splitPosition);
        assertEquals(expResult, result, 0.01);
    }

    /**
     * Test of addNeighborToGarbage, of class Vertex.
     * @throws java.io.IOException
     */
    @org.junit.Test
    public void testAddNeighborToGarbage() throws IOException {
        int splitPosition = 291;
        List splits = new ArrayList();
        splits.add(new SplitRight(splitPosition));
        Mesh mesh = new Mesh(splits);
        ObjReader.readMesh(
                "./src/test/java/gin/melec/MeshForTests/MeshTest_line_firstBot.obj",
                mesh.vertices, mesh.faces);
        mesh.doNeighborhood();
        // One vertex is put in the garbage
        mesh.garbage.add(mesh.vertices.get(0));
        mesh.completeGarbage();

        int expectedResult = 36;
        int result = mesh.garbage.size();

        assertEquals(expectedResult, result);
    }

}
