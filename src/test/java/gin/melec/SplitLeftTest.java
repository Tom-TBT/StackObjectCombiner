/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gin.melec;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author tom
 */
public class SplitLeftTest {

    public SplitLeftTest() {
    }

    Split splitLeft = new SplitLeft(100);

    /**
     * Test of findBorderVertices method, of class SplitLeft.
     */
    @Test
    public void testFindBorderVertices() {
        System.out.println("findBorderVertices");
        Vertex v1 = new Vertex(1, 101, 0 , 0);
        Vertex v2 = new Vertex(1, 103, 0 , 0);
        Vertex v3 = new Vertex(1, 102, 0 , 0);
        List vertices = new ArrayList();
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);

        List expResult = new ArrayList();
        expResult.add(v1);
        List result = splitLeft.findBorderVertices(vertices);
        assertEquals(expResult.get(0), result.get(0));
        assertEquals(1,result.size());
    }

    /**
     * Test of createVirtual method, of class SplitLeft.
     */
    @Test
    public void testCreateVirtual() {
        System.out.println("createVirtual");
        Vertex original = new Vertex(1,1,1,1);
        Vertex expResult = new Vertex(1,0,1,1);
        Vertex result = splitLeft.createVirtual(original);
        assertEquals(expResult.x, result.x, 0);
    }

    /**
     * Test of findCloserVertex method, of class SplitLeft.
     */
    @Test
    public void testFindCloserVertex() {
        System.out.println("findCloserVertex");
        Vertex v1 = new Vertex(1, 101, 0 , 0);
        Vertex v2 = new Vertex(1, 103, 0 , 0);
        Vertex v3 = new Vertex(1, 102, 0 , 0);
        List vertices = new ArrayList();
        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);

        Vertex result = splitLeft.findCloserVertex(vertices);
        assertEquals(v1, result);
    }

    /**
     * Test of xPosition method, of class SplitLeft.
     */
    @Test
    public void testXPosition() {
        System.out.println("xPosition");
        int expResult = 100;
        int result = splitLeft.xPosition();
        assertEquals(expResult, result);
    }

    /**
     * Test of yPosition method, of class SplitLeft.
     */
    @Test
    public void testYPosition() {
        System.out.println("yPosition");
        int expResult = 0;
        int result = splitLeft.yPosition();
        assertEquals(expResult, result);
    }

}
