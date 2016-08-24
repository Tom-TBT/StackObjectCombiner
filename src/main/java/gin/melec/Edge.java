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
public class Edge {
    private final boolean inverted;

    private final Vertex origin;

    private final Vertex end;

    private final char axe;

    private final List<Connector> connectors;

    public Edge(final boolean inverted, final Vertex origin, final Vertex end, final char axe) {
        this.inverted = inverted;
        this.origin = origin;
        this.end = end;
        this.axe = axe;
        this.connectors = new ArrayList();
    }

    /**
     * Place the connector on the edge. The connectors of the edges are ordered
     * switch their position.
     * @param connector, the connector to add.
     */
    public final void addConnector(final Connector connector) {
        if (this.connectors.isEmpty()) {
            this.connectors.add(connector);
        } else {
            for (int i = 0; i < this.connectors.size(); i++) {
                Connector c = this.connectors.get(i);
                if (this.isCloser(connector,c)) {
                    this.connectors.add(i,connector);
                    break;
                } else if (i == this.connectors.size() - 1) {
                    this.connectors.add(i+1, connector);
                }
            }
        }

    }

    /**
     * Check if the connector 1 is closer than the connector 2 according to the
     * edge.
     * @param c1, the first connector.
     * @param c2, the second connector.
     * @return true is c1 is closer than c2.
     */
    private boolean isCloser(final Connector c1, final Connector c2) {
        boolean result = false;
        switch (axe){
            case 'X':
                result = c1.getX() < c2.getX();
                break;
            case 'Y':
                result = c1.getY() < c2.getY();
                break;
            case 'Z':
                result = c1.getZ() < c2.getZ();
                break;
        }
        if (this.inverted) {
            result = !result;
        }
        return result;
    }

    public final Connector getFirst() {
        if(!this.connectors.isEmpty()) return this.connectors.get(0);
        else return null;
    }

    public final Vertex getNext(final Vertex previous) {
        Vertex result = null;
        for (int i = 0; i < this.connectors.size(); i++) {
            Connector conn = this.connectors.get(i);
            if (this.isCloser(conn, new Connector(previous, null, null))) {
                result = conn;
                break;
            }
        }
        if (result == null) {
            if (inverted) {
                result = end;
            } else {
                result = origin;
            }
        }
        return result;
    }

    public void removeConnector(Connector connector) {
        this.connectors.remove(connector);
    }
}
