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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class ObjReader {

    /**
     * A private constructor because this is an utilitary class and it should
     * never be instanciate.
     */
    private ObjReader() {
    }

    public static ObjReader getInstance() {
        return ObjReaderHolder.INSTANCE;
    }

    private static class ObjReaderHolder {
        private static final ObjReader INSTANCE = new ObjReader();
    }

    /**
     * Read a .obj file and make from it a mesh (vertices + faces).
     * @param path , the path of the file .obj
     * @param vertices , the list in which the method put the readed vertices.
     * @param faces , the list in which the method put the readed faces.
     * @throws IOException , when their is an error with the lecture of the file
     */
    public static void readMesh(final String path, final Set vertices,
            final Set faces) throws IOException {
        final InputStream ips = new FileInputStream(path);
        final InputStreamReader ipsr = new InputStreamReader(ips);
        final BufferedReader buR = new BufferedReader(ipsr);

        String currentLine;
        String[] splitedLine;
        int id = 1;

        while ((currentLine = buR.readLine()) != null) {
            splitedLine = currentLine.split(" ");
            if (splitedLine[0].equals("v")) {
                vertices.add(new Vertex(id, Float.parseFloat(splitedLine[1]),
                        Float.parseFloat(splitedLine[2]),
                        Float.parseFloat(splitedLine[3])));
                id++;
            }
            else if (splitedLine[0].equals("f")) {
                faces.add(new Face(Integer.parseInt(splitedLine[1]),
                        Integer.parseInt(splitedLine[2]),
                        Integer.parseInt(splitedLine[3])));
            }
        }
        buR.close();
    }

    /**
     * Read a border file.
     * @param path , the path of the file describing the border.
     * @return a list of vertices making the border.
     * @throws IOException if their is an error while reading the file.
     */
    public static List readBorders(final String path) throws IOException {
        final List<Border> borders = new ArrayList();

        final InputStream ips = new FileInputStream(path);
        final InputStreamReader ipsr = new InputStreamReader(ips);
        final BufferedReader buR = new BufferedReader(ipsr);

        String currentLine;
        String[] splitedLine;
        boolean isFirstLine = true;
        Border border = new Border();

        while ((currentLine = buR.readLine()) != null) {
            splitedLine = currentLine.split(" ");
            if (splitedLine[0].equals("b") && isFirstLine) {
                //border.isCircular = splitedLine[2].equals("circular");
            }
            else if (splitedLine[0].equals("b") && !isFirstLine) {
                borders.add(border);
                border = new Border();
                //border.isCircular = splitedLine[2].equals("circular");
                isFirstLine = true;
            }
            else if (splitedLine[0].equals("v")) {
                final Vertex v = new Vertex(Integer.parseInt(splitedLine[4]),
                Float.parseFloat(splitedLine[1]),
                Float.parseFloat(splitedLine[2]),
                Float.parseFloat(splitedLine[3]));
                border.addNextVertex(v);
                isFirstLine = false;
            }
        }
        buR.close();

        borders.add(border);
        return borders;
    }
}
