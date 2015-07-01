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
public class LimiterTest {

    public LimiterTest() {
    }

    /**
     * Test of makeVerticalBorder method, of class Limiter.
     */
    @Test
    public void testMakeVerticalBorder() throws IOException {
        System.out.println("makeVerticalBorder");
        int splitPosition = 103;
        Mesh mesh = ObjReader.readBorderMesh(
                "./src/test/java/gin/melec/MeshForTests/A_Mito_Gauche.obj", splitPosition, 1);
        mesh.doNeighborhood();
        Limiter.makeVerticalBorder(mesh, splitPosition);
        int i;
        for (i = 0; i < mesh.currentBorder.vertexSequence.size(); i++) {
            Vertex v = (Vertex) mesh.currentBorder.vertexSequence.get(i);
            System.out.println(v.toString());
        }
        System.out.println(i);
    }

}
