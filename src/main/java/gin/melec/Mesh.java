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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
    private TreeSet<Vertex> vertices;
    /**
     * Contain the faces composing this mesh.
     */
    private Set<Face> faces;
    /**
     * List of borders of this mesh.
     */
    private Set<Border> borders;

    /**
     * Contain the vertex already used to make a border. This garbage is emptied
     * when a border is formed, and the corresponding vertex are removed from
     * the mesh.
     */
    private Set<Vertex> garbage;
    /**
     * Contain the vertex that can initiate a border. Once this set is empty, it
     * means that all the borders have been detected. Else, we create a new one
     * by taking a vertex in the set (method in split). After a detection of a
     * border, the vertex detected are removed from this set.
     */
    private TreeSet<Vertex> primers;

    /**
     * Switch the position of the mesh and switch the type of scene, meshes can
     * have up to 4 different splits.
     */
    private List<AbstractSplit> splits;

    /**
     * Public constructor for a mesh.
     *
     * @param splits , the splits of the mesh.
     */
    public Mesh(final List<AbstractSplit> splits) {
        this.faces = new TreeSet();
        this.vertices = new TreeSet();
        this.garbage = new HashSet();
        this.borders = new HashSet();
        this.splits = splits;
        this.primers = new TreeSet();
    }

    final void setFacesToVertex(final Vertex vertex) {
        for (Face face : this.faces) {
            if (face.getIdVertex1() == vertex.getId()) {
                vertex.getFaces().add(face);
                vertex.getNeighbours().add(getVertex(face.getIdVertex2()));
                vertex.getNeighbours().add(getVertex(face.getIdVertex3()));
            } else if (face.getIdVertex2() == vertex.getId()) {
                vertex.getFaces().add(face);
                vertex.getNeighbours().add(getVertex(face.getIdVertex1()));
                vertex.getNeighbours().add(getVertex(face.getIdVertex3()));
            } else if (face.getIdVertex3() == vertex.getId()) {
                vertex.getFaces().add(face);
                vertex.getNeighbours().add(getVertex(face.getIdVertex1()));
                vertex.getNeighbours().add(getVertex(face.getIdVertex2()));
            }
        }
    }

    private void doPrimersNeighboors() {
        int minId, maxId;
        minId = this.primers.first().getId();
        maxId = this.primers.last().getId();
        for (Face face : this.faces) {
            if ((face.getIdVertex1() >= minId && face.getIdVertex3() <= maxId)) {
                final Vertex vertex1 = getVertex(face.getIdVertex1());
                final Vertex vertex2 = getVertex(face.getIdVertex2());
                final Vertex vertex3 = getVertex(face.getIdVertex3());
                vertex1.getNeighbours().add(vertex2);
                vertex1.getNeighbours().add(vertex3);
                vertex2.getNeighbours().add(vertex1);
                vertex2.getNeighbours().add(vertex3);
                vertex3.getNeighbours().add(vertex1);
                vertex3.getNeighbours().add(vertex2);
            }
        }
    }

    private final Vertex getVertex(final int idVertex) {
        Vertex result;
        Vertex doppleganger = new Vertex(idVertex, 0, 0, 0);
        result = this.vertices.floor(doppleganger);
        if(!result.equals(doppleganger)) {
            result = null;
        }
        return result;
    }

    /**
     * Once a border is finish, this method is called to add all vertex related
     * to the border to the garbage.
     */
    final void completeGarbage() {
        final List<Vertex> vertexToCheck = new ArrayList(this.garbage);
        for (Vertex vertex : vertexToCheck) {
            vertex.addNeighborToGarbage(this.garbage);
        }
    }

    final Vertex findNextVertex(final Border border) {
        Vertex nextVertex = null;
        final Set<Face> facesRemaining = new HashSet();
        final Set<Vertex> verticesRemaining = new HashSet();
        facesRemaining.addAll(border.getLastVertex().getFaces());
        verticesRemaining.addAll(border.getLastVertex().getNeighbours());
        Boolean notFoundYet = true;

        Face currentFace = null;
        // Search the face, the two last vertex added to the border, have in
        // in common
        for (Face face1 : border.getLastVertex().getFaces()) {
            for (Face face2 : border.getSecondLastVertex().getFaces()) {
                if (face2 == face1) {
                    currentFace = face1;
                    facesRemaining.remove(face1);
                    verticesRemaining.remove(border.getSecondLastVertex());
                    nextVertex = border.getSecondLastVertex();
                    break;
                }
            }
            if (currentFace != null) {
                break;
            }
        }
        while (notFoundYet) {
            for(Vertex vertex : verticesRemaining) {
                this.garbage.add(vertex);
                if (currentFace.include(vertex.getId())) {
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

    private Face getFaceIncluding(final Vertex vertex,
            final Set<Face> facesRemaining) {
        Face result = null;
        for (Face face : facesRemaining) {
            if (face.include(vertex.getId())) {
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
            Vertex nextVertex = border.getLastVertex();
            this.setFacesToVertex(nextVertex);
            nextVertex = this.findNextVertex(border);
            border.addNextVertex(nextVertex);
            while (nextVertex != border.getFirstVertex() && nextVertex != null) {
                nextVertex = this.findNextVertex(border);
                border.addNextVertex(nextVertex);
            }
            if (border.getLastVertex() == border.getFirstVertex()) {
                // on a fait le tour
                System.out.println("Wéé!");
            }
            completeGarbage();
            primers.removeAll(this.garbage);
            this.garbage.clear();
            this.borders.add(border);
        }
        this.borders = this.separateBorders();
    }

    private final Set separateBorders() {
        Set result = new HashSet();
        for (AbstractSplit split : this.splits) {
            result.addAll(split.refineBorders(this.borders));
        }
        return result;
    }

    final void createPrimers() {
        for (AbstractSplit split : splits) {
            primers.addAll(split.findBorderVertices(vertices));
        }
    }

    /**
     * This method shift the mesh, depending of its own splits.
     */
    final void shift() {
        int deltaX = 0, deltaY = 0;
        for (AbstractSplit split : splits) {
            if (split.xPosition() > deltaX) {
                deltaX = split.xPosition();
            }
            if (split.yPosition() > deltaY) {
                deltaY = split.yPosition();
            }
        }
        for (Vertex vertex : this.vertices) {
            vertex.setX(vertex.getX() + deltaX);
            vertex.setY(vertex.getY() + deltaY);
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

    public TreeSet<Vertex> getVertices() {
        return vertices;
    }

    public Set<Face> getFaces() {
        return faces;
    }

    public Set<Border> getBorders() {
        return borders;
    }

    public Set<Vertex> getGarbage() {
        return garbage;
    }

    public TreeSet<Vertex> getPrimers() {
        return primers;
    }

    public List<AbstractSplit> getSplits() {
        return splits;
    }

    
}
