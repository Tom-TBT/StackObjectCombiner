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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class MeshTest {


    /**
     * Test of addVertex method, of class Mesh.
     */
    @Test
    public void testAddVertex() {
        System.out.println("addVertex");
        Vertex vertex = null;
        Mesh instance = new Mesh();
        instance.addVertex(vertex);
        int numberOfVertices = instance.vertices.size();
        int expResult = 1;
        assertEquals(expResult, numberOfVertices);

    }

    /**
     * Test of addFace method, of class Mesh.
     */
    @Test
    public void testAddFace() {
        System.out.println("addFace");
        Face face = null;
        Mesh instance = new Mesh();
        instance.addFace(face);
        int numberOfFaces = instance.faces.size();
        int expResult = 1;
        assertEquals(expResult, numberOfFaces);
    }

    /**
     * Test of takeCloserVertexX method, of class Mesh.
     */
    @Test
    public void testTakeCloserVertexX() throws IOException {
        System.out.println("takeCloserVertexX");
        int splitPosition = 291;
        Mesh instance = ObjReader.readMesh("./src/test/java/gin/melec/MeshForTests/MeshTest_line.obj");
        int expVertices = 36;
        int expFaces = 44;
        int result = instance.vertices.size();
        assertEquals(expVertices, result);
        result = instance.faces.size();
        assertEquals(expFaces, result);

        Vertex closerV = instance.takeCloserVertexX(splitPosition);
        int expResult = 1;
        result = closerV.id;
        assertEquals(expResult, result);
    }

    /**
     * Test of doNeighborhood method, of class Mesh.
     */
    @Test
    public void doNeighborhood() throws IOException {
        System.out.println("doNeighborhood");
        Mesh mesh = ObjReader.readMesh("./src/test/java/gin/melec/MeshForTests/MeshTest_line.obj");
        mesh.doNeighborhood();
        Vertex vertex = (Vertex) mesh.vertices.get(0);
        int result = vertex.neighbours.size();
        int expResult = 2;
        assertEquals(expResult, result);
        vertex = (Vertex) mesh.vertices.get(15);
        result = vertex.neighbours.size();
        expResult = 6;
        assertEquals(expResult, result);

        boolean isHere1 = false, isHere2 = false, isHere3 = false,
                isHere4 = false, isHere5 = false, isHere6 = false;
        for (Object element : vertex.neighbours) {
            System.out.println(element);
            if (element.equals(mesh.vertices.get(4))) {
                isHere2 = true;
            }
            if (element.equals(mesh.vertices.get(3))) {
                isHere1 = true;
            }
            if (element.equals(mesh.vertices.get(14))) {
                isHere3 = true;
            }
            if (element.equals(mesh.vertices.get(26))) {
                isHere4 = true;
            }
            if (element.equals(mesh.vertices.get(27))) {
                isHere5 = true;
            }
            if (element.equals(mesh.vertices.get(16))) {
                isHere6 = true;
            }
        }
        assertTrue("Vertex 4 isn't in da hood", isHere1);
        assertTrue("Vertex 5 isn't in da hood", isHere2);
        assertTrue("Vertex 15 isn't in da hood", isHere3);
        assertTrue("Vertex 27 isn't in da hood", isHere4);
        assertTrue("Vertex 28 isn't in da hood", isHere5);
        assertTrue("Vertex 17 isn't in da hood", isHere6);
    }

}
