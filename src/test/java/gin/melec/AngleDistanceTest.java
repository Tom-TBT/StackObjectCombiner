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
public class AngleDistanceTest {

    public AngleDistanceTest() {
    }
    /**
     * Test of getAngle method, of class AngleDistance.
     */
    @Test
    public void testGetAngle() {
        System.out.println("getAngle");
        Vertex origin = new Vertex(0, 101.415f, 152.708f, 202.787f);
        Vertex reference = new Vertex(1, 101.419f, 153.283f, 202.774f);
        Vertex unknown = new Vertex(2, 101.401f, 152.045f, 202.730f);
        //double expResult = 270.0;
        double result = AngleDistance.getAngle(origin, reference, unknown);
        //assertEquals(expResult, result, 0.0);
        System.out.println(result);
    }

}
