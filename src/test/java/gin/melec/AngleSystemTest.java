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

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class AngleSystemTest {

    /**
     * Constante which indicate that the border is an X border, on the left
     * side.
     */
    private static final char ORI_X_LEFT = 'A';
    /**
     * Constante which indicate that the border is an X border, on the right
     * side.
     */
    private static final char ORI_X_RIGHT = 'B';
    /**
     * Constante which indicate that the border is an X border, on the upper
     * side.
     */
    private static final char ORI_Y_UP = 'C';
    /**
     * Constante which indicate that the border is an X border, on the lower
     * side.
     */
    private static final char ORI_Y_DOWN = 'D';

    public AngleSystemTest() {
    }
    /**
     * Test of getAngle method, of class AngleSystem.
     */
    @Test
    public void testGetAngle() {
        System.out.println("getAngle");

        // First test : really easy
        Vertex origin = new Vertex(0, 0f, 0f, 0f);
        Vertex reference = new Vertex(1, 0f, -2f, 0f);
        Vertex unknown = new Vertex(2, 0f, 1f, 0f);
        AngleSystem system = new AngleSystem(origin, reference, ORI_X_LEFT);
        double expResult = 180.0;
        double result = system.getAngle(unknown);
        assertEquals(expResult, result, 0);

        // Second test : really easy too
        origin = new Vertex(0, 0f, 0f, 0f);
        reference = new Vertex(1, 0f, -2f, 0f);
        unknown = new Vertex(2, -1f, 0f, 0f);
        system = new AngleSystem(origin, reference, ORI_X_LEFT);
        expResult = 270.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0);

        // Third test : the unknown vertex is not on the same plan.
        origin = new Vertex(0, 0f, 0f, 0f);
        reference = new Vertex(1, 0f, -2f, 0f);
        unknown = new Vertex(2, -1f, 0f, 2f);
        system = new AngleSystem(origin, reference, ORI_X_LEFT);
        expResult = 270.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0);

        // Fourth test : the reference vertex don't have same x and z than the
        // origin.
        origin = new Vertex(0, 0f, 0f, 0f);
        reference = new Vertex(1, 1f, 2f, 0f);
        unknown = new Vertex(2, -2f, 1f, 5f);
        system = new AngleSystem(origin, reference, ORI_X_LEFT);
        expResult = 270.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0);

        // Fifth test : the origin don't have 0,0,0 coordonates :
        origin = new Vertex(0, 2f, 2f, 2f);
        reference = new Vertex(1, 3f, 4f, 2f);
        unknown = new Vertex(2, 0f, 3f, 7f);
        system = new AngleSystem(origin, reference, ORI_X_LEFT);
        expResult = 270.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0);

        // Sixth test : Orientation in the X_RIGHT
        origin = new Vertex(0, 2f, 2f, 2f);
        reference = new Vertex(1, 3f, 4f, 2f);
        unknown = new Vertex(2, 0f, 3f, 7f);
        system = new AngleSystem(origin, reference, ORI_X_RIGHT);
        expResult = 90.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0);

        // Seventh test : Orientation in the Y_UP
        origin = new Vertex(0, 2f, 2f, 2f);
        reference = new Vertex(1, 4f, 2f, 2f);
        unknown = new Vertex(2, 2f, 1f, 7f);
        system = new AngleSystem(origin, reference, ORI_Y_UP);
        expResult = 270.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0);

        // Seventh test : Orientation in the Y_DOWN
        origin = new Vertex(0, 2f, 2f, 2f);
        reference = new Vertex(1, 4f, 2f, 2f);
        unknown = new Vertex(2, 2f, 3f, 7f);
        system = new AngleSystem(origin, reference, ORI_Y_UP);
        expResult = 90.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0);
    }

}
