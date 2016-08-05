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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    static ObjReader getInstance() {
        return ObjReaderHolder.INSTANCE;
    }

    private static class ObjReaderHolder {

        private static final ObjReader INSTANCE = new ObjReader();
    }

    /**
     * Read a .obj file and make from it a mesh (vertices + faces).
     *
     * @param file , the file of the file .obj
     * @param mesh , the mesh to load.
     * @throws IOException , when their is an error with the lecture of the file
     * @throws java.text.ParseException if their is an error while parsing the
     * values of the vertices.
     */
    static void readMesh(final File file, final Mesh mesh)
            throws IOException, ParseException {
        CustomFrame.appendToLog("Loading "+mesh.getFile().getName());
        final List<Vertex> tmpVertices;

        final InputStream ips = new FileInputStream(file.toString());
        final InputStreamReader ipsr = new InputStreamReader(ips);
        final BufferedReader buR = new BufferedReader(ipsr);
        try{
            NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
            tmpVertices = mesh.getVertices();
            String currentLine;
            String[] splitedLine;
            int id = 1;
            while ((currentLine = buR.readLine()) != null) {
                splitedLine = currentLine.split(" ");
                if (splitedLine[0].equals("v")) {
                    if (id == 1) {
                        if (splitedLine[1].contains(",")) {
                            format = NumberFormat.getInstance(Locale.FRANCE);
                        }
                    }
                    tmpVertices.add(new Vertex(id,
                            format.parse(splitedLine[1]).floatValue(),
                            format.parse(splitedLine[2]).floatValue(),
                            format.parse(splitedLine[3]).floatValue()));
                    id++;
                } else if (splitedLine[0].equals("f")) {
                    Vertex v1 = tmpVertices.get(
                                    Integer.parseInt(splitedLine[1]) - 1);
                    Vertex v2 = tmpVertices.get(
                                    Integer.parseInt(splitedLine[2]) - 1);
                    Vertex v3 = tmpVertices.get(
                                    Integer.parseInt(splitedLine[3]) - 1);
                    Face face = new Face(v1, v2, v3);
                    mesh.getFaces().add(face);
                    v1.addUnique(v2, v3);
                    v2.addUnique(v1, v3);
                    v3.addUnique(v1, v2);
                }
            }
        } finally {
            buR.close();
        }
    }

    /**
     * Check if the mesh has already been moved by the plugin.
     * @param file , the file of the mesh.
     * @return true if the mesh has already been moved by the plugin.
     * @throws FileNotFoundException if the file does not exist.
     * @throws IOException if their is an error of lecture.
     */
    static boolean isMeshMoved(final File file)
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
     * Check if the mesh has already been moved by the plugin.
     * @param file , the file of the mesh.
     * @return true if the mesh has already been moved by the plugin.
     * @throws FileNotFoundException if the file does not exist.
     * @throws IOException if their is an error of lecture.
     */
    static boolean isMeshMerged(final File file)
            throws FileNotFoundException, IOException {
        String currentLine;
        boolean result = false;

        final InputStream ips = new FileInputStream(file.toString());
        final InputStreamReader ipsr = new InputStreamReader(ips);
        final BufferedReader buR = new BufferedReader(ipsr);
        try {
            while ((currentLine = buR.readLine()) != null) {
                if (currentLine.contains("Merged")) {
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
     * Check if the mesh has already been moved by the plugin.
     * @param file , the file of the mesh.
     * @return true if the mesh has already been moved by the plugin.
     * @throws FileNotFoundException if the file does not exist.
     * @throws IOException if their is an error of lecture.
     */
    static double[] getShift(final File file)
            throws FileNotFoundException, IOException {
        String currentLine;
        double result[] = new double[3];

        final InputStream ips = new FileInputStream(file.toString());
        final InputStreamReader ipsr = new InputStreamReader(ips);
        final BufferedReader buR = new BufferedReader(ipsr);
        try {
            while ((currentLine = buR.readLine()) != null) {
                if (currentLine.contains("#movedBySOC")) {
                    int xIndex = currentLine.indexOf("X:");
                    int yIndex = currentLine.indexOf("Y:");
                    int zIndex = currentLine.indexOf("Z:");
                    String tmpString = currentLine.substring(xIndex+2, yIndex - 1);
                    result[0] = Double.parseDouble(tmpString);
                    tmpString = currentLine.substring(yIndex+2, zIndex - 1);
                    result[1] = Double.parseDouble(tmpString);
                    tmpString = currentLine.substring(zIndex+2, currentLine.length());
                    result[2] = Double.parseDouble(tmpString);
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
    static List deserializeBorders(final File file) throws IOException {
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
