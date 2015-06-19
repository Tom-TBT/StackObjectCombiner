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

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Shifter {

    private Shifter() {
    }

    public static Shifter getInstance() {
        return ShifterHolder.INSTANCE;
    }

    private static class ShifterHolder {
        private static final Shifter INSTANCE = new Shifter();
    }

    /**
     * Translate the mesh given by its path by a shift given in the x direction.
     * @param meshPath , the path of the mesh translated.
     * @param shift , the shift applied to the mesh.
     * @throws IOException , thrown by the ObjReader.
     */
    public static void xTranslation(final String meshPath, final int shift)
            throws IOException {
        final Mesh mesh = ObjReader.readMesh(meshPath);
        for (Object object : mesh.vertices) {
            final Vertex vertex = (Vertex) object;
            vertex.x = vertex.x + shift;
        }
        ObjWriter.replaceMesh(meshPath, mesh);
    }

    /**
     * Translate the mesh given by its path by a shift given in the y direction.
     * @param meshPath , the path of the mesh translated.
     * @param shift , the shift applied to the mesh.
     * @throws IOException , thrown by the ObjReader.
     */
    public static void yTranslation(final String meshPath, final int shift)
            throws IOException {
        final Mesh mesh = ObjReader.readMesh(meshPath);
        for (Object object : mesh.vertices) {
            final Vertex vertex = (Vertex) object;
            vertex.x = vertex.y + shift;
        }
        ObjWriter.replaceMesh(meshPath, mesh);
    }

    /**
     * Translate the mesh given by its path by a shift given in the x and y
     * direction.
     * @param meshPath , the path of the mesh translated.
     * @param shiftX , the shift applied to the mesh in x.
     * @param shiftY , the shift applied to the mesh in y.
     * @throws IOException , thrown by the ObjReader.
     */
    public static void xYTranslation(final String meshPath, final int shiftX,
            final int shiftY)
            throws IOException {
        final Mesh mesh = ObjReader.readMesh(meshPath);
        for (Object object : mesh.vertices) {
            final Vertex vertex = (Vertex) object;
            vertex.x = vertex.x + shiftX;
            vertex.x = vertex.y + shiftY;
        }
        ObjWriter.replaceMesh(meshPath, mesh);
    }


}
