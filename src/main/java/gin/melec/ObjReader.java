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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class ObjReader {

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
     * @param meshName , the name of the file .obj
     * @return , a mesh made from the file.
     * @throws IOException , when their is an error with the lecture of the file
     */
    public static void readMesh(final String path, final List vertices,
            final List faces) throws IOException {
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

    public static List readBorders(String path) throws FileNotFoundException, IOException {
        List borders = new ArrayList();

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
                border.isCircular = splitedLine[2].equals("circular");
            }
            else if (splitedLine[0].equals("b") && !isFirstLine) {
                borders.add(border);
                border = new Border();
                border.isCircular = splitedLine[2].equals("circular");
            }
            else if(splitedLine[0].equals("v")) {
                Vertex v = new Vertex(Integer.parseInt(splitedLine[4]),
                Float.parseFloat(splitedLine[1]),
                Float.parseFloat(splitedLine[2]),
                Float.parseFloat(splitedLine[3]));
                if(isFirstLine) {
                    border.firstVertex = v;
                }
                border.vertexSequence.add(v);
                isFirstLine = false;
            }
        }
        borders.add(border);
        return borders;
    }
}
