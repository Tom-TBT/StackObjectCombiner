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

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Cube {
    public static int BORDER_SEPARATION = 5;

    private final List<Mesh> meshes;
    private final double x;
    private final double y;
    private final double z;
    private final double width;
    private final double height;
    private final double depth;
    private final WidthSplit leftSplit;
    private final WidthSplit rightSplit;
    private final HeightSplit backSplit;
    private final HeightSplit frontSplit;
    private final DepthSplit downSplit;
    private final DepthSplit upSplit;
    private CustomPlane frontLeftPlane;
    private CustomPlane frontRightPlane;
    private CustomPlane frontUpPlane;
    private CustomPlane frontDownPlane;
    private CustomPlane backLeftPlane;
    private CustomPlane backRightPlane;
    private CustomPlane backUpPlane;
    private CustomPlane backDownPlane;
    private CustomPlane leftUpPlane;
    private CustomPlane leftDownPlane;
    private CustomPlane rightUpPlane;
    private CustomPlane rightDownPlane;

    public Cube(final double x,final double y, final double z, final double width,
            final double height, final double depth) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.width = width;
        this.height = height;
        this.depth = depth;
        // width height and depth are not good names. Here it represent the position
        // of the end of the cube

        leftSplit = new WidthSplit(x);
        rightSplit = new WidthSplit(width);
        backSplit = new HeightSplit(y);
        frontSplit = new HeightSplit(height);
        upSplit = new DepthSplit(z);
        downSplit = new DepthSplit(depth);

        setEdges();
        setPlanes();
        meshes = new ArrayList<Mesh>();
    }

    /**
     * Reload all the meshes of the cube into memory.
     * @throws ParseException
     * @throws IOException
     */
    public void reloadMeshes() throws ParseException, IOException {
        for (Mesh mesh: this.meshes) {
            mesh.reload();
        }
    }

    /**
     * For all the meshes, it detect the borders and then separate them into
     * flat borders.
     * @throws ParseException
     * @throws IOException
     * @throws BorderSeparationException
     */
    protected void detectMeshBorders() throws ParseException, IOException, BorderSeparationException {
        for (Mesh mesh : meshes) {
            mesh.importMesh(true);
            CustomFrame.appendToLog("Detecting and preparing borders for "+mesh.getFile().getName());
            mesh.findPrimers();
            mesh.createBorders(leftSplit);
            mesh.createBorders(rightSplit);
            mesh.createBorders(backSplit);
            mesh.createBorders(frontSplit);
            mesh.createBorders(downSplit);
            mesh.createBorders(upSplit);
            for (Border currentBorder : mesh.getBorders()) {
                separateBorder(currentBorder, mesh);
            }
            storeFlatBorders(mesh);
            setFlatsToMesh(mesh);
            computeFlatProperties(mesh);
            int borderNumber = mesh.getBackFlats().size() + mesh.getFrontFlats().size()+
                    mesh.getLeftFlats().size() + mesh.getRightFlats().size()+
                    mesh.getUpFlats().size() + mesh.getDownFlats().size();
            CustomFrame.appendToLog(borderNumber+" borders detected for "+mesh.getFile().getName());
        }
    }

    /**
     * Compute the properties of all the flats contained by the mesh. For that
     * it needs the corresponding splits.
     * @param mesh , the mesh to compute the flat properties for.
     */
    private void computeFlatProperties(final Mesh mesh) {
        mesh.computeLeftFlatProperties(this.leftSplit);
        mesh.computeRightFlatProperties(this.rightSplit);
        mesh.computeUpFlatProperties(this.upSplit);
        mesh.computeDownFlatProperties(this.downSplit);
        mesh.computeFrontFlatProperties(this.frontSplit);
        mesh.computeBackFlatProperties(this.backSplit);
    }

    /**
     * Create the flats and store them into the splits.
     * @param mesh, the mesh for which we create the flats borders.
     */
    private void storeFlatBorders(Mesh mesh) {
        frontSplit.storeFlatBorders(mesh);
        backSplit.storeFlatBorders(mesh);
        upSplit.storeFlatBorders(mesh);
        downSplit.storeFlatBorders(mesh);
        leftSplit.storeFlatBorders(mesh);
        rightSplit.storeFlatBorders(mesh);
    }

    /**
     * Get the flats borders stored in the splits to put it inside the mesh.
     * @param mesh, the mesh for which we give its flats.
     */
    private void setFlatsToMesh(final Mesh mesh) {
        mesh.addBackFlat(this.backSplit.getFlatBorders());
        this.backSplit.clearFlatBorders();
        mesh.addFrontFlat(this.frontSplit.getFlatBorders());
        this.frontSplit.clearFlatBorders();
        mesh.addLeftFlat(this.leftSplit.getFlatBorders());
        this.leftSplit.clearFlatBorders();
        mesh.addRightFlat(this.rightSplit.getFlatBorders());
        this.rightSplit.clearFlatBorders();
        mesh.addUpFlat(this.upSplit.getFlatBorders());
        this.upSplit.clearFlatBorders();
        mesh.addDownFlat(this.downSplit.getFlatBorders());
        this.downSplit.clearFlatBorders();
    }

    /**
     * Separation of the borders into flat borders. The separation is made with
     * planes crossing the edges of the cube. When the border pass throught one
     * of these planes, it separate it. Then fragments of border create are also
     * assigned to an edge of the cube so that we can put together the differents
     * fragments of one side of the cube to create the whole surface of the mesh
     * corresponding to this side.
     *
     * @param currentBorder the border to be separated.
     * @param mesh, the mesh to which the border belong.
     * @throws BorderSeparationException, exception thrown if the separation went
     * wrong.
     */
    private void separateBorder(Border currentBorder, Mesh mesh) throws BorderSeparationException {
        List<Vertex> vertexList = new ArrayList(currentBorder.getVertexSequence());

        int i = 0;
        // Moving outside of the range of a split.
        while (i < vertexList.size()) {
            Vertex tmpVertex = vertexList.get(0);
            if (vertexCloseToSeveralSplit(tmpVertex)) {
                vertexList.remove(0);
                vertexList.add(tmpVertex);
            } else {
                break;
            }
            i++;
        }

        if(i == vertexList.size()) {
            //erroor, all vertex from the border are close to two borders.
        }

        AbstractSplit currSplit = null;
        if (this.backSplit.isClose(vertexList.get(0))) {
            currSplit = this.backSplit;
        } else if (this.frontSplit.isClose(vertexList.get(0))) {
            currSplit = this.frontSplit;
        } else if (this.upSplit.isClose(vertexList.get(0))) {
            currSplit = this.upSplit;
        } else if (this.downSplit.isClose(vertexList.get(0))) {
            currSplit = this.downSplit;
        } else if (this.leftSplit.isClose(vertexList.get(0))) {
            currSplit = this.leftSplit;
        } else if (this.rightSplit.isClose(vertexList.get(0))) {
            currSplit = this.rightSplit;
        }

        AbstractSplit nextSplit = null;
        CustomPlane[] planes = getPlanes(currSplit);

        List<Integer> crossingPositions = new ArrayList();
        List<AbstractSplit> splitList = new ArrayList();
        splitList.add(currSplit);
        i = 0;
        while (i < vertexList.size()) {
            Vertex v1 = getVertexAtIndex(i, vertexList);
            Vertex v2 = getVertexAtIndex(i + 1, vertexList);
            for (int j = 0; j < 4; j++) {
                nextSplit = getNextSplit(v1, v2, planes[j], currSplit);
                if (nextSplit != null) {
                    crossingPositions.add(i);
                    splitList.add(nextSplit);
                    planes = getPlanes(nextSplit);
                    while(currSplit.isClose(getVertexAtIndex(i, vertexList))) {
                        i++;
                    }
                    currSplit = nextSplit;
                    i+=BORDER_SEPARATION; // After moving away from the last crossing, we still move a bit far away just in case
                    break;
                }
            }
            i++;
        }

        if (splitList.get(0) == splitList.get(splitList.size()-1)) {
            if (splitList.size() == 1) { // cross no edge -> circular
                FlatBorder flat = new FlatBorder(mesh);
                flat.addElement(vertexList);
                splitList.get(0).addFlatBorder(flat);
            } else {
                i = 0;
                Edge startEdge = null;
                Border firstBorder = null;
                while (i < crossingPositions.size()) {
                    Edge[] edges = getNextEdge(splitList.get(i), splitList.get(i+1));
                    Border border;
                    if (i == 0) {
                        List<Vertex> tmpSequence = new ArrayList(vertexList.subList(crossingPositions.get(crossingPositions.size()-1), vertexList.size()));
                        tmpSequence.addAll(vertexList.subList(0, crossingPositions.get(i)));
                        border = new Border(tmpSequence, 0, tmpSequence.size(), edges[0]);
                        firstBorder = border;
                    } else {
                        border = new Border(vertexList, crossingPositions.get(i-1), crossingPositions.get(i), edges[0]);
                        startEdge.addConnector(border.getConnector());
                    }
                    border.setSplit(splitList.get(i));
                    border.getVertexSequence().add(vertexList.get(crossingPositions.get(i)));
                    //border.getConnector().setVertex(border.getFirstVertex());
                    startEdge = edges[1];
                    i++;
                    if (i == crossingPositions.size()) {
                        startEdge.addConnector(firstBorder.getConnector());
                    }
                }
            }
        } else {
            throw new BorderSeparationException("Error while separating the border of "+mesh.toString()+":\n"
                    + "The first and last fragments don't correspond. \n"
                    + "Try to change the border separation parameter, \n"
                    + "or remove the mesh from automatic merging.");
        }

    }

    /**
     * Check if the given vertex is close to more than one split.
     * @param v, the vertex to check.
     * @return true if the vertex is close to several splits.
     */
    private boolean vertexCloseToSeveralSplit(Vertex v) {
        int result = 0;
        if (this.frontSplit.isClose(v)) {
            result++;
        }
        if (this.backSplit.isClose(v)) {
            result++;
        }
        if (this.upSplit.isClose(v)) {
            result++;
        }
        if (this.downSplit.isClose(v)) {
            result++;
        }
        if (this.leftSplit.isClose(v)) {
            result++;
        }
        if (this.rightSplit.isClose(v)) {
            result++;
        }
        return result > 1;
    }

    /**
     * Retrieve the vertex from the list at the given indice. If the index is
     * bigger than the size of the list, it will start back from the begining of
     * the list.
     * @param index, the index of the vertex we are looking for.
     * @param vertexList, the list containing the vertex.
     * @return the vertex at the index inside the list.
     */
    private Vertex getVertexAtIndex(int index, List<Vertex> vertexList) {
        Vertex result;
        if (index < vertexList.size()) {
            result = vertexList.get(index);
        } else {
            result = vertexList.get(index % vertexList.size());
        }
        return result;
    }

    /**
     * Give the next plane, by checking first if the segment created by the two
     * vertices cross the current plane.
     * @param v1, the first vertex.
     * @param v2, the second vertex.
     * @param plane, the plane that might be intersected.
     * @param currentSplit, the current split used.
     * @return the next split of the border.
     */
    private AbstractSplit getNextSplit(final Vertex v1, final Vertex v2, CustomPlane plane, AbstractSplit currentSplit) {
        Vector3D vect1 = new Vector3D(v1.getX(), v1.getY(), v1.getZ());
        Vector3D vect2 = new Vector3D(v2.getX(), v2.getY(), v2.getZ());
        Line line = new Line(vect1, vect2, 0.001);

        Vector3D intersection = plane.intersection(line);
        AbstractSplit result = null;
        if (intersection != null && vect1.distance(vect2) > vect1.distance(intersection)) {
            if (plane.getSplit1() == currentSplit) {
                result = plane.getSplit2();
            } else if (plane.getSplit2() == currentSplit){
                result = plane.getSplit1();
            }
        }
        return result;
    }

    protected void addMesh(final Mesh mesh) {
        meshes.add(mesh);
    }

    protected void addAllMesh(final List meshes) {
        this.meshes.addAll(meshes);
    }

    public List<Mesh> getMeshes() {
        return meshes;
    }

    protected Mesh getMesh(final String meshName) {
        Mesh result = null;
        for (Mesh mesh : meshes) {
                if (mesh.toString().equals(meshName)) {
                    result = mesh;
                    break;
                }
        }
        return result;
    }

    /**
     * Set the planes of the cube.
     */
    private void setPlanes() {
        Vector3D orig= new Vector3D(x,y,z);
        Vector3D origX= new Vector3D(width,y,z);
        Vector3D origY= new Vector3D(x,height,z);
        Vector3D origZ= new Vector3D(x,y,depth);
        Vector3D xZ = new Vector3D(width, y,depth);
        Vector3D xY = new Vector3D(width,height, z);
        Vector3D yZ = new Vector3D(x,height,depth);
        Vector3D xYZ = new Vector3D(width,height,depth);

        Vector3D constructedVector;

        constructedVector = new Vector3D((origY.getX()-100),(origY.getY()+100),origY.getZ());
        frontLeftPlane = new CustomPlane(frontSplit, leftSplit, origY, yZ, constructedVector, 0.001);

        constructedVector = new Vector3D((xY.getX()-100),(xY.getY()-100),xY.getZ());
        frontRightPlane = new CustomPlane(frontSplit, rightSplit, xY, xYZ, constructedVector, 0.001);

        constructedVector = new Vector3D(origY.getX(),(origY.getY()-100),(origY.getZ() + 100));
        frontUpPlane = new CustomPlane(frontSplit, upSplit, origY, xY, constructedVector, 0.001);

        constructedVector = new Vector3D(yZ.getX(),(yZ.getY()-100),(yZ.getZ() - 100));
        frontDownPlane = new CustomPlane(frontSplit, downSplit, yZ, xYZ, constructedVector, 0.001);

        constructedVector = new Vector3D((orig.getX()+100),(orig.getY()+100),orig.getZ());
        backLeftPlane = new CustomPlane(backSplit, leftSplit, orig, origZ, constructedVector, 0.001);

        constructedVector = new Vector3D((origX.getX()-100),(origX.getY()+100),origX.getZ());
        backRightPlane = new CustomPlane(backSplit, rightSplit, origX, xZ, constructedVector, 0.001);

        constructedVector = new Vector3D(orig.getX(),(orig.getY()+100),(orig.getZ() + 100));
        backUpPlane = new CustomPlane(backSplit, upSplit, orig, origX, constructedVector, 0.001);

        constructedVector = new Vector3D(origZ.getX(),(origZ.getY()+100),(origZ.getZ() - 100));
        backDownPlane = new CustomPlane(backSplit, downSplit, origZ, xZ, constructedVector, 0.001);

        constructedVector = new Vector3D((orig.getX()+100),orig.getY(),(orig.getZ() + 100));
        leftUpPlane = new CustomPlane(leftSplit, upSplit, orig, origY, constructedVector, 0.001);

        constructedVector = new Vector3D((origZ.getX()+100),origZ.getY(),(origZ.getZ() - 100));
        leftDownPlane = new CustomPlane(leftSplit, downSplit, origZ, yZ, constructedVector, 0.001);

        constructedVector = new Vector3D((origX.getX()-100),origX.getY(),(origX.getZ() + 100));
        rightUpPlane = new CustomPlane(rightSplit, upSplit, origX, xY, constructedVector, 0.001);

        constructedVector = new Vector3D((xZ.getX()-100),xZ.getY(),(xZ.getZ() - 100));
        rightDownPlane = new CustomPlane(rightSplit, downSplit, xZ, xYZ, constructedVector, 0.001);
    }

    /**
     * Give all the planes of this split.
     * @param split
     * @return the planes of this split.
     */
    private CustomPlane[] getPlanes(AbstractSplit split) {
        CustomPlane[] planes = new CustomPlane[4];
        if (split == frontSplit) {
            planes[0] = frontDownPlane;
            planes[1] = frontUpPlane;
            planes[2] = frontLeftPlane;
            planes[3] = frontRightPlane;
        } else if (split == backSplit) {
            planes[0] = backDownPlane;
            planes[1] = backUpPlane;
            planes[2] = backLeftPlane;
            planes[3] = backRightPlane;
        } else if (split == leftSplit) {
            planes[0] = frontLeftPlane;
            planes[1] = leftUpPlane;
            planes[2] = backLeftPlane;
            planes[3] = leftDownPlane;
        } else if (split == rightSplit) {
            planes[0] = frontRightPlane;
            planes[1] = rightUpPlane;
            planes[2] = backRightPlane;
            planes[3] = rightDownPlane;
        } else if (split == upSplit) {
            planes[0] = frontUpPlane;
            planes[1] = rightUpPlane;
            planes[2] = backUpPlane;
            planes[3] = leftUpPlane;
        } else if (split == downSplit) {
            planes[0] = frontDownPlane;
            planes[1] = rightDownPlane;
            planes[2] = backDownPlane;
            planes[3] = leftDownPlane;
        }
        return planes;
    }

    /**
     * Create and set the edges of all the splits of the cube.
     */
    private void setEdges() {
        Vertex cornerOrig, cornerX, cornerY, cornerZ, cornerXY, cornerXZ, cornerYZ, cornerXYZ;
        cornerOrig = new Vertex(0,(float)x,(float)y,(float)z);
        cornerX = new Vertex(0,(float)width,(float)y,(float)z);
        cornerY = new Vertex(0,(float)x,(float)height,(float)z);
        cornerZ = new Vertex(0,(float)x,(float)y,(float)depth);
        cornerXY = new Vertex(0,(float)width,(float)height,(float)z);
        cornerXZ = new Vertex(0,(float)width,(float)y,(float)depth);
        cornerYZ = new Vertex(0,(float)x,(float)height,(float)depth);
        cornerXYZ = new Vertex(0,(float)width,(float)height,(float)depth);

        this.upSplit.setUpEdge(new Edge(false, cornerOrig, cornerX, 'X'));
        this.upSplit.setRightEdge(new Edge(false, cornerX, cornerXY, 'Y'));
        this.upSplit.setDownEdge(new Edge(true, cornerY, cornerXY, 'X'));
        this.upSplit.setLeftEdge(new Edge(true, cornerOrig, cornerY, 'Y'));

        this.downSplit.setUpEdge(new Edge(false, cornerYZ, cornerXYZ, 'X'));
        this.downSplit.setRightEdge(new Edge(true, cornerXZ, cornerXYZ, 'Y'));
        this.downSplit.setDownEdge(new Edge(true, cornerZ, cornerXZ, 'X'));
        this.downSplit.setLeftEdge(new Edge(false, cornerZ, cornerYZ, 'Y'));

        this.leftSplit.setUpEdge(new Edge(false, cornerOrig, cornerY, 'Y'));
        this.leftSplit.setRightEdge(new Edge(false, cornerY, cornerYZ, 'Z'));
        this.leftSplit.setDownEdge(new Edge(true, cornerZ, cornerYZ, 'Y'));
        this.leftSplit.setLeftEdge(new Edge(true, cornerOrig, cornerZ, 'Z'));

        this.rightSplit.setUpEdge(new Edge(true, cornerX, cornerXY, 'Y'));
        this.rightSplit.setRightEdge(new Edge(false, cornerX, cornerXZ, 'Z'));
        this.rightSplit.setDownEdge(new Edge(false, cornerXZ, cornerXYZ, 'Y'));
        this.rightSplit.setLeftEdge(new Edge(true, cornerXY, cornerXYZ, 'Z'));

        this.frontSplit.setUpEdge(new Edge(false, cornerY, cornerXY, 'X'));
        this.frontSplit.setRightEdge(new Edge(false, cornerXY, cornerXYZ, 'Z'));
        this.frontSplit.setDownEdge(new Edge(true, cornerYZ, cornerXYZ, 'X'));
        this.frontSplit.setLeftEdge(new Edge(true, cornerY, cornerYZ, 'Z'));

        this.backSplit.setUpEdge(new Edge(true, cornerOrig, cornerX, 'X'));
        this.backSplit.setRightEdge(new Edge(false, cornerOrig, cornerZ, 'Z'));
        this.backSplit.setDownEdge(new Edge(false, cornerZ, cornerXZ, 'X'));
        this.backSplit.setLeftEdge(new Edge(true, cornerX, cornerXZ, 'Z'));
    }

    /**
     * Give the next edge, that separate the two splits given in parameter.
     * @param previousSplit
     * @param nextSplit
     * @return the next edge.
     */
    private Edge[] getNextEdge(final AbstractSplit previousSplit, final AbstractSplit nextSplit) {
        Edge[] result = new Edge[2];
        if (previousSplit == this.upSplit) {
            result[1] = nextSplit.getUpEdge();
            if (nextSplit == this.frontSplit) {
                result[0] = previousSplit.getDownEdge();
            } else if (nextSplit == this.leftSplit) {
                result[0] = previousSplit.getLeftEdge();
            } else if (nextSplit == this.backSplit) {
                result[0] = previousSplit.getUpEdge();
            } else if (nextSplit == this.rightSplit) {
                result[0] = previousSplit.getRightEdge();
            }
        } else if (previousSplit == this.downSplit){
            if (nextSplit == this.frontSplit) {
                result[0] = previousSplit.getUpEdge();
            } else if (nextSplit == this.leftSplit) {
                result[0] = previousSplit.getLeftEdge();
            } else if (nextSplit == this.backSplit) {
                result[0] = previousSplit.getDownEdge();
            } else if (nextSplit == this.rightSplit) {
                result[0] = previousSplit.getRightEdge();
            }
            result[1] = nextSplit.getDownEdge();
        } else if (previousSplit == this.leftSplit){
            if (nextSplit == this.upSplit) {
                result[0] = previousSplit.getUpEdge();
                result[1] = nextSplit.getLeftEdge();
            } else if(nextSplit == this.downSplit) {
                result[0] = previousSplit.getDownEdge();
                result[1] = nextSplit.getLeftEdge();
            } else if(nextSplit == this.backSplit) {
                result[0] = previousSplit.getLeftEdge();
                result[1] = nextSplit.getRightEdge();
            } else if(nextSplit == this.frontSplit) {
                result[0] = previousSplit.getRightEdge();
                result[1] = nextSplit.getLeftEdge();
            }
        } else if (previousSplit == this.rightSplit){
            if (nextSplit == this.upSplit) {
                result[0] = previousSplit.getUpEdge();
                result[1] = nextSplit.getRightEdge();
            } else if(nextSplit == this.downSplit) {
                result[0] = previousSplit.getDownEdge();
                result[1] = nextSplit.getRightEdge();
            } else if(nextSplit == this.backSplit) {
                result[0] = previousSplit.getRightEdge();
                result[1] = nextSplit.getLeftEdge();
            } else if(nextSplit == this.frontSplit) {
                result[0] = previousSplit.getLeftEdge();
                result[1] = nextSplit.getRightEdge();
            }
        } else if (previousSplit == this.frontSplit){
            if (nextSplit == this.upSplit) {
                result[0] = previousSplit.getUpEdge();
                result[1] = nextSplit.getDownEdge();
            } else if(nextSplit == this.downSplit) {
                result[0] = previousSplit.getDownEdge();
                result[1] = nextSplit.getUpEdge();
            } else if(nextSplit == this.leftSplit) {
                result[0] = previousSplit.getLeftEdge();
                result[1] = nextSplit.getRightEdge();
            } else if(nextSplit == this.rightSplit) {
                result[0] = previousSplit.getRightEdge();
                result[1] = nextSplit.getLeftEdge();
            }
        } else if (previousSplit == this.backSplit){
            if (nextSplit == this.upSplit) {
                result[0] = previousSplit.getUpEdge();
                result[1] = nextSplit.getUpEdge();
            } else if(nextSplit == this.downSplit) {
                result[0] = previousSplit.getDownEdge();
                result[1] = nextSplit.getDownEdge();
            } else if(nextSplit == this.leftSplit) {
                result[0] = previousSplit.getRightEdge();
                result[1] = nextSplit.getLeftEdge();
            } else if(nextSplit == this.rightSplit) {
                result[0] = previousSplit.getLeftEdge();
                result[1] = nextSplit.getRightEdge();
            }
        }
        return result;
    }

    public WidthSplit getLeftSplit() {
        return leftSplit;
    }

    public WidthSplit getRightSplit() {
        return rightSplit;
    }

    public HeightSplit getBackSplit() {
        return backSplit;
    }

    public HeightSplit getFrontSplit() {
        return frontSplit;
    }

    public DepthSplit getDownSplit() {
        return downSplit;
    }

    public DepthSplit getUpSplit() {
        return upSplit;
    }
}
