/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gin.melec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author tom
 */
public class MeshTest {

    public MeshTest() {
    }

    Mesh meshTest;

//    @Before
//    public void setUp() {
//        Vertex v1 = new Vertex(1, 99.5f, 1, 1);
//        Vertex v2 = new Vertex(2, 99.4f, 2, 1);
//        Vertex v3 = new Vertex(3, 99.3f, 3, 1);
//        Vertex v4 = new Vertex(4, 99.3f, 3, 2);
//        Vertex v5 = new Vertex(5, 99.3f, 3, 3);
//
//        Vertex v6 = new Vertex(6, 98f, 1.5f, 1);
//        Vertex v7 = new Vertex(7, 98f, 2.5f, 1);
//        Vertex v8 = new Vertex(8, 98f, 3f, 1.5f);
//        Vertex v9 = new Vertex(9, 98f, 3f, 2.5f);
//
//        Vertex v10 = new Vertex(10, 98f, 100, 100);
//        Vertex v11 = new Vertex(10, 98f, 101, 100);
//        Vertex v12 = new Vertex(10, 98f, 102, 100);
//
//        Face f1 = new Face(1, 2, 6);
//        Face f2 = new Face(2, 3, 7);
//        Face f3 = new Face(3, 4, 8);
//        Face f4 = new Face(4, 5, 9);
//
//        Face f5 = new Face(2, 6, 7);
//        Face f6 = new Face(3, 7, 8);
//        Face f7 = new Face(4, 8, 9);
//
//        Face f8 = new Face(10, 11, 12);
//
//        List splits = new ArrayList();
//        splits.add(new SplitRight(100));
//        meshTest = new Mesh(splits);
//
//        meshTest.vertices.add(v1);
//        meshTest.vertices.add(v2);
//        meshTest.vertices.add(v3);
//        meshTest.vertices.add(v4);
//        meshTest.vertices.add(v5);
//        meshTest.vertices.add(v6);
//        meshTest.vertices.add(v7);
//        meshTest.vertices.add(v8);
//        meshTest.vertices.add(v9);
//        meshTest.vertices.add(v10);
//        meshTest.vertices.add(v11);
//        meshTest.vertices.add(v12);
//
//        meshTest.faces.add(f1);
//        meshTest.faces.add(f2);
//        meshTest.faces.add(f3);
//        meshTest.faces.add(f4);
//        meshTest.faces.add(f5);
//        meshTest.faces.add(f6);
//        meshTest.faces.add(f7);
//        meshTest.faces.add(f8);
//    }

//    @Test
//    public void testShift() throws IOException {
//        System.out.println("Shift");
//        AbstractSplit rightSplit = new SplitRight(248);
//        List splits = new ArrayList(); splits.add(rightSplit);
//        Mesh mesh = new Mesh(splits);
//        ObjReader.readMesh("./src/test/java/gin/melec/LinearTest/B_LinearBorder1.obj"
//                , mesh.getVertices(), mesh.getFaces());
//        mesh.shift();
//        for(Vertex vertex : mesh.getVertices()){
//            System.out.println(vertex.toString());
//        }
//        for(Face face : mesh.getFaces()){
//            System.out.println(face.toString());
//        }
//    }


