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
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class ObjWriter {

    /**
     * A private constructor because this is an utilitary class and it should
     * never be instanciate.
     */
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
     *
     * @param path , the path of the mesh.
     * @param mesh , the mesh to write.
     * @throws IOException , thrown by the writer.
     */
    public static void writeMesh(final File file, final Mesh mesh)
            throws IOException {
        final FileWriter fiW = new FileWriter(file.toString());
        final BufferedWriter bfW = new BufferedWriter(fiW);
        final PrintWriter prW = new PrintWriter(bfW);
        try {
            if (mesh.isMoved()) {
                prW.write("#movedBySOC\n");
            }
            for (Object element : mesh.getVertices()) {
                prW.write(element.toString() + "\n");
            }
            for (Object element : mesh.getFaces()) {
                prW.write(element.toString() + "\n");
            }
        } finally {
            prW.close();
        }
    }

    /**
     * A method to write borders in a file.
     *
     * @param filePath , the path to the file to write.
     * @param borders , the borders to write.
     * @throws IOException if their is an error while writing.
     */
    static void serializeBorders(final File file, final List<Border> borders)
            throws IOException {
        final ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(file.toString()));
        try {
            oos.writeObject(borders);
        } finally {
            oos.close();
        }

    }
}
