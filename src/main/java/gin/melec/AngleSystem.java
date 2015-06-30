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

    private static final double TOLERANCE = 0.001;

    private static final double SCALE = 1;

    private static final double CONVERT_CSTE = 180 / Math.PI;

    private Vector3D vectOrig;

    private Vector3D vectRef;

    private Vector3D vectUnkn;

    private Vector3D vectVirt;

    private Vector3D vectOrigRef;

    private Vector3D vectOrigUnkn;

    private Vector3D vectOrigVirt;
    
    private Plane plane;
    
    private double angleRefVirt;
    
    private double angleRefUnkn;
    
    private double angleVirtUnkn;

    public AngleSystem(final Vertex origin, final Vertex reference) {
        this.vectOrig = new Vector3D(origin.x, origin.y, origin.z);
        this.vectRef = new Vector3D(reference.x, reference.y, reference.z);
        this.vectVirt = new Vector3D(origin.x + 1, origin.y, origin.z);
        
        this.vectOrigRef = new Vector3D(vectRef.getX() - vectOrig.getX(),
                                  vectRef.getY() - vectOrig.getY(),
                                  vectRef.getZ() - vectOrig.getZ());
        this.vectOrigVirt = new Vector3D(vectVirt.getX() - vectOrig.getX(),
                                  vectVirt.getY() - vectOrig.getY(),
                                  vectVirt.getZ() - vectOrig.getZ());
        
        this.plane = new Plane(new Vector3D(0, 0, 0), this.vectOrigRef,
                this.vectOrigVirt, TOLERANCE);
        
        this.angleRefVirt = Vector3D.angle(this.vectOrigRef, this.vectOrigVirt)
                * CONVERT_CSTE;
    }
    
    public double getAngle(final Vertex unknown) {
        this.vectUnkn = new Vector3D(unknown.x, unknown.y, unknown.z);

        this.vectOrigUnkn = new Vector3D(vectUnkn.getX() - vectOrig.getX(),
                                  vectUnkn.getY() - vectOrig.getY(),
                                  vectUnkn.getZ() - vectOrig.getZ());
        
        
        this.vectOrigUnkn = this.getNormalizedVector();

        this.angleRefUnkn = Vector3D.angle(this.vectOrigUnkn, 
                this.vectOrigRef) * CONVERT_CSTE;
        this.angleVirtUnkn = Vector3D.angle(this.vectOrigUnkn, 
                this.vectOrigVirt) * CONVERT_CSTE;
        
        double result;
        double addAngle = angleVirtUnkn + angleRefVirt;
        if ( addAngle - 1 < angleRefUnkn && angleRefUnkn < addAngle + 1) {
            result = angleRefUnkn;
        }
        else {
            result = 360 - angleRefUnkn;
        }
        return result;
    }
    
    private Vector3D getNormalizedVector() {
        Vector3D normal = this.plane.getNormal();
        Vector3D tmpVect = this.vectOrigUnkn.add(normal);
        Line line = new Line(this.vectOrigUnkn, tmpVect, TOLERANCE);
        Vector3D vect = plane.intersection(line);
        return vect;
    }
}
