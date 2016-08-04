/*
 * Copyright (C) 2016 ImageJ
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package gin.melec;

import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author tom
 */
public class CustomPlane extends Plane{

    private final AbstractSplit split1;

    private final AbstractSplit split2;

    public CustomPlane(AbstractSplit split1, AbstractSplit split2, Vector3D p1, Vector3D p2, Vector3D p3, double tolerance) throws MathArithmeticException {
        super(p1, p2, p3, tolerance);
        this.split1 = split1;
        this.split2 = split2;
    }

    public AbstractSplit getSplit1() {
        return split1;
    }

    public AbstractSplit getSplit2() {
        return split2;
    }

}
