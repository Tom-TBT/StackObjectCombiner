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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tom
 */
public class FlatBorder {

    private final List elements;

    public FlatBorder() {
        this.elements = new ArrayList();
    }

    public void addElement(Object element) {
        if (element instanceof List) {
            List listElement = (List)element;
            if (listElement.get(0) instanceof Vertex) {
                this.elements.add(element);
            }
        } else if (element instanceof Vertex) {
            this.elements.add(element);
        }
    }

    public void printBorder() {
        for (Object obj: this.elements) {
            if (obj instanceof List) {
                List<Vertex> list = (List) obj;
                for (Vertex vertex: list) {
                    System.out.println(vertex);
                }
            } else {
                System.out.println(obj);
            }
        }
    }
}
