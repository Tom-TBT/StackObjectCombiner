/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gin.melec;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tom
 */
public class MeshTest {

    public MeshTest() {
    }

    Mesh meshTest;

    @Before
    public void setUp() {
        Vertex v1 = new Vertex(1, 99.5f, 1, 1);
        Vertex v2 = new Vertex(2, 99.4f, 2, 1);
        Vertex v3 = new Vertex(3, 99.3f, 3, 1);
        Vertex v4 = new Vertex(4, 99.3f, 3, 2);
        Vertex v5 = new Vertex(5, 99.3f, 3, 3);

        Vertex v6 = new Vertex(6, 98f, 1.5f, 1);
        Vertex v7 = new Vertex(7, 98f, 2.5f, 1);
        Vertex v8 = new Vertex(8, 98f, 3f, 1.5f);
        Vertex v9 = new Vertex(9, 98f, 3f, 2.5f);

        Vertex v10 = new Vertex(10, 98f, 100, 100);
        Vertex v11 = new Vertex(10, 98f, 101, 100);
        Vertex v12 = new Vertex(10, 98f, 102, 100);

        Face f1 = new Face(1,2,6);
        Face f2 = new Face(2,3,7);
        Face f3 = new Face(3,4,8);
        Face f4 = new Face(4,5,9);

        Face f5 = new Face(2,6,7);
        Face f6 = new Face(3,7,8);
        Face f7 = new Face(4,8,9);

        Face f8 = new Face(10,11,12);

        List splits = new ArrayList();
        splits.add(new SplitRight(100));
        meshTest = new Mesh(splits);

        meshTest.vertices.add(v1);meshTest.vertices.add(v2);meshTest.vertices.add(v3);
        meshTest.vertices.add(v4);meshTest.vertices.add(v5);meshTest.vertices.add(v6);
        meshTest.vertices.add(v7);meshTest.vertices.add(v8);meshTest.vertices.add(v9);
        meshTest.vertices.add(v10);meshTest.vertices.add(v11);meshTest.vertices.add(v12);

        meshTest.faces.add(f1);meshTest.faces.add(f2);meshTest.faces.add(f3);
        meshTest.faces.add(f4);meshTest.faces.add(f5);meshTest.faces.add(f6);
        meshTest.faces.add(f7);meshTest.faces.add(f8);
    }

    /**
     * Test of findVertex method, of class Mesh.
     */
    @Test
    public void testFindVertex() {
        System.out.println("findVertex");
        int vertexID = 5;
        Vertex expResult = (Vertex)meshTest.vertices.get(4);
        Vertex result = meshTest.findVertex(vertexID);
        assertEquals(expResult.id, result.id);
    }

    /**
     * Test of doNeighborhood method, of class Mesh.
     */
    @Test
    public void testDoNeighborhood() {
        System.out.println("doNeighborhood");
        meshTest.doNeighborhood();
        Vertex v = (Vertex) meshTest.vertices.get(0);
        assertEquals(2, v.neighbours.size());
    }
    /**
     * Test of completeGarbage method, of class Mesh.
     */
    @Test
    public void testCompleteGarbage() {
        System.out.println("completeGarbage");
        meshTest.garbage.add(meshTest.vertices.get(0));
        meshTest.doNeighborhood();
        meshTest.completeGarbage();
        assertEquals(9, meshTest.garbage.size());
    }


    /**
     * Test of shift method, of class Mesh.
     */
    @Test
    public void testShift() {
        System.out.println("shift");
        meshTest.shift();
        Vertex result = (Vertex) meshTest.vertices.get(0);
        assertEquals(199.5, result.x, 0);
    }

    /**
     * Test of findNextVertex method, of class Mesh.
     */
    @Test
    public void testFindNextVertex() {
        System.out.println("findNextVertex");
        Split split = null;
        Mesh instance = null;
        Vertex expResult = null;
        Vertex result = instance.findNextVertex(split);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createBorders method, of class Mesh.
     */
    @Test
    public void testCreateBorders() {
        System.out.println("createBorders");
        Mesh instance = null;
        instance.createBorders();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }


}