    @Test
    public void testCreateBorders() throws IOException {
        System.out.println("CreateBorders");
        AbstractSplit rightSplit = new SplitRight(102);
        List splits = new ArrayList(); splits.add(rightSplit);
        Mesh circularMesh = new Mesh(splits);
        ObjReader.readMesh("./src/test/java/gin/melec/MeshForTests/A_CircularBorder1.obj"
                , circularMesh.getVertices(), circularMesh.getFaces());

        circularMesh.createBorders();
        circularMesh.exportBorders("./src/test/java/gin/melec/MeshForTests/A_CircularBorder1_borders.obj");
    }
//
//
//    @Test
//    public void testCreateDoubleBorders() throws IOException {
//        System.out.println("createDoubleBorders");
//        AbstractSplit rightSplit = new SplitRight(133);
//        List splits = new ArrayList(); splits.add(rightSplit);
//        Mesh doubleBorder = new Mesh(splits);
//        ObjReader.readMesh("./src/test/java/gin/melec/MeshForTests/A_MultiBorder1.obj"
//                , doubleBorder.getVertices(), doubleBorder.getFaces());
//
//        doubleBorder.createBorders();
//
//        doubleBorder.exportBorders("./src/test/java/gin/melec/MeshForTests/A_MultiBorder1_borders.obj");
//    }
//    @Test
//    public void testCreateUpBorders() throws IOException {
//        System.out.println("createUpBorders");
//        AbstractSplit rightSplit = new SplitRight(148);
//        List splits = new ArrayList(); splits.add(rightSplit);
//        Mesh upBorder = new Mesh(splits);
//        upBorder.vertices = rightSplit.findBorderVertices(upBorder.vertices);
//        ObjReader.readMesh("./src/test/java/gin/melec/MeshForTests/A_UpBorder1.obj"
//                , upBorder.vertices, upBorder.faces);
//
//        upBorder.createBorders();
//
//        upBorder.exportBorders("./src/test/java/gin/melec/MeshForTests/A_UpBorder1_borders.obj");
//    }

//    @Test
//    public void testCreateLinearBorders() throws IOException {
//        System.out.println("createLinearBorders");
//        AbstractSplit rightSplit = new SplitRight(247);
//        List<AbstractSplit> splits = new ArrayList(); splits.add(rightSplit);
//        Mesh linearBorder = new Mesh(splits);
//        ObjReader.readMesh("./src/test/java/gin/melec/MeshForTests/A_LinearBorder1.obj"
//                , linearBorder.getVertices(), linearBorder.getFaces());
//
//        linearBorder.createBorders();
//
//        linearBorder.exportBorders("./src/test/java/gin/melec/MeshForTests/A_LinearBorder1_borders.obj");
//    }

//    @Test
//    public void testCreateLinearBorders2() throws IOException {
//        System.out.println("createLinearBorders");
//        AbstractSplit leftSplit = new SplitLeft(248);
//        List<AbstractSplit> splits = new ArrayList(); splits.add(leftSplit);
//        Mesh linearBorder = new Mesh(splits);
//        ObjReader.readMesh("./src/test/java/gin/melec/MeshForTests/B_LinearBorder1.obj"
//                , linearBorder.getVertices(), linearBorder.getFaces());
//        linearBorder.createBorders();
//
//        System.out.println("Border2 OK");
//
//        linearBorder.exportBorders("./src/test/java/gin/melec/MeshForTests/B_LinearBorder1_borders.obj");
//    }


//
//    @Test
//    public void testCreateHasardBorders() throws IOException {
//        System.out.println("createHasardBorders");
//        AbstractSplit rightSplit = new SplitRight(266);
//        List splits = new ArrayList(); splits.add(rightSplit);
//        Mesh hasardBorder = new Mesh(splits);
//        ObjReader.readMesh("./src/test/java/gin/melec/MeshForTests/A_HasardBorder.obj"
//                , hasardBorder.vertices, hasardBorder.faces);
//
//        hasardBorder.createBorders();
//
//        hasardBorder.exportBorders("./src/test/java/gin/melec/MeshForTests/A_HasardBorder_borders.obj");
//    }
//
//    @Test
//    public void testCreateHasardBorders2() throws IOException {
//        System.out.println("createHasardBorders2");
//        AbstractSplit rightSplit = new SplitRight(128);
//        List splits = new ArrayList(); splits.add(rightSplit);
//        Mesh hasardBorder;// = new Mesh(splits);
////        ObjReader.readMesh("./src/test/java/gin/melec/MeshForTests/A_HasardBorder2.obj"
////                , hasardBorder.vertices, hasardBorder.faces);
////
////        hasardBorder.createBorders();
////
////        hasardBorder.exportBorders("./src/test/java/gin/melec/MeshForTests/A_HasardBorder_borders2.obj");
//
//        hasardBorder = new Mesh(splits);
//        ObjReader.readMesh("./src/test/java/gin/melec/MeshForTests/A_HasardBorder3.obj"
//                , hasardBorder.vertices, hasardBorder.faces);
//
//        hasardBorder.createBorders();
//
//        hasardBorder.exportBorders("./src/test/java/gin/melec/MeshForTests/A_HasardBorder_borders3.obj");
//    }


    // Create the two borders we need for the tests.
//    @Test
//    public void testCreateBorders() throws IOException {
//        System.out.println("CreateBordersMito");
//        AbstractSplit leftSplit = new SplitLeft(103);
//        List splits = new ArrayList(); splits.add(leftSplit);
//        Mesh mitoGauche = new Mesh(splits);
//        ObjReader.readMesh("./src/test/java/gin/melec/MitoTest/A_MitoGauche.obj"
//                , mitoGauche.getVertices(), mitoGauche.getFaces());
//        mitoGauche.createBorders();
//        mitoGauche.exportBorders("./src/test/java/gin/melec/MitoTest/A_mitoGauche_bordure.obj");
//
//        AbstractSplit rightSplit = new SplitRight(103);
//        splits = new ArrayList(); splits.add(rightSplit);
//        Mesh mitoDroite = new Mesh(splits);
//        ObjReader.readMesh("./src/test/java/gin/melec/MitoTest/B_MitoDroite.obj"
//                , mitoDroite.getVertices(), mitoDroite.getFaces());
//        mitoDroite.createBorders();
//        mitoDroite.exportBorders("./src/test/java/gin/melec/MitoTest/B_mitoDroite_bordure.obj");
//    }
//    @Test
//    public void testJoinBorders() {
//        System.out.println("JoinBorders");
//        AbstractSplit leftSplit = new SplitLeft(103);
//        AbstractSplit rightSplit = new SplitRight(103);
//        List splits = new ArrayList(); splits.add(rightSplit);
//        Mesh mitoDroite = new Mesh(splits);
//        splits = new ArrayList(); splits.add(leftSplit);
//        Mesh mitoGauche = new Mesh(splits);
//        mitoDroite.importBorders("./src/test/java/gin/melec/MitoTest/B_mitoDroite_bordure.obj");
//        mitoGauche.importBorders("./src/test/java/gin/melec/MitoTest/A_mitoGauche_bordure.obj");
//
//        System.out.println(mitoDroite.getBorders().size());
//    }


}
