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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     *
     * @param path , the path of the file .obj
     * @param mesh , the mesh to load.
     * @throws IOException , when their is an error with the lecture of the file
     */
    public static void readMesh(final File file, final Mesh mesh)
            throws IOException {
        final List<Vertex> tmpVertices;

        final InputStream ips = new FileInputStream(file.toString());
        final InputStreamReader ipsr = new InputStreamReader(ips);
        final BufferedReader buR = new BufferedReader(ipsr);
        try{
            tmpVertices = new ArrayList();
            String currentLine;
            String[] splitedLine;
            int id = 1;
            while ((currentLine = buR.readLine()) != null) {
                splitedLine = currentLine.split(" ");
                if (splitedLine[0].equals("v")) {
                    tmpVertices.add(new Vertex(id, Float.parseFloat(splitedLine[1]),
                            Float.parseFloat(splitedLine[2]),
                            Float.parseFloat(splitedLine[3])));
                    id++;
                } else if (splitedLine[0].equals("f")) {
                    mesh.getFaces().add(new Face(
                            tmpVertices.get(
                                    Integer.parseInt(splitedLine[1]) - 1),
                            tmpVertices.get(
                                    Integer.parseInt(splitedLine[2]) - 1),
                            tmpVertices.get(
                                    Integer.parseInt(splitedLine[3]) - 1)));
                }
            }
        } finally {
            buR.close();
        }
        mesh.getVertices().addAll(tmpVertices);
    }

    public static boolean isMeshMoved(final File file)
            throws FileNotFoundException, IOException {
        String currentLine;
        boolean result = false;

        final InputStream ips = new FileInputStream(file.toString());
        final InputStreamReader ipsr = new InputStreamReader(ips);
        final BufferedReader buR = new BufferedReader(ipsr);
        try {
            while ((currentLine = buR.readLine()) != null) {
                if (currentLine.contains("#movedBySOC")) {
                    result = true;
                    break;
                }
            }
        } finally {
            buR.close();
        }
        return result;
    }

    /**
     * Read a border file.
     *
     * @param path , the path of the file describing the border.
     * @return a list of vertices making the border.
     * @throws IOException if their is an error while reading the file.
     */
    public static List deserializeBorders(final File file) throws IOException {
        List<Border> borders = null;
        final ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file.toString()));
        try {
            borders = (ArrayList) ois.readObject();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ObjReader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ois.close();
        }

        return borders;
    }
}
