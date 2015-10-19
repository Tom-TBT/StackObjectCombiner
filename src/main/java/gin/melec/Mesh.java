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

import ij.IJ;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
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
    private List<Border> borders;

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
     * The path to this mesh's file.
     */
    private File file;

    /**
     * The boolean that indicate if the mesh has already been moved.
     */
    private boolean moved;

    /**
     * Public constructor for a mesh.
     * @param file , the file containing the mesh.
     * @param splits , the splits of the mesh.
     */
    public Mesh(final List<AbstractSplit> splits, final File file) {
        this.file = file;
        this.faces = new TreeSet();
        this.vertices = new TreeSet();
        this.garbage = new HashSet();
        this.borders = new ArrayList();
        this.splits = splits;
        this.primers = new TreeSet();

        try {
            moved = ObjReader.isMeshMoved(this.file);
        } catch (IOException ex) {
            Logger.getLogger(Mesh.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Set to a vertex the faces in which he is.
     *
     * @param vertex , the vertex to set the faces.
     */
    protected final void setNeighbourhoodToVertex(final Vertex vertex) {
        for (Face face : this.faces) {
            if (face.getVertex1().equals(vertex)) {
                vertex.getFaces().add(face);
                vertex.getNeighbours().add(face.getVertex2());
                vertex.getNeighbours().add(face.getVertex3());
            } else if (face.getVertex2().equals(vertex)) {
                vertex.getFaces().add(face);
                vertex.getNeighbours().add(face.getVertex1());
                vertex.getNeighbours().add(face.getVertex3());
            } else if (face.getVertex3().equals(vertex)) {
                vertex.getFaces().add(face);
                vertex.getNeighbours().add(face.getVertex1());
                vertex.getNeighbours().add(face.getVertex2());
            }
        }
    }

    /**
     * Set the neighbours to the primers. Every primers need to know its
     * neighbours, so that they can add them in the garbage later.
     */
    private void doPrimersNeighbours() {
        int minId, maxId;
        minId = this.primers.first().getId();
        maxId = this.primers.last().getId();
        for (Face face : this.faces) {
            if ((face.getVertex1().getId() >= minId
                    && face.getVertex3().getId() <= maxId)) {
                final Vertex vertex1 = face.getVertex1();
                final Vertex vertex2 = face.getVertex2();
                final Vertex vertex3 = face.getVertex3();
                vertex1.getNeighbours().add(vertex2);
                vertex1.getNeighbours().add(vertex3);
                vertex2.getNeighbours().add(vertex1);
                vertex2.getNeighbours().add(vertex3);
                vertex3.getNeighbours().add(vertex1);
                vertex3.getNeighbours().add(vertex2);
            }
        }
    }

    /**
     * Once a border is finish, this method is called to add all vertex related
     * to the border to the garbage.
     */
    private void completeGarbage() {
        final List<Vertex> vertexToCheck = new ArrayList(this.garbage);
        for (Vertex vertex : vertexToCheck) {
            vertex.addNeighborToGarbage(this.garbage);
        }
    }

    /**
     * Find the next vertex to add to the border. It is find according to the
     * appartenance of a face in which is also the last vertex added.
     *
     * @param border , the border for which we search the next vertex.
     * @return the next vertex to add to the border.
     */
    private Vertex findNextVertex(final Border border) {
        Vertex nextVertex = border.getSecondLastVertex();
        Face currentFace = null;
        final Set<Face> facesRemaining = new HashSet();
        facesRemaining.addAll(border.getLastVertex().getFaces());

        while (!facesRemaining.isEmpty()) {
            for (Face face : facesRemaining) {
                if (face.include(nextVertex)) {
                    currentFace = face;
                    break;
                }
            }
            facesRemaining.remove(currentFace);
            nextVertex = currentFace.getThirdVertex(border.getLastVertex(),
                    nextVertex);
        }
        this.setNeighbourhoodToVertex(nextVertex);
        return nextVertex;
    }

    /**
     * This is the principal method for creating every borders of a mesh. It
     * first create primers (vertex of the mesh) which can potentially initiate
     * a new border. Then a border is created from the primers, and once the
     * border has been detected, the vertex already detected are removed to the
     * primers. The iteration of the detection of the borders stop once the
     * primers set is empty.
     */
    protected final void createBorders() {
        createPrimers();
        if (!this.primers.isEmpty()) {
            doPrimersNeighbours();
            IJ.log("Primers created");
            while (!primers.isEmpty()) {
                final Border border = new Border(this);
                IJ.log("New border");
                Vertex nextVertex = border.getLastVertex();
                while (!nextVertex.equals(border.getFirstVertex())) {
                    nextVertex = this.findNextVertex(border);
                    if (!nextVertex.equals(border.getFirstVertex())) {
                        border.addNextVertex(nextVertex);
                    }
                }
                IJ.log("Border ok");
                completeGarbage();
                primers.removeAll(this.garbage);
                IJ.log("primers size " + primers.size());
                this.garbage.clear();
                this.borders.add(border);
            }
            IJ.log("mesh done");
            final List<Border> tmpBorders = new ArrayList();
            for (Border border : this.borders) {
                tmpBorders.addAll(border.separateSubBorders());
            }
            this.borders = tmpBorders;
        }
    }

    /**
     * Add the vertex that can initiate a border to the primers set.
     */
    private void createPrimers() {
        for (AbstractSplit split : splits) {
            primers.addAll(split.findLimitVertices(vertices));
        }
    }

    /**
     * This method shift the mesh, depending of its own splits.
     */
    protected final void shift() {
        long deltaX = 0, deltaY = 0;
        for (AbstractSplit split : splits) {
            if (SplitLeft.class.isInstance(split)) {
                deltaX = split.xPosition();
            }
            if (SplitUp.class.isInstance(split)) {
                deltaY = split.yPosition();
            }
        }
        if (deltaX > 0 || deltaY > 0) {
            for (Vertex vertex : this.vertices) {
                vertex.setX(vertex.getX() + deltaX);
                vertex.setY(vertex.getY() + deltaY);
            }
        }
        this.moved = true;
    }

    /**
     * Use the ObjWriter to write the vertices and the faces in the file of the
     * mesh.
     */
    protected final void exportMesh() {
        try {
            ObjWriter.writeMesh(this.file, this);
        } catch (IOException ex) {
            Logger.getLogger(Mesh.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Use the ObjReader to import into the mesh the vertices and the faces.
     */
    protected final void importMesh() throws ParseException {
        try {
            ObjReader.readMesh(this.file, this);
        } catch (IOException ex) {
            Logger.getLogger(Mesh.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is used to export in a file the borders of this mesh.
     *
     * @param filePath , the filename to use.
     */
    protected final void exportBorders() {
        try {
            ObjWriter.serializeBorders(this.file, borders);
        } catch (IOException ex) {
            Logger.getLogger(Mesh.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Import the borders from a given file.
     *
     * @param filePath ,the path of the file in which the borders are contained.
     */
    protected final void importBorders() {
        try {
            this.borders = ObjReader.deserializeBorders(this.file);
        } catch (IOException ex) {
            Logger.getLogger(Mesh.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        return this.file.getName();
    }

    /**
     * Getter of the attribute vertices.
     *
     * @return the vertices of the mesh.
     */
    public final TreeSet<Vertex> getVertices() {
        return vertices;
    }

    /**
     * Getter of the attribute faces.
     *
     * @return the faces of the mesh.
     */
    public final Set<Face> getFaces() {
        return faces;
    }

    /**
     * Getter of the attribute borders.
     *
     * @return the borders of the mesh.
     */
    public final List<Border> getBorders() {
        return borders;
    }

    /**
     * Getter of the attribute garbage.
     *
     * @return the garbage of the mesh.
     */
    public final Set<Vertex> getGarbage() {
        return garbage;
    }

    /**
     * Getter of the attribute primers.
     *
     * @return the primers of the mesh.
     */
    public final TreeSet<Vertex> getPrimers() {
        return primers;
    }

    /**
     * Getter of the attribute splits.
     *
     * @return the splits of the mesh.
     */
    public final List<AbstractSplit> getSplits() {
        return splits;
    }

    /**
     * Getter of the attribute path.
     *
     * @return the path of the mesh.
     */
    public final File getFile() {
        return file;
    }

    /**
     * Getter of the attribute moved.
     *
     * @return true if the mesh has already been moved.
     */
    public final boolean isMoved() {
        return moved;
    }

    /**
     * Clear the faces and the vertices of the mesh. It is used to release
     * memory because of the size of these arrays.
     */
    void clear() {
        this.borders.clear();
        this.faces.clear();
        this.vertices.clear();
    }
}
