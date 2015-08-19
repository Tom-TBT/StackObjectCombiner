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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class ObjWriter {

    private ObjWriter() {
    }

    public static ObjWriter getInstance() {
        return ObjWriterHolder.INSTANCE;
    }

    private static class ObjWriterHolder {
        private static final ObjWriter INSTANCE = new ObjWriter();
    }

    /**
     * Replace the given mesh by the new mesh contained in the array.
     * @param meshPath , the path of the old mesh.
     * @param mesh , the array containing the new mesh.
     * @throws IOException , thrown by the writer.
     */
    public static void writeMesh(final String meshPath, final Mesh mesh)
            throws IOException {
        final FileWriter fiW = new FileWriter(meshPath);
        final BufferedWriter bfW = new BufferedWriter(fiW);
        final PrintWriter prW = new PrintWriter(bfW);

        // Better than a toString function in Mesh, because of the memory this
        // will use.
        for (Object element : mesh.vertices) {
            prW.write(element.toString() + "\n");
        }
        for (Object element : mesh.faces) {
            prW.write(element.toString() + "\n");
        }

        prW.close();
    }

    static void writeBorders(String filePath, List borders) throws IOException {

        final FileWriter fiW = new FileWriter(filePath);
        final BufferedWriter bfW = new BufferedWriter(fiW);
        final PrintWriter prW = new PrintWriter(bfW);

        int numBorder = 1;

        for(Object o : borders) {
            Border border = (Border) o;
            prW.write("b " + numBorder);
            if(border.isCircular) {
                prW.write(" circular");
            }
            else {
                prW.write(" linear");
            }
            prW.write("\n");
            for (Object ob : border.vertexSequence) {
                Vertex vertex = (Vertex) ob;
                prW.write(vertex.toIdString() + "\n");
            }
            numBorder++;
        }
        prW.close();
    }

}