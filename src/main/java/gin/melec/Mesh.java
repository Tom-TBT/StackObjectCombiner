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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ij.IJ;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Mesh {

    /**
     * Contain the vertices composing this mesh.
     */
    private final List<Vertex> vertices;
    /**
     * Contain the faces composing this mesh.
     */
    private final Set<Face> faces;
    /**
     * List of borders of this mesh.
     */
    private List<Border> borders;

    /**
     * The flat borders of a mesh are stored in 6 lists corresponding to the
     * different side of a cube. These are the borders that will be merged (automatic
     * merging for now).
     */
    private final List<FlatBorder> leftFlats, rightFlats, upFlats, downFlats, frontFlats, backFlats;

    /**
     * Contain the vertex already used to make a border. This garbage is emptied
     * when a border is formed, and the corresponding vertex are removed from
     * the mesh.
     */
    private final Set<Vertex> garbage;

    /**
     * The path to this mesh's file.
     */
    private final File file;

    /**
     * Boolean to indicate if the mesh is currently loaded into the memory.
     */
    private boolean inMemory;

    /**
     * The boolean that indicate if the mesh has already been moved.
     */
    private boolean moved;

    /**
     * The boolean that indicate if the mesh has already been moved.
     */
    private final boolean merged;

    private final List<Vertex> primers;

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
        this.vertices = new ArrayList();
        this.garbage = new HashSet();
        this.borders = new ArrayList();
        this.inMemory = false;

        this.moved = ObjReader.isMeshMoved(this.file);
        this.merged = ObjReader.isMeshMerged(this.file);

        leftFlats = new ArrayList<FlatBorder>();
        rightFlats = new ArrayList<FlatBorder>();
        upFlats = new ArrayList<FlatBorder>();
        downFlats = new ArrayList<FlatBorder>();
        frontFlats = new ArrayList<FlatBorder>();
        backFlats = new ArrayList<FlatBorder>();

        this.primers = new ArrayList();
    }

    /**
     * Once a border is finish, this method is called to add all vertex related
     * to the border to the garbage.
     */
    /**private void completeGarbage() {
        final List<Vertex> vertexToCheck = new ArrayList(this.garbage);
        for (Vertex vertex : vertexToCheck) {
            vertex.addNeighbourToGarbage(this.garbage, this.primers, 10);
        }
    }**/

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
        while (!this.primers.isEmpty()) {
            final Border border = new Border(this, split);
            if (border.getFirstVertex() == null) {
                break;
            }
            Vertex previousVertex = border.getFirstVertex();
            Vertex currentVertex = border.getLastVertex();
            Vertex nextVertex;
            while (!currentVertex.equals(border.getFirstVertex())) {
                nextVertex = currentVertex.getOtherUnique(previousVertex);
                if (!nextVertex.equals(border.getFirstVertex())) {
                    border.addNextVertex(nextVertex);
                }
                this.primers.remove(nextVertex);
                previousVertex = currentVertex;
                currentVertex = nextVertex;
            }
            this.borders.add(border);
        }
    }

    /**
     * This method shift the mesh, depending of its own splits.
     * @param splits , the splits to apply
     */
    protected final void shift(final List<AbstractSplit> splits) throws IOException {
        double deltaX = 0, deltaY = 0, deltaZ = 0;
        AbstractSplit split1, split2, split3;
        if (splits.size() >= 1) {
            split1 = splits.get(0);
            if (WidthSplit.class.isInstance(split1)) {
                deltaX = split1.xPosition();
            } else if (HeightSplit.class.isInstance(split1)) {
                deltaY = split1.yPosition();
            } else if (DepthSplit.class.isInstance(split1)) {
                deltaZ = split1.zPosition();
            }
            if (splits.size() >= 2) {
                split2 = splits.get(1);
                if (WidthSplit.class.isInstance(split2)) {
                    deltaX = split2.xPosition();
                } else if (HeightSplit.class.isInstance(split2)) {
                    deltaY = split2.yPosition();
                } else if (DepthSplit.class.isInstance(split2)) {
                    deltaZ = split2.zPosition();
                }
                if (splits.size() >= 3) {
                    split3 = splits.get(2);
                    if (WidthSplit.class.isInstance(split3)) {
                        deltaX = split3.xPosition();
                    } else if (HeightSplit.class.isInstance(split3)) {
                        deltaY = split3.yPosition();
                    } else if (DepthSplit.class.isInstance(split3)) {
                        deltaZ = split3.zPosition();
                    }
                }
            }
        }

        if (deltaX > 0) {
            deltaX += 0.5; // To compensate the position of the split in the middle
            for (Vertex vertex : this.vertices) {
                vertex.setX(vertex.getX() + deltaX);
            }
        }
        if (deltaY > 0) {
            deltaY += 0.5; // To compensate the position of the split in the middle
            for (Vertex vertex : this.vertices) {
                vertex.setY(vertex.getY() + deltaY);
            }
        }
        if (deltaZ > 0) {
            deltaZ += 0.5; // To compensate the position of the split in the middle
            for (Vertex vertex : this.vertices) {
                vertex.setZ(vertex.getZ() + deltaZ);
            }
        }
        this.moved = true;
        this.exportMesh(deltaX, deltaY, deltaZ);
    }

    void unshift() throws IOException {
        double deltaX , deltaY, deltaZ;
        double shifts[];
        shifts = ObjReader.getShift(file);
        deltaX = -shifts[0];
        deltaY = -shifts[1];
        deltaZ = -shifts[2];
        if (deltaX < 0) {
            for (Vertex vertex : this.vertices) {
                vertex.setX(vertex.getX() + deltaX);
            }
        }
        if (deltaY < 0) {
            for (Vertex vertex : this.vertices) {
                vertex.setY(vertex.getY() + deltaY);
            }
        }
        if (deltaZ < 0) {
            for (Vertex vertex : this.vertices) {
                vertex.setZ(vertex.getZ() + deltaZ);
            }
        }
        this.moved = false;
    }

    /**
     * Use the ObjWriter to write the vertices and the faces in the file of the
     * mesh.
     *
     * @param shiftX
     * @param shiftY
     * @throws java.io.IOException
     */
    protected final void exportMesh(final double shiftX, final double shiftY, final double shiftZ)
            throws IOException {
        ObjWriter.writeMesh(this, shiftX, shiftY, shiftZ);
    }

    /**
     * Use the ObjReader to import into the mesh the vertices and the faces.
     *
     * @throws java.text.ParseException
     * @throws java.io.IOException
     */
    protected final void importMesh(boolean checkForPrimers) throws ParseException, IOException {
        boolean outOfMem = false;
        Runtime rT = Runtime.getRuntime();
        if (rT.maxMemory() - (rT.totalMemory() - rT.freeMemory())
                < DialogContentManager.MEMORY_WATCHER*1000000) { // Multiplied by one million to make it megabyte
            DialogContentManager.unloadMeshes();
        }
        try {
        ObjReader.readMesh(this.file, this, checkForPrimers);
        } catch (OutOfMemoryError e) {
            DialogContentManager.unloadMeshes();
            outOfMem = true;
        }
        if (outOfMem) {
            ObjReader.readMesh(this.file, this, checkForPrimers);
        }
        this.inMemory = true;
    }

    public final void unload() {
        this.inMemory = false;
        this.clear();
    }

    public final void reload() throws ParseException, IOException {
        if (!inMemory) {
            this.importMesh(false);
            replaceVerticesInBorders();
        }
    }

    private void replaceVerticesInBorders() {
        List<FlatBorder> flats = new ArrayList();
        flats.addAll(this.backFlats);
        flats.addAll(this.frontFlats);
        flats.addAll(this.leftFlats);
        flats.addAll(this.rightFlats);
        flats.addAll(this.upFlats);
        flats.addAll(this.downFlats);

        for (FlatBorder currFlat: flats) {
            for (Object element: currFlat.getElements()) {
                List<Vertex> vertList;
                if (element instanceof List) {
                    vertList = (List) element;
                    for (int i = 0; i < vertList.size(); i++) {
                        Vertex currVertex = vertList.get(i);
                        vertList.set(i, this.vertices.get(currVertex.id - 1));
                    }
                }
            }
        }
    }

    public void findPrimers() {
        for (Vertex v: this.vertices) {
            if (v.isBorderVertex()) {
                this.primers.add(v);
            }
        }
    }

    /**
     * Import the borders from a given file.
     *
     * @throws java.io.IOException
     */
    protected final void importBorders() throws IOException {
        this.borders = ObjReader.deserializeBorders(this.file);
    }

    protected final void incremVertices(int increm) {
        for(Vertex vertex: this.vertices) {
            vertex.incrementId(increm);
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
    public final List<Vertex> getVertices() {
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
     * Getter of the attribute merged.
     *
     * @return true if the mesh has already been merged.
     */
    public final boolean isMerged() {
        return merged;
    }

    /**
     * Clear the faces and the vertices of the mesh. It is used to release
     * memory because of the size of these arrays.
     */
    private void clear() {
        this.faces.clear();
        this.vertices.clear();
    }

    void setBorders(List<Border> borders) {
        this.borders = borders;
    }

    void addLeftFlat(List<FlatBorder> flats) {
        if (flats != null) {
            this.leftFlats.addAll(flats);
        }
    }
    void addRightFlat(List<FlatBorder> flats) {
        if (flats != null) {
            this.rightFlats.addAll(flats);
        }
    }
    void addUpFlat(List<FlatBorder> flats) {
        if (flats != null) {
            this.upFlats.addAll(flats);
        }
    }
    void addDownFlat(List<FlatBorder> flats) {
        if (flats != null) {
            this.downFlats.addAll(flats);
        }
    }
    void addFrontFlat(List<FlatBorder> flats) {
        if (flats != null) {
            this.frontFlats.addAll(flats);
        }
    }
    void addBackFlat(List<FlatBorder> flats) {
        if (flats != null) {
            this.backFlats.addAll(flats);
        }
    }

    void computeRightFlatProperties(AbstractSplit split) {
        for (FlatBorder flat : this.rightFlats) {
            flat.computeProperties(split);
        }

    }
    void computeLeftFlatProperties(AbstractSplit split) {
        for (FlatBorder flat : this.leftFlats) {
            flat.computeProperties(split);
        }
    }
    void computeUpFlatProperties(AbstractSplit split) {
        for (FlatBorder flat : this.upFlats) {
            flat.computeProperties(split);
        }
    }
    void computeDownFlatProperties(AbstractSplit split) {
        for (FlatBorder flat : this.downFlats) {
            flat.computeProperties(split);
        }
    }
    void computeFrontFlatProperties(AbstractSplit split) {
        for (FlatBorder flat : this.frontFlats) {
            flat.computeProperties(split);
        }
    }
    void computeBackFlatProperties(AbstractSplit split) {
        for (FlatBorder flat : this.backFlats) {
            flat.computeProperties(split);
        }
    }

    public List<Vertex> getPrimers() {
        return primers;
    }

    public List<FlatBorder> getLeftFlats() {
        return leftFlats;
    }

    public List<FlatBorder> getRightFlats() {
        return rightFlats;
    }

    public List<FlatBorder> getUpFlats() {
        return upFlats;
    }

    public List<FlatBorder> getDownFlats() {
        return downFlats;
    }

    public List<FlatBorder> getFrontFlats() {
        return frontFlats;
    }

    public List<FlatBorder> getBackFlats() {
        return backFlats;
    }
}
