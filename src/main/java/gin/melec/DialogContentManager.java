/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gin.melec;

import ij.IJ;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author tom
 */
public class DialogContentManager {

    /**
     * The filters for the obj files.
     */
    private static final FilenameFilter[] OBJ_FILTERS = new FilenameFilter[4];

    private static File WORKING_DIR;

    /**
     * List of meshes in the A_ part.
     */
    protected static final List<Mesh> A_MESHES = new ArrayList();
    /**
     * List of meshes in the B_ part.
     */
    protected static final List<Mesh> B_MESHES = new ArrayList();
    /**
     * List of meshes in the C_ part.
     */
    protected static final List<Mesh> C_MESHES = new ArrayList();
    /**
     * List of meshes in the D_ part.
     */
    protected static final List<Mesh> D_MESHES = new ArrayList();

    protected static final AbstractSplit WIDTH_SPLIT = new WidthSplit();
    protected static final AbstractSplit HEIGHT_SPLIT = new HeightSplit();

    protected static AbstractSplit ACTIVE_SPLIT;

    protected static Mesh ACTIVE_MESH_1;

    protected static Mesh ACTIVE_MESH_2;

    static {
        setObjFilters();
    }

    public static void setWorkingDir(final String dir) {
        WORKING_DIR = new File(dir);
        getFiles(OBJ_FILTERS);
    }

    private static void getFiles(final FilenameFilter[] objFilters) {
        File[] listing;
        A_MESHES.clear();
        B_MESHES.clear();
        C_MESHES.clear();
        D_MESHES.clear();

        listing = WORKING_DIR.listFiles(objFilters[0]);
        for (File file : listing) {
            if (file.isFile()) {
                try {
                    final Mesh mesh = new Mesh(file);
                    A_MESHES.add(mesh);
                } catch (IOException ex) {
                    IJ.handleException(ex);
                }
            }
        }
        listing = WORKING_DIR.listFiles(objFilters[1]);
        for (File file : listing) {
            if (file.isFile()) {
                try {
                    final Mesh mesh = new Mesh(file);
                    B_MESHES.add(mesh);
                } catch (IOException ex) {
                    IJ.handleException(ex);
                }
            }
        }
        listing = WORKING_DIR.listFiles(objFilters[2]);
        for (File file : listing) {
            if (file.isFile()) {
                try {
                    final Mesh mesh = new Mesh(file);
                    C_MESHES.add(mesh);
                } catch (IOException ex) {
                    IJ.handleException(ex);
                }
            }
        }
        listing = WORKING_DIR.listFiles(objFilters[3]);
        for (File file : listing) {
            if (file.isFile()) {
                try {
                    final Mesh mesh = new Mesh(file);
                    D_MESHES.add(mesh);
                } catch (IOException ex) {
                    IJ.handleException(ex);
                }
            }
        }
    }

    /**
     * This method create FilenameFilters we need to filter the .obj files with
     * the prefix A_, B_, C_ or D_.
     *
     * @return the filenameFilters.
     */
    public static void setObjFilters() {
        OBJ_FILTERS[0] = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                return lowercaseName.endsWith(".obj")
                        && name.matches("A_.*");
            }
        };
        OBJ_FILTERS[1] = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                return lowercaseName.endsWith(".obj")
                        && name.matches("B_.*");
            }
        };
        OBJ_FILTERS[2] = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                return lowercaseName.endsWith(".obj")
                        && name.matches("C_.*");
            }
        };
        OBJ_FILTERS[3] = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                return lowercaseName.endsWith(".obj")
                        && name.matches("D_.*");
            }
        };
    }

    protected static void setSplits(double x, double y) {
        //ALL_SPLITS.clear();
        if (x != 0) {
            WIDTH_SPLIT.setPosition(x - 0.5);
        }
        if (y != 0) {
            HEIGHT_SPLIT.setPosition(y - 0.5);
        }

    }

    protected static boolean setActiveSplits(final String obj1, final String obj2) {
        ACTIVE_MESH_1 = getMesh(obj1);
        ACTIVE_MESH_2 = getMesh(obj2);

        if (!ACTIVE_MESH_1.isMoved() || !ACTIVE_MESH_2.isMoved()) {
            IJ.error("At least one of the mesh haven't been shifted.\n"
                    + "Please use the Shift function of the plugin");
            CustomFrame.appendToLog("Merging aborted");
            return false;
        }
        if ((A_MESHES.contains(ACTIVE_MESH_1) && B_MESHES.contains(ACTIVE_MESH_2))
                || (B_MESHES.contains(ACTIVE_MESH_1) && A_MESHES.contains(ACTIVE_MESH_2))
                || (C_MESHES.contains(ACTIVE_MESH_1) && D_MESHES.contains(ACTIVE_MESH_2))
                || (D_MESHES.contains(ACTIVE_MESH_1) && C_MESHES.contains(ACTIVE_MESH_2))) {
            ACTIVE_SPLIT = WIDTH_SPLIT;
        } else {
            ACTIVE_SPLIT = HEIGHT_SPLIT;
        } // No other control needed since the meshes added to the merge must
            // already correspond.
        return true;
    }

    /**
     * Return the mesh given by it's name.
     *
     * @param meshName , the name of the mesh we search.
     * @param allMeshes , the list containing the meshes.
     * @return the meshes with the given name.
     */
    private static Mesh getMesh(String meshName) {
        List<List> allMesh = new ArrayList();
        allMesh.add(A_MESHES);
        allMesh.add(B_MESHES);
        allMesh.add(C_MESHES);
        allMesh.add(D_MESHES);

        Mesh result = null;
        for (List<Mesh> listMesh : allMesh) {
            for (Mesh mesh : listMesh) {
                if (mesh.toString().equals(meshName)) {
                    result = mesh;
                    break;
                }
            }
        }
        return result;
    }
}
