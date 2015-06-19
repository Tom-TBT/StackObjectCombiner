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
public class Limiter {

    /**
     * The value to compare for a vertical limit will be in x (1 in the table).
     */
    private static final int VERTICAL_LIMIT = 1;
    /**
     * The value to compare for a vertical limit will be in y (2 in the table).
     */
    private static final int HORIZONTAL_LIMIT = 2;

    private Limiter() {
    }

    public static Limiter getInstance() {
        return LimiterHolder.INSTANCE;
    }

    private static class LimiterHolder {

        private static final Limiter INSTANCE = new Limiter();
    }

    public static void findVerticalBorder(final String meshPath,
            final int borderPosition) throws IOException {
        final Mesh mesh = ObjReader.readBorderMesh(meshPath, borderPosition,
                VERTICAL_LIMIT);
        ObjWriter.replaceMesh(meshPath+"Bordered", mesh);
    }

    public static void findHorizontalBorder(final String meshPath,
            final int borderPosition) throws IOException {
        final Mesh mesh = ObjReader.readBorderMesh(meshPath, borderPosition,
                HORIZONTAL_LIMIT);
        ObjWriter.replaceMesh(meshPath+"Bordered", mesh);
    }


}
