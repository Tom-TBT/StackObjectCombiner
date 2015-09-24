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
import java.util.TreeSet;
import org.junit.Test;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class LinkerTest {

    public LinkerTest() {
    }

//    @Test
//    public void testLinkTo() {
//        Border border1 = new Border();
//        Border border2 = new Border();
//        border1.addNextVertex(new Vertex(1, 0, 0, 0));
//        border1.addNextVertex(new Vertex(2, 0, 1, 0));
//        border1.addNextVertex(new Vertex(3, 0, 1, 0.8f));
//        border1.addNextVertex(new Vertex(4, 0, 1, 1));
//        border1.addNextVertex(new Vertex(5, 0, 0, 1));
//        border2.addNextVertex(new Vertex(1, 1, 0, 0));
//        border2.addNextVertex(new Vertex(2, 1, 1, 0));
//        border2.addNextVertex(new Vertex(3, 1, 1, 1));
//        border2.addNextVertex(new Vertex(4, 1, 0, 1));
//
//        TreeSet<Link> links = new TreeSet();
//        links.addAll(Linker.linkTo(border1, border2));
//
//        for(Link link : links) {
//            System.out.println(link.getOrigin() + " // " + link.getDestination());
//        }
//    }
//
//    @Test
//    public void testExportLinks() {
//        Border border1 = new Border();
//        Border border2 = new Border();
//        border1.addNextVertex(new Vertex(91, 0, 0, 0));
//        border1.addNextVertex(new Vertex(92, 0, 1, 0));
//        border1.addNextVertex(new Vertex(93, 0, 1, 0.8f));
//        border1.addNextVertex(new Vertex(94, 0, 1, 1));
//        border1.addNextVertex(new Vertex(95, 0, 0, 1));
//        border2.addNextVertex(new Vertex(1, 1, 0, 0));
//        border2.addNextVertex(new Vertex(2, 1, 1, 0));
//        border2.addNextVertex(new Vertex(3, 1, 1, 1));
//        border2.addNextVertex(new Vertex(4, 1, 0, 1));
//
//        TreeSet<Link> links = new TreeSet();
//        links.addAll(Linker.linkTo(border1, border2));
//
//        List<Face> faces = Linker.exportLinks(links, 130);
//        for(Face face : faces) {
//            System.out.println(face.toString());
//        }
//    }

//    @Test
//    public void testRealBorder() throws IOException {
//        Mesh leftMesh, rightMesh;
//        AbstractSplit rightSplit = new SplitRight(248);
//        List splits = new ArrayList(); splits.add(rightSplit);
//        Mesh mitoGauche = new Mesh(splits);
//        ObjReader.readMesh("./src/test/java/gin/melec/LinearTest/A_LinearBorder1.obj"
//                , mitoGauche.getVertices(), mitoGauche.getFaces());
//        mitoGauche.createBorders();
//
//        System.out.println("Border1 OK");
//
//        AbstractSplit leftSplit = new SplitLeft(248);
//        splits = new ArrayList(); splits.add(leftSplit);
//        Mesh mitoDroite = new Mesh(splits);
//        ObjReader.readMesh("./src/test/java/gin/melec/LinearTest/B_LinearBorder1.obj"
//                , mitoDroite.getVertices(), mitoDroite.getFaces());
//        mitoDroite.createBorders();
//
//        System.out.println("Border2 OK");
//
//        TreeSet<Link> links = new TreeSet();
//        Border leftBorder = mitoGauche.getBorders().get(0);
//        Border rightBorder = mitoDroite.getBorders().get(0);
//        leftBorder.alignOn(rightBorder);
//        links.addAll(Linker.linkTo(leftBorder, rightBorder));
//        List<Face> newFaces = Linker.exportLinks(links, mitoGauche.getVertices().size());
//        for(Vertex vertex : mitoGauche.getVertices()) {
//            System.out.println(vertex.toString());
//        }
//        for(Vertex vertex : mitoDroite.getVertices()) {
//            System.out.println(vertex.toString());
//        }
//        for (Face face : mitoGauche.getFaces()) {
//            System.out.println(face.toString());
//        }
//        for (Face face : mitoDroite.getFaces()) {
//            System.out.println(face.toIncrementString(mitoGauche.getVertices().size()));
//        }
//        for(Face face : newFaces) {
//            System.out.println(face.toString());
//        }
//    }

//    @Test
//    public void testRealBorder() throws IOException {
//        Mesh leftMesh, rightMesh;
//        AbstractSplit leftSplit = new SplitLeft(103);
//        List splits = new ArrayList(); splits.add(leftSplit);
//        Mesh mitoGauche = new Mesh(splits);
//        ObjReader.readMesh("./src/test/java/gin/melec/MitoTest/A_MitoGauche.obj"
//                , mitoGauche.getVertices(), mitoGauche.getFaces());
//        mitoGauche.createBorders();
//
//        AbstractSplit rightSplit = new SplitRight(103);
//        splits = new ArrayList(); splits.add(rightSplit);
//        Mesh mitoDroite = new Mesh(splits);
//        ObjReader.readMesh("./src/test/java/gin/melec/MitoTest/B_MitoDroite.obj"
//                , mitoDroite.getVertices(), mitoDroite.getFaces());
//        mitoDroite.createBorders();
//
//        TreeSet<Link> links = new TreeSet();
//        Border leftBorder = mitoGauche.getBorders().get(0);
//        Border rightBorder = mitoDroite.getBorders().get(0);
//        leftBorder.alignOn(rightBorder);
//        links.addAll(Linker.linkTo(leftBorder, rightBorder));
//        List<Face> newFaces = Linker.exportLinks(links, mitoGauche.getVertices().size());
//        for(Vertex vertex : mitoGauche.getVertices()) {
//            System.out.println(vertex.toString());
//        }
//        for(Vertex vertex : mitoDroite.getVertices()) {
//            System.out.println(vertex.toString());
//        }
//        for (Face face : mitoGauche.getFaces()) {
//            System.out.println(face.toString());
//        }
//        for (Face face : mitoDroite.getFaces()) {
//            System.out.println(face.toIncrementString(mitoGauche.getVertices().size()));
//        }
//        for(Face face : newFaces) {
//            System.out.println(face.toString());
//        }
//    }


}
