/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gin.melec;

import org.junit.Test;

/**
 *
 * @author tom
 */
public class Scene2PartTest {

    public Scene2PartTest() {
    }

    /**
     * Test of loadMesh method, of class Scene2Part.
     */
    @Test
    public void testLoadMesh() {
        System.out.println("loadMesh");
        Scene2Part scene = new Scene2Part("./src/test/java/gin/melec/MeshForTests",100);
        Mesh mesh = scene.loadMesh("B_MeshLoaderTest.obj", null);
        for(int i = 0; i< mesh.vertices.size(); i++) {
            System.out.println(mesh.vertices.get(i).toString());
        }
        for(int i = 0; i< mesh.faces.size(); i++) {
            System.out.println(mesh.faces.get(i).toString());
        }
    }

    /**
     * Test of shiftMeshes method, of class Scene2Part.
     */
    @Test
    public void testShiftMeshes() {
        System.out.println("shiftMeshes");
        Scene2Part scene = new Scene2Part("./src/test/java/gin/melec/MeshForTests",10);
        scene.shiftMeshes();
    }
//
//    /**
//     * Test of createLimit method, of class Scene2Part.
//     */
//    @Test
//    public void testCreateLimit() {
//        System.out.println("createLimit");
//        Scene2Part instance = null;
//        instance.createLimit();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}
