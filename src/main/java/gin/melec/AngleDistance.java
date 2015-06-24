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
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class AngleDistance {

    private final static double TOLERANCE = 0.001;

    private final static double SCALE = 1;

    private static Vector3D VECT_ORIG;

    private static Vector3D VECT_REF;

    private static Vector3D VECT_UNKN;

    private static Vector3D VECT_VIRT;

    private static Vector3D VECT_OR_RE;

    private static Vector3D VECT_OR_UN;

    private static Vector3D VECT_OR_VI;

    private AngleDistance() {
    }

    public static AngleDistance getInstance() {
        return AngleDistanceHolder.INSTANCE;
    }

    private static class AngleDistanceHolder {

        private static final AngleDistance INSTANCE = new AngleDistance();
    }

    public static double getAngle(final Vertex origin,
            final Vertex reference, final Vertex unknown) {

        // Creation of vector with the vertex given.
        VECT_ORIG = new Vector3D(origin.x, origin.y, origin.z);
        VECT_REF = new Vector3D(reference.x, reference.y, reference.z);
        VECT_UNKN = new Vector3D(unknown.x, unknown.y, unknown.z);

        // Creation of the plane.
        final Plane plane = new Plane(VECT_ORIG, VECT_REF, VECT_UNKN,
                TOLERANCE);

        // TODO Adapt this to the different orientations
        final Vector3D vectVirt1 = new Vector3D(VECT_ORIG.getX() + 1,
                VECT_ORIG.getY(), VECT_ORIG.getZ());
        final Vector3D vectVirt2 = new Vector3D(VECT_ORIG.getX() + 1,
                VECT_ORIG.getY(), VECT_ORIG.getZ() + 1);

        // Creation of a line shifted in x or y (switch case).
        final Line line = new Line(vectVirt1, vectVirt2, TOLERANCE);
        // Get vector of the point of the lane crossing the plane.
        VECT_VIRT = plane.intersection(line);

        // Creation of the vectors for which we want the angles
        VECT_OR_RE = new Vector3D(VECT_REF.getX() - VECT_ORIG.getX(),
                                  VECT_REF.getY() - VECT_ORIG.getY(),
                                  VECT_REF.getZ() - VECT_ORIG.getZ());
        VECT_OR_UN = new Vector3D(VECT_UNKN.getX() - VECT_ORIG.getX(),
                                  VECT_UNKN.getY() - VECT_ORIG.getY(),
                                  VECT_UNKN.getZ() - VECT_ORIG.getZ());
        VECT_OR_VI = new Vector3D(VECT_VIRT.getX() - VECT_ORIG.getX(),
                                  VECT_VIRT.getY() - VECT_ORIG.getY(),
                                  VECT_VIRT.getZ() - VECT_ORIG.getZ());

        // Angle Reference, Origin, Virtual
        double angleROV = Vector3D.angle(VECT_OR_RE, VECT_OR_VI);
        // Angle Virtual, Origin, Unknown
        double angleVOU = Vector3D.angle(VECT_OR_VI, VECT_OR_UN);
        // Angle Reference, Origin, Unknown
        double angleROU = Vector3D.angle(VECT_OR_RE, VECT_OR_UN);

        double result;
        if (angleROV - angleVOU == angleROU) {
            result = 360 - angleROU;
        }
        else {
            result = angleROV + angleVOU;
        }
        return result * 180 / Math.PI;
    }
}
