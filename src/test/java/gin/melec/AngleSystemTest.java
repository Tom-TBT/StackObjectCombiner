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

    Split splitLeft = new SplitLeft(20);
    Split splitUp = new SplitUp(20);
    Split splitRight = new SplitRight(20);
    Split splitDown = new SplitDown(20);

    public AngleSystemTest() {
    }
    /**
     * Test of getAngle method, of class AngleSystem.
     */
    @Test
    public void testGetAngle() {
        System.out.println("getAngle");

        Vertex origin = new Vertex(0, 0f, 0f, 0f);
        Vertex reference = new Vertex(1, 0f, -2f, 0f);
        Vertex unknown = new Vertex(2, 0f, 1f, 0f);
        AngleSystem system = new AngleSystem(origin, reference, splitLeft.createVirtual(origin));
        double expResult = 180.0;
        double result = system.getAngle(unknown);
        assertEquals(expResult, result, 0.001);

        origin = new Vertex(0, 0f, 0f, 0f);
        reference = new Vertex(1, 0f, -2f, 0f);
        unknown = new Vertex(0, 1f, 0f, 0f);
        system = new AngleSystem(origin, reference, splitLeft.createVirtual(origin));
        expResult = 270.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0.001);

        origin = new Vertex(0, 0f, 0f, 0f);
        reference = new Vertex(1, 0f, -2f, 0f);
        unknown = new Vertex(2, 1f, 1f, 0f);
        system = new AngleSystem(origin, reference, splitLeft.createVirtual(origin));
        expResult = 225.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0.001);

        origin = new Vertex(0, 0f, 0f, 0f);
        reference = new Vertex(1, 0f, -2f, 0f);
        unknown = new Vertex(2, 1f, -1f, 0f);
        system = new AngleSystem(origin, reference, splitLeft.createVirtual(origin));
        expResult = 315.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0.001);

        origin = new Vertex(0, 0f, 0f, 0f);
        reference = new Vertex(1, 0f, -2f, 0f);
        unknown = new Vertex(2, -1f, -1f, 0f);
        system = new AngleSystem(origin, reference, splitLeft.createVirtual(origin));
        expResult = 45.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0.001);

        origin = new Vertex(0, 0f, 0f, 0f);
        reference = new Vertex(1, 0f, -2f, 0f);
        unknown = new Vertex(2, -1f, 1f, 0f);
        system = new AngleSystem(origin, reference, splitLeft.createVirtual(origin));
        expResult = 135.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0.001);

        origin = new Vertex(0, 0f, 0f, 0f);
        reference = new Vertex(1, 0f, -2f, 0f);
        unknown = new Vertex(2, -1f, 1f, 0f);
        system = new AngleSystem(origin, reference, splitRight.createVirtual(origin));
        expResult = 225.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0);

        origin = new Vertex(0, 0f, 0f, 0f);
        reference = new Vertex(1, -2f, 0f, 0f);
        unknown = new Vertex(2, 1f, -1f, 0f);
        system = new AngleSystem(origin, reference, splitUp.createVirtual(origin));
        expResult = 135.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0);

        origin = new Vertex(0, 0f, 0f, 0f);
        reference = new Vertex(1, -2f, 0f, 0f);
        unknown = new Vertex(2, 1f, -1f, 0f);
        system = new AngleSystem(origin, reference, splitDown.createVirtual(origin));
        expResult = 225.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0);
    }

}
