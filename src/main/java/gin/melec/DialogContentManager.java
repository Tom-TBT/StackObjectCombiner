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

    public static boolean USE_NAME_PATTERN = false;

    public static double MEMORY_WATCHER = 400; // the millions correspond to Megabytes

    /**
     * The filters for the obj files.
     */
    private static final FilenameFilter[] OBJ_FILTERS = new FilenameFilter[8];

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
    /**
     * List of meshes in the E_ part.
     */
    protected static final List<Mesh> E_MESHES = new ArrayList();
    /**
     * List of meshes in the F_ part.
     */
    protected static final List<Mesh> F_MESHES = new ArrayList();
    /**
     * List of meshes in the F_ part.
     */
    protected static final List<Mesh> G_MESHES = new ArrayList();
    /**
     * List of meshes in the H_ part.
     */
    protected static final List<Mesh> H_MESHES = new ArrayList();

    protected static Cube CUBE_A;
    protected static Cube CUBE_B;
    protected static Cube CUBE_C;
    protected static Cube CUBE_D;
    protected static Cube CUBE_E;
    protected static Cube CUBE_F;
    protected static Cube CUBE_G;
    protected static Cube CUBE_H;

    protected static final AbstractSplit WIDTH_SPLIT = new WidthSplit();
    protected static final AbstractSplit HEIGHT_SPLIT = new HeightSplit();
    protected static final AbstractSplit DEPTH_SPLIT = new DepthSplit();

    protected static AbstractSplit ACTIVE_SPLIT;

    protected static Mesh ACTIVE_MESH_1;

    protected static Mesh ACTIVE_MESH_2;

    public static void setWorkingDir(final String dir) {
        WORKING_DIR = new File(dir);
        setObjFilters();
        getFiles(OBJ_FILTERS);
    }

    private static void getFiles(final FilenameFilter[] objFilters) {
        File[] listing;
        A_MESHES.clear();
        B_MESHES.clear();
        C_MESHES.clear();
        D_MESHES.clear();
        E_MESHES.clear();
        F_MESHES.clear();
        G_MESHES.clear();
        H_MESHES.clear();

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
        listing = WORKING_DIR.listFiles(objFilters[4]);
        for (File file : listing) {
            if (file.isFile()) {
                try {
                    final Mesh mesh = new Mesh(file);
                    E_MESHES.add(mesh);
                } catch (IOException ex) {
                    IJ.handleException(ex);
                }
            }
        }
        listing = WORKING_DIR.listFiles(objFilters[5]);
        for (File file : listing) {
            if (file.isFile()) {
                try {
                    final Mesh mesh = new Mesh(file);
                    F_MESHES.add(mesh);
                } catch (IOException ex) {
                    IJ.handleException(ex);
                }
            }
        }
        listing = WORKING_DIR.listFiles(objFilters[6]);
        for (File file : listing) {
            if (file.isFile()) {
                try {
                    final Mesh mesh = new Mesh(file);
                    G_MESHES.add(mesh);
                } catch (IOException ex) {
                    IJ.handleException(ex);
                }
            }
        }
        listing = WORKING_DIR.listFiles(objFilters[7]);
        for (File file : listing) {
            if (file.isFile()) {
                try {
                    final Mesh mesh = new Mesh(file);
                    H_MESHES.add(mesh);
                } catch (IOException ex) {
                    IJ.handleException(ex);
                }
            }
        }
    }

    public static void unloadMeshes() {
        List<Mesh> allMeshes = new ArrayList();
        allMeshes.addAll(CUBE_A.getMeshes());
        allMeshes.addAll(CUBE_B.getMeshes());
        allMeshes.addAll(CUBE_C.getMeshes());
        allMeshes.addAll(CUBE_D.getMeshes());
        allMeshes.addAll(CUBE_E.getMeshes());
        allMeshes.addAll(CUBE_F.getMeshes());
        allMeshes.addAll(CUBE_G.getMeshes());
        allMeshes.addAll(CUBE_H.getMeshes());
        for (Mesh mesh:allMeshes) {
            mesh.unload();
        }
        Runtime.getRuntime().gc();
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
                String pattern = CustomFrame.getNamePattern().toLowerCase();
                return lowercaseName.endsWith(".obj")
                        && lowercaseName.matches("a_.*")
                        && (!USE_NAME_PATTERN || lowercaseName.matches(".*"+pattern+".*"));
            }
        };
        OBJ_FILTERS[1] = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                String pattern = CustomFrame.getNamePattern().toLowerCase();
                return lowercaseName.endsWith(".obj")
                        && lowercaseName.matches("b_.*")
                        && (!USE_NAME_PATTERN || lowercaseName.matches(".*"+pattern+".*"));
            }
        };
        OBJ_FILTERS[2] = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                String pattern = CustomFrame.getNamePattern().toLowerCase();
                return lowercaseName.endsWith(".obj")
                        && lowercaseName.matches("c_.*")
                        && (!USE_NAME_PATTERN || lowercaseName.matches(".*"+pattern+".*"));
            }
        };
        OBJ_FILTERS[3] = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                String pattern = CustomFrame.getNamePattern().toLowerCase();
                return lowercaseName.endsWith(".obj")
                        && lowercaseName.matches("d_.*")
                        && (!USE_NAME_PATTERN || lowercaseName.matches(".*"+pattern+".*"));
            }
        };
        OBJ_FILTERS[4] = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                String pattern = CustomFrame.getNamePattern().toLowerCase();
                return lowercaseName.endsWith(".obj")
                        && lowercaseName.matches("e_.*")
                        && (!USE_NAME_PATTERN || lowercaseName.matches(".*"+pattern+".*"));
            }
        };
        OBJ_FILTERS[5] = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                String pattern = CustomFrame.getNamePattern().toLowerCase();
                return lowercaseName.endsWith(".obj")
                        && lowercaseName.matches("f_.*")
                        && (!USE_NAME_PATTERN || lowercaseName.matches(".*"+pattern+".*"));
            }
        };
        OBJ_FILTERS[6] = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                String pattern = CustomFrame.getNamePattern().toLowerCase();
                return lowercaseName.endsWith(".obj")
                        && lowercaseName.matches("g_.*")
                        && (!USE_NAME_PATTERN || lowercaseName.matches(".*"+pattern+".*"));
            }
        };
        OBJ_FILTERS[7] = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                String pattern = CustomFrame.getNamePattern().toLowerCase();
                return lowercaseName.endsWith(".obj")
                        && lowercaseName.matches("h_.*")
                        && (!USE_NAME_PATTERN || lowercaseName.matches(".*"+pattern+".*"));
            }
        };
    }

    protected static void setSplits(double x, double y, double z) {
        //ALL_SPLITS.clear();
        if (x != 0) {
            WIDTH_SPLIT.setPosition(x - 0.5);
        }
        if (y != 0) {
            HEIGHT_SPLIT.setPosition(y - 0.5);
        }
        if (z != 0) {
            DEPTH_SPLIT.setPosition(z - 0.5);
        }

    }

    protected static boolean setActiveSplit(final String obj1, final String obj2) {
        ACTIVE_MESH_1 = getMesh(obj1);
        ACTIVE_MESH_2 = getMesh(obj2);

        if (ACTIVE_MESH_1== null || ACTIVE_MESH_2== null) {
            return false;
        }

        if (!ACTIVE_MESH_1.isMoved() || !ACTIVE_MESH_2.isMoved()) {
            IJ.error("At least one of the mesh haven't been shifted.\n"
                    + "Please use the Shift function of the plugin");
            CustomFrame.appendToLog("Merging aborted");
            return false;
        }
        if ((A_MESHES.contains(ACTIVE_MESH_1) && B_MESHES.contains(ACTIVE_MESH_2))
                || (B_MESHES.contains(ACTIVE_MESH_1) && A_MESHES.contains(ACTIVE_MESH_2))
                || (C_MESHES.contains(ACTIVE_MESH_1) && D_MESHES.contains(ACTIVE_MESH_2))
                || (D_MESHES.contains(ACTIVE_MESH_1) && C_MESHES.contains(ACTIVE_MESH_2))
                || (E_MESHES.contains(ACTIVE_MESH_1) && F_MESHES.contains(ACTIVE_MESH_2))
                || (F_MESHES.contains(ACTIVE_MESH_1) && E_MESHES.contains(ACTIVE_MESH_2))
                || (G_MESHES.contains(ACTIVE_MESH_1) && H_MESHES.contains(ACTIVE_MESH_2))
                || (H_MESHES.contains(ACTIVE_MESH_1) && G_MESHES.contains(ACTIVE_MESH_2))) {
            ACTIVE_SPLIT = WIDTH_SPLIT;
        } else if ((A_MESHES.contains(ACTIVE_MESH_1) && C_MESHES.contains(ACTIVE_MESH_2))
                || (B_MESHES.contains(ACTIVE_MESH_1) && D_MESHES.contains(ACTIVE_MESH_2))
                || (C_MESHES.contains(ACTIVE_MESH_1) && A_MESHES.contains(ACTIVE_MESH_2))
                || (D_MESHES.contains(ACTIVE_MESH_1) && B_MESHES.contains(ACTIVE_MESH_2))
                || (E_MESHES.contains(ACTIVE_MESH_1) && G_MESHES.contains(ACTIVE_MESH_2))
                || (G_MESHES.contains(ACTIVE_MESH_1) && E_MESHES.contains(ACTIVE_MESH_2))
                || (F_MESHES.contains(ACTIVE_MESH_1) && H_MESHES.contains(ACTIVE_MESH_2))
                || (H_MESHES.contains(ACTIVE_MESH_1) && F_MESHES.contains(ACTIVE_MESH_2))){
            ACTIVE_SPLIT = HEIGHT_SPLIT;
        } else {
            ACTIVE_SPLIT = DEPTH_SPLIT;
        }
            // No other control needed since the meshes added to the merge must
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
        allMesh.add(E_MESHES);
        allMesh.add(F_MESHES);
        allMesh.add(G_MESHES);
        allMesh.add(H_MESHES);

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

    public static void generateCubes(double xPos, double yPos, double zPos, double width,
            double height, double depth) {
        // Here we displace from half a pixel the different positions
        double basePos = -0.5;
        xPos -= 0.5;
        yPos -= 0.5;
        zPos -= 0.5;
        width -= 0.5;
        height -= 0.5;
        depth -= 0.5;

        // 0.5 values are added/substracted to place the split between the cubes
        CUBE_A = new Cube(basePos,basePos,basePos,xPos,yPos,zPos);
        CUBE_A.addAllMesh(A_MESHES);
        CUBE_B = new Cube(xPos,basePos,basePos,width,yPos,zPos);
        CUBE_B.addAllMesh(B_MESHES);
        CUBE_C = new Cube(basePos,yPos,basePos,xPos,height,zPos);
        CUBE_C.addAllMesh(C_MESHES);
        CUBE_D = new Cube(xPos,yPos,basePos,width,height,zPos);
        CUBE_D.addAllMesh(D_MESHES);
        CUBE_E = new Cube(basePos,basePos,zPos,xPos,yPos,depth);
        CUBE_E.addAllMesh(E_MESHES);
        CUBE_F = new Cube(xPos,basePos,zPos,width,yPos,depth);
        CUBE_F.addAllMesh(F_MESHES);
        CUBE_G = new Cube(basePos,yPos,zPos,xPos,height,depth);
        CUBE_G.addAllMesh(G_MESHES);
        CUBE_H = new Cube(xPos,yPos,zPos,width,height,depth);
        CUBE_H.addAllMesh(H_MESHES);
    }
}
