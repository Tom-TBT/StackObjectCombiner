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
public class AngleSystem {

    /**
     * Tolerance is the value for the plane and line for which, under this
     * value, two points are the same.
     */
    private static final double TOLERANCE = 0.001;
    /**
     * Constante used to convert angles in radian to angles in degree. Easier to
     * understand.
     */
    private static final double CONVERT_CSTE = 180 / Math.PI;

    /**
     * Vector which situate the origin of the system.
     */
    final private Vector3D vectOrig;

    /**
     * Vector which situate the reference of the system, relatively to the
     * origin point.
     */
    final private Vector3D vectOrigToRef;
    /**
     * Vector which situate the virtual point of the system, relatively to the
     * origin point.
     */
    final private Vector3D vectOrigToVirt;
    /**
     * Vector which situate the unknown point of the system, relatively to the
     * origin point.
     */
    private Vector3D vectOrigToUnkn;

    /**
     * The plane created to compare the angles. It is created with the origin,
     * the reference and the virtual point.
     */
    final private Plane plane;
    /**
     * The angle on the plane, between the reference and the virtual point at
     * the origin.
     */
    final private double angleRefVirt;
    /**
     * The angle on the plane, between the reference and the unknow point at
     * the origin.
     */
    private double angleRefUnkn;
    /**
     * The angle on the plane, between the unknown point and the virtual point
     * at the origin.
     */
    private double angleVirtUnkn;

    /**
     * Public constructor for an AngleSystem. This object provide a plane to
     * compare angles between points for which we want to determine the position
     * in this system. The reference give a direction to this system.
     * @param origin , the origin point of the system.
     * @param reference , the reference point of the system.
     * @param virtual, the virtual point of the system.
     */
    public AngleSystem(final Vertex origin, final Vertex reference,
            final Vertex virtual) {
        this.vectOrig = new Vector3D(origin.x, origin.y, origin.z);
        final Vector3D vectRef = new Vector3D(reference.x, reference.y,
                reference.z);

        final Vector3D vectVirt = new Vector3D(virtual.x, virtual.y, virtual.z);

        // Ref and virtual point coordonates are changed to correspond with the
        // origin point as... origin.
        this.vectOrigToRef = new Vector3D(vectRef.getX() - vectOrig.getX(),
                                  vectRef.getY() - vectOrig.getY(),
                                  vectRef.getZ() - vectOrig.getZ());
        this.vectOrigToVirt = new Vector3D(vectVirt.getX() - vectOrig.getX(),
                                  vectVirt.getY() - vectOrig.getY(),
                                  vectVirt.getZ() - vectOrig.getZ());

        this.plane = new Plane(new Vector3D(0, 0, 0), this.vectOrigToRef,
                this.vectOrigToVirt, TOLERANCE);

        this.angleRefVirt = Vector3D.angle(this.vectOrigToRef,
                this.vectOrigToVirt) * CONVERT_CSTE;
    }

    /**
     * Give the angle between the unknown point and the reference point at the
     * origin. The angle is oriented.
     * @param unknown , the vertex for which we want the angle.
     * @return , the angle of the unknown vertex.
     */
    public final double getAngle(final Vertex unknown) {
        this.vectOrigToUnkn = new Vector3D(unknown.x - vectOrig.getX(),
                                  unknown.y - vectOrig.getY(),
                                  unknown.z - vectOrig.getZ());

        this.vectOrigToUnkn = this.getNormalizedVector();

        this.angleRefUnkn = Vector3D.angle(this.vectOrigToUnkn,
                this.vectOrigToRef) * CONVERT_CSTE;
        this.angleVirtUnkn = Vector3D.angle(this.vectOrigToUnkn,
                this.vectOrigToVirt) * CONVERT_CSTE;

        double result;
        final double addAngle1 = angleVirtUnkn + angleRefVirt;
        final double addAngle2 = angleVirtUnkn + angleRefUnkn;
        // Four different conformations for the angles (take a paper and try at
        // home !). Two in the if and two other in the else.
        if ((addAngle1 - 1 < angleRefUnkn && angleRefUnkn < addAngle1 + 1)
                || (addAngle2 - 1 < angleRefVirt && angleRefVirt
                < addAngle2 + 1)) {
            // In this case, the unknown point form an "as we want" acute angle
            // with the reference.
            result = angleRefUnkn;
        }
        else {
            result = 360 - angleRefUnkn;
        }
        return result;
    }

    /**
     * This method give the unknown point orthogonal projection on the plane.
     * @return the orthogonal projection of the unknown point on the plane.
     */
    private Vector3D getNormalizedVector() {
        final Vector3D normal = this.plane.getNormal();
        final Line line = new Line(this.vectOrigToUnkn,
                this.vectOrigToUnkn.add(normal), TOLERANCE);
        return plane.intersection(line);
    }
}
