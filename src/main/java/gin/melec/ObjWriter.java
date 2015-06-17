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

    /**
     * Replace the given mesh by the new mesh contained in the array.
     * @param meshPath , the path of the old mesh.
     * @param mesh , the array containing the new mesh.
     * @throws IOException , thrown by the writer.
     */
    public static void replaceMesh(final String meshPath, final List mesh)
            throws IOException {
        final FileWriter fiW = new FileWriter(meshPath);
        final BufferedWriter bfW = new BufferedWriter(fiW);
        final PrintWriter prW = new PrintWriter(bfW);

        for (Object mesh1 : mesh) {
            final String[] content = (String[]) mesh1;
            String line = "";
            for (String word : content) {
                line += word + " ";
            }
            line += "\n";
            prW.write(line);
        }
        prW.close();
    }

}
