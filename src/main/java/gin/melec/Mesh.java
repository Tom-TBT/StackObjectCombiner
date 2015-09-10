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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Mesh {

    /**
     * Contain the vertices composing this mesh.
     */
    Set vertices;
    /**
     * Contain the faces composing this mesh.
     */
    Set faces;
    /**
     * List of borders of this mesh.
     */
    List borders;

    /**
     * Contain the vertex already used to make a border. This garbage is emptied
     * when a border is formed, and the corresponding vertex are removed from
     * the mesh.
     */
    Set garbage;
    /**
     * Contain the vertex that can initiate a border. Once this set is empty, it
     * means that all the borders have been detected. Else, we create a new one
     * by taking a vertex in the set (method in split). After a detection of a
     * border, the vertex detected are removed from this set.
     */
    Set primers;

    /**
     * Switch the position of the mesh and switch the type of scene, meshes can
     * have up to 4 different splits.
     */
    List splits;

    /**
     * Public constructor for a mesh.
     *
     * @param splits , the splits of the mesh.
     */
    public Mesh(final List splits) {
        this.faces = new HashSet<Face>();
        this.vertices = new HashSet<Vertex>();
        this.garbage = new HashSet();
        this.borders = new ArrayList();
        this.splits = splits;
        this.primers = new HashSet();
    }

    void setFacesToVertex(final Vertex vertex) {
        for (Object element : this.faces) {
            Face face = (Face) element;
            if (face.idVertex1 == vertex.id) {
                vertex.faces.add(face);
                vertex.neighbours.add(findVertex(face.idVertex2));
                vertex.neighbours.add(findVertex(face.idVertex3));
            } else if (face.idVertex2 == vertex.id) {
                vertex.faces.add(face);
                vertex.neighbours.add(findVertex(face.idVertex1));
                vertex.neighbours.add(findVertex(face.idVertex3));
            } else if (face.idVertex3 == vertex.id) {
                vertex.faces.add(face);
                vertex.neighbours.add(findVertex(face.idVertex1));
                vertex.neighbours.add(findVertex(face.idVertex2));
            }
        }
    }

    private int findMaxIdVertex(Set primers) {
        int idMax = 0;
        for (Object obj : this.primers) {
            Vertex vertex = (Vertex) obj;
            if (vertex.id > idMax) {
                idMax = vertex.id;
            }
        }
        return idMax;
    }

    private int findMinIdVertex(Set primers) {
        int idMin = 0;
        for (Object obj : this.primers) {
            Vertex vertex = (Vertex) obj;
            if (idMin == 0 || vertex.id < idMin) {
                idMin = vertex.id;
            }
        }
        return idMin;
    }

    private void doPrimersNeighboors() {
        int minId, maxId;
        minId = findMinIdVertex(this.primers);
        maxId = findMaxIdVertex(this.primers);
        for (Object obj1 : this.faces) {
            final Face face = (Face) obj1;
            final Vertex vertex1;
            final Vertex vertex2;
            final Vertex vertex3;
            if (face.idVertex1 >= minId && face.idVertex1 <= maxId) {
                vertex1 = findVertex(face.idVertex1);
                vertex2 = findVertex(face.idVertex2);
                vertex3 = findVertex(face.idVertex3);
                vertex1.neighbours.add(vertex2);
                vertex1.neighbours.add(vertex3);
                vertex2.neighbours.add(vertex1);
                vertex2.neighbours.add(vertex3);
                vertex3.neighbours.add(vertex1);
                vertex3.neighbours.add(vertex2);
            }
            else if (face.idVertex2 >= minId && face.idVertex2 <= maxId) {
                vertex1 = findVertex(face.idVertex1);
                vertex2 = findVertex(face.idVertex2);
                vertex3 = findVertex(face.idVertex3);
                vertex1.neighbours.add(vertex2);
                vertex1.neighbours.add(vertex3);
                vertex2.neighbours.add(vertex1);
                vertex2.neighbours.add(vertex3);
                vertex3.neighbours.add(vertex1);
                vertex3.neighbours.add(vertex2);
            }
            else if (face.idVertex3 >= minId && face.idVertex3 <= maxId) {
                vertex1 = findVertex(face.idVertex1);
                vertex2 = findVertex(face.idVertex2);
                vertex3 = findVertex(face.idVertex3);
                vertex1.neighbours.add(vertex2);
                vertex1.neighbours.add(vertex3);
                vertex2.neighbours.add(vertex1);
                vertex2.neighbours.add(vertex3);
                vertex3.neighbours.add(vertex1);
                vertex3.neighbours.add(vertex2);
            }
        }
    }

    private Vertex findInPrimers(final int idVertex) {
        Vertex result = null;
        for (Object obj : this.primers) {
            final Vertex vertex = (Vertex) obj;
            if (vertex.id == idVertex) {
                result = vertex;
                break;
            }
        }
        return result;
    }

    final Vertex findVertex(int idVertex) {
        for (Object obj : vertices) {
            Vertex vertex = (Vertex) obj;
            if (vertex.id == idVertex) {
                return vertex;
            }
        }
        return null;
    }

    /**
     * Once a border is finish, this method is called to add all vertex related
     * to the border to the garbage.
     */
    final void completeGarbage() {
        final List vertexToCheck = new ArrayList(this.garbage);
        for (Object element : vertexToCheck) {
            final Vertex vertex = (Vertex) element;
            vertex.addNeighborToGarbage(this.garbage);
        }
    }

    final Vertex findNextVertex(final Border border) {
        Vertex nextVertex = null;
        Set facesRemaining = new HashSet();
        Set verticesRemaining = new HashSet();
        facesRemaining.addAll(border.lastVertexAdded.faces);
        verticesRemaining.addAll(border.lastVertexAdded.neighbours);
        Boolean notFoundYet = true;

        Face currentFace = null;
        // Search the face, the two last vertex added to the border, have in
        // in common
        for (Object obj1 : border.lastVertexAdded.faces) {
            Face face1 = (Face) obj1;
            for (Object obj2 : border.scndLastVertexAdded.faces) {
                Face face2 = (Face) obj2;
                if (face2 == face1) {
                    currentFace = face1;
                    facesRemaining.remove(face1);
                    verticesRemaining.remove(border.scndLastVertexAdded);
                    nextVertex = border.scndLastVertexAdded;
                    break;
                }
            }
            if (currentFace != null) {
                break;
            }
        }
        while (notFoundYet) {
            for(Object obj : verticesRemaining) {
                Vertex vertex = (Vertex) obj;
                this.garbage.add(vertex);
                if (currentFace.include(vertex.id)) {
                    currentFace = getFaceIncluding(vertex, facesRemaining);
                    facesRemaining.remove(currentFace);
                    verticesRemaining.remove(vertex);
                    nextVertex = vertex;
                    break;
                }
            }
            if (verticesRemaining.isEmpty()) {
                notFoundYet = false;
            }
        }
        this.setFacesToVertex(nextVertex);
        System.out.println(nextVertex.toIdString());
        return nextVertex;
    }

    private Face getFaceIncluding(Vertex vertex, Set facesRemaining) {
        Face result = null;
        for (Object obj : facesRemaining) {
            Face face = (Face) obj;
            if (face.include(vertex.id)) {
                result = face;
                break;
            }
        }
        return result;
    }

    final void createBorders() {
        createPrimers();
        doPrimersNeighboors();
        while (!primers.isEmpty()) {
            final Border border = new Border(this);
            Vertex nextVertex = border.lastVertexAdded;
            this.setFacesToVertex(nextVertex);
            nextVertex = this.findNextVertex(border);
            border.addNextVertex(nextVertex);
            while (nextVertex != border.firstVertex && nextVertex != null) {
                nextVertex = this.findNextVertex(border);
                border.addNextVertex(nextVertex);
            }
            if (border.lastVertexAdded == border.firstVertex) {
                // on a fait le tour
                System.out.println("Wéé!");
            }
            completeGarbage();
            primers.removeAll(this.garbage);
            this.garbage.clear();
            this.borders.add(border);
        }
    }

    final void createPrimers() {
        for (Object e : splits) {
            AbstractSplit split = (AbstractSplit) e;
            primers.addAll(split.findBorderVertices(vertices));
        }
    }

    /**
     * This method shift the mesh, depending of its own splits.
     */
    final void shift() {
        int deltaX = 0, deltaY = 0;
        for (final Iterator it = splits.iterator(); it.hasNext();) {
            final AbstractSplit split = (AbstractSplit) it.next();
            if (split.xPosition() > deltaX) {
                deltaX = split.xPosition();
            }
            if (split.yPosition() > deltaY) {
                deltaY = split.yPosition();
            }
        }
        for (final Iterator it = vertices.iterator(); it.hasNext();) {
            final Vertex vertex = (Vertex) it.next();
            vertex.x += deltaX;
            vertex.y += deltaY;
        }
    }

    /**
     * This method is used to export in a file the borders of this mesh.
     *
     * @param filePath , the filename to use.
     */
    final void exportBorders(final String filePath) {
        try {
            ObjWriter.writeBorders(filePath, this.borders);
        } catch (IOException ex) {
            Logger.getLogger(Mesh.class.getName()).log(Level.SEVERE, null, ex);
        }
        borders.clear();
    }
}
