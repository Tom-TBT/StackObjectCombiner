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

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Mesh {

    /**
     * Contain the vertices composing this mesh.
     */
    private final TreeSet<Vertex> vertices;
    /**
     * Contain the faces composing this mesh.
     */
    private final Set<Face> faces;
    /**
     * List of borders of this mesh.
     */
    private List<Border> borders;

    /**
     * Contain the vertex already used to make a border. This garbage is emptied
     * when a border is formed, and the corresponding vertex are removed from
     * the mesh.
     */
    private final Set<Vertex> garbage;
    /**
     * Contain the vertex that can initiate a border. Once this set is empty, it
     * means that all the borders have been detected. Else, we create a new one
     * by taking a vertex in the set (method in split). After a detection of a
     * border, the vertex detected are removed from this set.
     */
    private final TreeSet<Vertex> primers;

    /**
     * The path to this mesh's file.
     */
    private final File file;

    /**
     * The boolean that indicate if the mesh has already been moved.
     */
    private boolean moved;

    /**
     * Public constructor for a mesh.
     *
     * @param file , the file containing the mesh.
     * @param splits , the splits of the mesh.
     * @throws java.io.IOException
     */
    public Mesh(final File file)
            throws IOException {
        this.file = file;
        this.faces = new HashSet();
        this.vertices = new TreeSet();
        this.garbage = new HashSet();
        this.borders = new ArrayList();
        this.primers = new TreeSet();

        this.moved = ObjReader.isMeshMoved(this.file);
    }

    /**
     * Once a border is finish, this method is called to add all vertex related
     * to the border to the garbage.
     */
    private void completeGarbage() {
        final List<Vertex> vertexToCheck = new ArrayList(this.garbage);
        for (Vertex vertex : vertexToCheck) {
            vertex.addNeighbourToGarbage(this.garbage, this.primers);
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
        this.garbage.add(nextVertex);
        return nextVertex;
    }

    /**
     * This is the principal method for creating every borders of a mesh. It
     * first create primers (vertex of the mesh) which can potentially initiate
     * a new border. Then a border is created from the primers, and once the
     * border has been detected, the vertex already detected are removed to the
     * primers. The iteration of the detection of the borders stop once the
     * primers set is empty.
     *
     * @param split , the split for which we create the borders.
     */
    protected final void createBorders(final AbstractSplit split) {
        createPrimers(split);
        if (!this.primers.isEmpty()) {
            while (!primers.isEmpty()) {
                final Border border = new Border(this, split);
                if (border.getFirstVertex() == null) {
                    break;
                }
                Vertex nextVertex = border.getLastVertex();
                while (!nextVertex.equals(border.getFirstVertex())) {
                    nextVertex = this.findNextVertex(border);
                    if (!nextVertex.equals(border.getFirstVertex())) {
                        border.addNextVertex(nextVertex);
                    }
                }
                completeGarbage();
                primers.removeAll(this.garbage);
                this.garbage.clear();
                this.borders.add(border);
            }

            final List<Border> tmpBorders = new ArrayList();
            for (Border border : this.borders) {
                tmpBorders.addAll(border.separateSubBorders());
            }
            this.borders = tmpBorders;
            CustomFrame.appendToLog(this.borders.size() + " borders detected for "
                    + this.file.getName());
        }
    }

    /**
     * Add the vertex that can initiate a border to the primers set.
     *
     * @param split , the split used for the primers.
     */
    private void createPrimers(final AbstractSplit split) {
        primers.addAll(split.findLimitVertices(vertices));
    }

    /**
     * This method shift the mesh, depending of its own splits.
     * @param split , the split that give the shift to apply.
     */
    protected final void shift(final AbstractSplit split) {
        double deltaX = 0, deltaY = 0;
        if (SplitLeft.class.isInstance(split)) {
            deltaX = split.xPosition();
        }
        if (SplitUp.class.isInstance(split)) {
            deltaY = split.yPosition();
        }
        if (deltaX > 0) {
            deltaX += 0.5;
            for (Vertex vertex : this.vertices) {
                vertex.setX(vertex.getX() + deltaX);
            }
        }
        if (deltaY > 0) {
            deltaY += 0.5;
            for (Vertex vertex : this.vertices) {
                vertex.setY(vertex.getY() + deltaY);
            }
        }
        this.moved = true;
    }

    /**
     * Use the ObjWriter to write the vertices and the faces in the file of the
     * mesh.
     *
     * @throws java.io.IOException
     */
    protected final void exportMesh() throws IOException {
        ObjWriter.writeMesh(this.file, this);
    }

    /**
     * Use the ObjReader to import into the mesh the vertices and the faces.
     *
     * @throws java.text.ParseException
     * @throws java.io.IOException
     */
    protected final void importMesh() throws ParseException, IOException {
        ObjReader.readMesh(this.file, this);
    }

    /**
     * Import the borders from a given file.
     *
     * @throws java.io.IOException
     */
    protected final void importBorders() throws IOException {
        this.borders = ObjReader.deserializeBorders(this.file);
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
