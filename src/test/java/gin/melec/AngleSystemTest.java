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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class AngleSystemTest {

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
        AngleSystem system = new AngleSystem(origin, reference);
        double expResult = 180.0;
        double result = system.getAngle(unknown);
        assertEquals(expResult, result, 0);
        
        // Second test : really easy too
        origin = new Vertex(0, 0f, 0f, 0f);
        reference = new Vertex(1, 0f, -2f, 0f);
        unknown = new Vertex(2, -1f, 0f, 0f);
        system = new AngleSystem(origin, reference);
        expResult = 270.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0);
        
        // Third test : the unknown vertex is not on the same plan.
        origin = new Vertex(0, 0f, 0f, 0f);
        reference = new Vertex(1, 0f, -2f, 0f);
        unknown = new Vertex(2, -1f, 0f, 2f);
        system = new AngleSystem(origin, reference);
        expResult = 270.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0);
        
        // Fourth test : the unknown vertex is not on the same plan.
        origin = new Vertex(0, 0f, 0f, 0f);
        reference = new Vertex(1, 1f, 2f, 0f);
        unknown = new Vertex(2, -2f, 1f, 5f);
        system = new AngleSystem(origin, reference);
        expResult = 270.0;
        result = system.getAngle(unknown);
        assertEquals(expResult, result, 0);
        
        // Last test : real condition with situation that could not work :
        origin = new Vertex(0, 101.264f, 151.232f, 202.285f);
        reference = new Vertex(1, 101.383f, 151.656f, 202.637f);
        Vertex unknown1 = new Vertex(2, 101.127f, 150.509f, 201.720f);        
        Vertex unknown2 = new Vertex(2, 101.203f, 151.381f, 202.253f);
        system = new AngleSystem(origin, reference);
        double result1 = system.getAngle(unknown1);
        double result2 = system.getAngle(unknown2);
        assertTrue(result1 < result2);
    }

}
