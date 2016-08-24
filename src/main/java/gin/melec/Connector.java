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

import java.util.List;

/**
 *
 * @author tom
 */
public class Connector extends Vertex{
    private final Border parentBorder;

    private final Edge nextEdge;

    public Connector(final int i, final float x,  final float y,  final float z,
            Border parent, Edge nextEdge) {
        super(i,x,y,z);
        this.parentBorder = parent;
        this.nextEdge = nextEdge;
    }

    /**
     * Create a connector.
     * A connector indicate to which edge we have to continue the flat border.
     * @param v, the position of the connector.
     * @param parent, the border that created the connector.
     * @param nextEdge , the edge that the connector is pointing at.
     */
    public Connector(final Vertex v, Border parent, Edge nextEdge) {
        super(v.id,v.x,v.y,v.z);
        this.parentBorder = parent;
        this.nextEdge = nextEdge;
    }

    public List<Vertex> getSequence() {
        return this.parentBorder.getVertexSequence();
    }

    public Edge getNextEdge() {
        return this.nextEdge;
    }

    public Vertex getLastVertex() {
        return this.parentBorder.getLastVertex();
    }

    public void setVertex(Vertex v) {
        this.id = v.id;
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
}
