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

import ij.IJ;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Plane;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Cube {
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
    private Plane plane;

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
        meshes = new ArrayList<Mesh>();
    }

    protected boolean isCrossedEdge(final Vertex v1, final Vertex v2) {
        Vector3D vect1 = new Vector3D(v1.getX(), v1.getY(), v1.getZ());
        Vector3D vect2 = new Vector3D(v2.getX(), v2.getY(), v2.getZ());
        Line line = new Line(vect1, vect2, 0.001);

        Vector3D intersection = this.plane.intersection(line);
        boolean result;
        if (intersection == null) {
            result = false;
        } else {
            result = vect1.distance(vect2) > vect1.distance(intersection);
        }
        return result;
    }

    private void setPlane(final AbstractSplit newSplit,
            final AbstractSplit oldSplit) {
        // 7 vertex are enough to create all the planes
        // Draw the cube and vertex to understand the planes created
        Vector3D orig= new Vector3D(x,y,z);
        Vector3D origX= new Vector3D(x + width,y,z);
        Vector3D origY= new Vector3D(x,y + height,z);
        Vector3D origZ= new Vector3D(x,y,z + depth);
        Vector3D xZ = new Vector3D(x + width, y,z + depth);
        Vector3D xY = new Vector3D(x + width,y + height, z);
        Vector3D yZ = new Vector3D(x, y + height, z + depth);
        Vector3D xYZ = new Vector3D(x + width, y + height, z + depth);
        //leftSplit && downSplit
        if ((newSplit == leftSplit || oldSplit == leftSplit) && (newSplit == downSplit || oldSplit == downSplit)) {
            Vector3D constructedVector = new Vector3D((origZ.getX()+100),origZ.getY(),(origZ.getZ() - 100));
            this.plane = new Plane(origZ, yZ, constructedVector, 0.001);
        }
        //backSplit && downSplit
        else if ((newSplit == backSplit || oldSplit == backSplit) && (newSplit == downSplit || oldSplit == downSplit)) {
            Vector3D constructedVector = new Vector3D(origZ.getX(),(origZ.getY()+100),(origZ.getZ() - 100));
            this.plane = new Plane(origZ, xZ, constructedVector, 0.001);
        }
        //rightSplit && downSplit
        else if ((newSplit == rightSplit || oldSplit == rightSplit) && (newSplit == downSplit || oldSplit == downSplit)) {
            Vector3D constructedVector = new Vector3D((xZ.getX()-100),xZ.getY(),(xZ.getZ() - 100));
            this.plane = new Plane(xZ, xYZ, constructedVector, 0.001);
        }
        //frontSplit && downSplit
        else if ((newSplit == frontSplit || oldSplit == frontSplit) && (newSplit == downSplit || oldSplit == downSplit)) {
            Vector3D constructedVector = new Vector3D(yZ.getX(),(yZ.getY()-100),(yZ.getZ() - 100));
            this.plane = new Plane(yZ, xYZ, constructedVector, 0.001);
        }
        //leftSplit && backSplit
        else if ((newSplit == leftSplit || oldSplit == leftSplit) && (newSplit == backSplit || oldSplit == backSplit)) {
            Vector3D constructedVector = new Vector3D((orig.getX()+100),(orig.getY()+100),orig.getZ());
            this.plane = new Plane(orig, origZ, constructedVector, 0.001);
        }
        //backSplit && rightSplit
        else if ((newSplit == backSplit || oldSplit == backSplit) && (newSplit == rightSplit || oldSplit == rightSplit)) {
            Vector3D constructedVector = new Vector3D((origX.getX()-100),(origX.getY()+100),origX.getZ());
            this.plane = new Plane(origX, xZ, constructedVector, 0.001);
        }
        //rightSplit && frontSplit
        else if ((newSplit == rightSplit || oldSplit == rightSplit) && (newSplit == frontSplit || oldSplit == frontSplit)) {
            Vector3D constructedVector = new Vector3D((xY.getX()-100),(xY.getY()-100),xY.getZ());
            this.plane = new Plane(xY, xYZ, constructedVector, 0.001);
        }
        //frontSplit && leftSplit
        else if ((newSplit == frontSplit || oldSplit == frontSplit) && (newSplit == leftSplit || oldSplit == leftSplit)) {
            Vector3D constructedVector = new Vector3D((origY.getX()-100),(origY.getY()+100),origY.getZ());
            this.plane = new Plane(origY, yZ, constructedVector, 0.001);
        }
        //leftSplit && upSplit
        else if ((newSplit == leftSplit || oldSplit == leftSplit) && (newSplit == upSplit || oldSplit == upSplit)) {
            Vector3D constructedVector = new Vector3D((orig.getX()+100),orig.getY(),(orig.getZ() + 100));
            this.plane = new Plane(orig, origY, constructedVector, 0.001);
        }
        //backSplit && upSplit
        else if ((newSplit == backSplit || oldSplit == backSplit) && (newSplit == upSplit || oldSplit == upSplit)) {
            Vector3D constructedVector = new Vector3D(orig.getX(),(orig.getY()+100),(orig.getZ() + 100));
            this.plane = new Plane(orig, origX, constructedVector, 0.001);
        }
        //rightSplit && upSplit
        else if ((newSplit == rightSplit || oldSplit == rightSplit) && (newSplit == upSplit || oldSplit == upSplit)) {
            Vector3D constructedVector = new Vector3D((origX.getX()-100),origX.getY(),(origX.getZ() + 100));
            this.plane = new Plane(origX, xY, constructedVector, 0.001);
        }
        //frontSplit && upSplit
        else if ((newSplit == frontSplit || oldSplit == frontSplit) && (newSplit == upSplit || oldSplit == upSplit)) {
            Vector3D constructedVector = new Vector3D(origY.getX(),(origY.getY()-100),(origY.getZ() + 100));
            this.plane = new Plane(origY, xY, constructedVector, 0.001);
        }
    }

    protected void addMesh(final Mesh mesh) {
        meshes.add(mesh);
    }
    protected void addAllMesh(final List meshes) {
        this.meshes.addAll(meshes);
    }

    protected void detectMeshBorders() {
        try {
            for (Mesh mesh : meshes) {
                mesh.importMesh();
            }
        } catch (ParseException ex) {
            IJ.handleException(ex);
        } catch (IOException ex) {
            IJ.handleException(ex);
        }
        for (Mesh mesh : meshes) {
            mesh.createPrimers(leftSplit);
            mesh.createPrimers(rightSplit);
            mesh.createPrimers(backSplit);
            mesh.createPrimers(frontSplit);
            mesh.createPrimers(downSplit);
            mesh.createPrimers(upSplit);

            mesh.createBorders(leftSplit);
            mesh.createBorders(rightSplit);
            mesh.createBorders(backSplit);
            mesh.createBorders(frontSplit);
            mesh.createBorders(downSplit);
            mesh.createBorders(upSplit);
        }
    }

    protected void prepareMeshBorders() {
        for (Mesh mesh : meshes) {
            List<FlatBorder> tmpBorders = new ArrayList();
            for (Border currentBorder : mesh.getBorders()) {
                separateBorder(currentBorder, mesh);
            }
            this.storeFlatBorders(mesh);
            setFlatsToMesh(mesh);
            computeFlatProperties(mesh);
        }

    }
    private void computeFlatProperties(final Mesh mesh) {
        mesh.computeLeftFlatProperties(this.leftSplit);
        mesh.computeRightFlatProperties(this.rightSplit);
        mesh.computeUpFlatProperties(this.upSplit);
        mesh.computeDownFlatProperties(this.downSplit);
        mesh.computeFrontFlatProperties(this.frontSplit);
        mesh.computeBackFlatProperties(this.backSplit);
    }

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

    private void separateBorder(Border currentBorder, Mesh mesh) {
        List<FlatBorder> result = new ArrayList();
        List<Border> bordersList = new ArrayList();
        AbstractSplit currSplit = currentBorder.getSplit();
        List<Vertex> vertexList = new ArrayList(currentBorder.getVertexSequence());
        int startSequence = 0;
        int endSequence;

        Edge edge = null;

        for (int i = 0; i < vertexList.size(); i++) {
            if (!currSplit.isClose(vertexList.get(i))) {
                int j = i;
                AbstractSplit nextSplit = getNextSplit(vertexList, j);
                if (nextSplit != null) {
                    // if no split is available no need to create a subborder
                    Edge[] nextEdge = null;
                    if (!((currSplit instanceof WidthSplit && nextSplit instanceof WidthSplit) ||
                            (currSplit instanceof HeightSplit && nextSplit instanceof HeightSplit) ||
                            (currSplit instanceof DepthSplit && nextSplit instanceof DepthSplit))) {
                        setPlane(currSplit, nextSplit);
                        endSequence = getIndexEndBorder(vertexList, j);
                        nextEdge = getNextEdge(currSplit, nextSplit);
                        Border border = new Border(vertexList, startSequence, endSequence, nextEdge[0]);
                        border.setSplit(currSplit);
                        border.getConnector().setVertex(border.getFirstVertex());
                        if (edge != null) {
                            edge.addConnector(border.getConnector());
                        }
                        bordersList.add(border);
                        startSequence = endSequence;
                    }
                    edge = nextEdge[1];
                    currSplit = nextSplit;
                }

            }
        }
        if ((startSequence+1) < vertexList.size()) {
            if (bordersList.size() > 0) {
                Border border = bordersList.get(0);
                border.addStartingSequence(vertexList.subList(startSequence, vertexList.size()));
                border.setSplit(currSplit);
                border.getConnector().setVertex(border.getFirstVertex());
                edge.addConnector(border.getConnector());
            } else {
                // Its only on one split so we transform it directly onto a flat border
                FlatBorder flat = new FlatBorder(mesh);
                flat.addElement(vertexList);
                currSplit.addFlatBorder(flat);
            }
        }
    }

    private void storeFlatBorders(Mesh mesh) {
        frontSplit.storeFlatBorders(mesh);
        backSplit.storeFlatBorders(mesh);
        upSplit.storeFlatBorders(mesh);
        downSplit.storeFlatBorders(mesh);
        leftSplit.storeFlatBorders(mesh);
        rightSplit.storeFlatBorders(mesh);
    }

    private int getIndexEndBorder(List<Vertex> vertexList, int startingPosition) {
        int result;
        int crossBefore = -1, crossAfter = -1;
        int limitInf = startingPosition - 20;
        if (limitInf < 0) {
            limitInf = 0;
        }
        int limitSup = startingPosition + 20;
        if (limitSup >= vertexList.size()) {
            limitSup = vertexList.size()-1;
        }
        int j = startingPosition;
        while (j >= limitInf) {
            if (isCrossedEdge(vertexList.get(j), vertexList.get(j-1))) {
                crossBefore = j;
                break;
            }
            j--;
        }
        j = startingPosition;
        while (j < limitSup) {
            if (isCrossedEdge(vertexList.get(j), vertexList.get(j+1))) {
                crossAfter = j;
                break;
            }
            j++;
        }
        if (crossBefore != -1) {
            if (crossAfter != -1) {
                if (Math.abs(startingPosition - crossBefore) >= Math.abs(startingPosition - crossAfter)) {
                    result = crossAfter;
                } else {
                    result = crossBefore;
                }
            } else {
                result = crossBefore;
            }
        } else if (crossAfter != -1) {
            result = crossAfter;
        } else {
            result = startingPosition;
        }
        return result;
    }

    private AbstractSplit getNextSplit(List<Vertex> vertexList, int j) {
        AbstractSplit result = null;
        List<AbstractSplit> candidates = new ArrayList();
        if (leftSplit.isClose(vertexList.get(j))) candidates.add(leftSplit);
        if (rightSplit.isClose(vertexList.get(j))) candidates.add(rightSplit);
        if (backSplit.isClose(vertexList.get(j))) candidates.add(backSplit);
        if (frontSplit.isClose(vertexList.get(j))) candidates.add(frontSplit);
        if (downSplit.isClose(vertexList.get(j))) candidates.add(downSplit);
        if (upSplit.isClose(vertexList.get(j))) candidates.add(upSplit);

        if (candidates.isEmpty()) {
            result = null;
        }
        else if (candidates.size() == 1) {
            // Only one close, no conflicts
            result = candidates.get(0);
        } else if (candidates.size() > 1) {
            // Try to differenciate them on the distance, with a tolerance (0.5).
            double distanceMin = -1;
            for (AbstractSplit currCdt: candidates) {
                if (distanceMin == -1
                        || currCdt.distanceTo(vertexList.get(j)) < distanceMin) {
                    distanceMin = currCdt.distanceTo(vertexList.get(j));
                }
            }
            List<AbstractSplit> tmp = new ArrayList();
            for (AbstractSplit currCdt: candidates) {
                if (currCdt.distanceTo(vertexList.get(j)) <= distanceMin + 0.5) {
                    tmp.add(currCdt);
                }
            }
            candidates = tmp;
            if (candidates.size() == 1) {
                result = candidates.get(0);
            } else {
                // Differenciate them on the vertex sequence.
                for (Vertex v: vertexList) {
                    for (AbstractSplit cdt:candidates) {
                        if(!cdt.isClose(v)) {
                            candidates.remove(cdt);
                        }
                    }
                    if (candidates.size() == 1) {
                        break;
                        // Hope we won't remove all the candidates at the same time...
                    }
                }
                result = candidates.get(0);
            }
        }
        return result;
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

    public List<Mesh> getMeshes() {
        return meshes;
    }
}
