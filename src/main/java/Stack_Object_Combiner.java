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

import gin.melec.AbstractSplit;
import gin.melec.Mesh;
import gin.melec.MeshMerger;
import gin.melec.MeshMover;
import gin.melec.SplitDown;
import gin.melec.SplitLeft;
import gin.melec.SplitRight;
import gin.melec.SplitUp;

import ij.IJ;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.Prefs;
import java.io.File;
import java.io.FilenameFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Stack_Object_Combiner implements PlugIn {

    /**
     * List of splits in the A_ part.
     */
    private static final List<AbstractSplit> A_SPLITS = new ArrayList();
    /**
     * List of splits in the B_ part.
     */
    private static final List<AbstractSplit> B_SPLITS = new ArrayList();
    /**
     * List of splits in the C_ part.
     */
    private static final List<AbstractSplit> C_SPLITS = new ArrayList();
    /**
     * List of splits in the D_ part.
     */
    private static final List<AbstractSplit> D_SPLITS = new ArrayList();

    /**
     * List of meshes in the A_ part.
     */
    private static final List<Mesh> A_MESHES = new ArrayList();
    /**
     * List of meshes in the B_ part.
     */
    private static final List<Mesh> B_MESHES = new ArrayList();
    /**
     * List of meshes in the C_ part.
     */
    private static final List<Mesh> C_MESHES = new ArrayList();
    /**
     * List of meshes in the D_ part.
     */
    private static final List<Mesh> D_MESHES = new ArrayList();
    /**
     * List of all meshes (A part + B part + C part + D part).
     */
    private static final List<List> ALL_MESHES = new ArrayList();

    /**
     * The start of the plugin.
     *
     * @param arg , parameter for the plugin.
     */
    @Override
    public final void run(final String arg) {
        final FilenameFilter[] objFilters = getFilters();

        final File workingDirectory = new File(IJ.getDirectory("Give the folder"
                + " containing the .obj files"));
        if (getSplits(workingDirectory, objFilters)) {
            getMeshes(workingDirectory, objFilters);
            boolean notCanceled = true;
            while (notCanceled) {
                notCanceled = proposeAction();
            }
        }

    }

    /**
     * This method create FilenameFilters we need to filter the .obj files with
     * the prefix A_, B_, C_ or D_.
     *
     * @return the filenameFilters.
     */
    private FilenameFilter[] getFilters() {
        FilenameFilter[] result = new FilenameFilter[4];
        result[0] = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(".obj")
                        && name.startsWith("A_")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        result[1] = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(".obj")
                        && name.startsWith("B_")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        result[2] = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(".obj")
                        && name.startsWith("C_")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        result[3] = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(".obj")
                        && name.startsWith("D_")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        return result;
    }

    /**
     * Ask to the user if he want to replace the meshes or if he want merge two
     * meshes. The user can also cancel the dialog to quit the plugin.
     *
     * @return true if the user choosed an action, else return false because the
     * user choosed to canceled the plugin.
     */
    private boolean proposeAction() {
        boolean result;
        final GenericDialog gDial = new GenericDialog("Stack Object Combiner");
        gDial.addMessage("Choose the action to perform");
        gDial.enableYesNoCancel("Move meshes", "Merge two meshes");
        gDial.showDialog();
        if (gDial.wasCanceled()) {
            result = false;
        } else if (gDial.wasOKed()) {
            MeshMover.moveMeshes(ALL_MESHES);
            result = true;
        } else {
            IJ.showMessage("On construction !");
            //MeshMerger.work(ALL_MESHES);
            result = true;
        }
        return result;
    }

    /**
     * Open a dialog box that ask for the position of the splits.
     *
     * @param workingDirectory , the current directory.
     * @param objFilters , the filters of our files.
     * @return true if the panel hasn't been canceled.
     */
    private boolean getSplits(final File workingDirectory,
            final FilenameFilter[] objFilters) {
        Prefs.set("my.persistent.name", "Grizzly Adams");
        long verticalSplit = Math.round(Prefs.get("SOC.verticalSplit", 0));
        long horizontalSplit = Math.round(Prefs.get("SOC.horizontalSplit", 0));
        boolean result = true;
        boolean verticalExist = false, horizontalExist = false;
        final GenericDialog gDial = new GenericDialog("Indicate the positions "
                + "of the splits.");

        // TODO think to a control
        File[] listing;
        listing = workingDirectory.listFiles(objFilters[3]); // Filter D_
        for (File file : listing) {
            if (file.isFile()) { // If files in D exists
                gDial.addNumericField("Vertical split", verticalSplit, 0);
                gDial.addNumericField("Horizontal split", horizontalSplit, 0);
                verticalExist = true;
                horizontalExist = true;
                break;
            }
        }
        if (!verticalExist && !horizontalExist) { // If their was no D_ file
            listing = workingDirectory.listFiles(objFilters[1]); // Filter B_
            for (File file : listing) {
                if (file.isFile()) { // If files in B exists
                    gDial.addNumericField("Vertical split", verticalSplit, 0);
                    verticalExist = true;
                    break;
                }
            }
            listing = workingDirectory.listFiles(objFilters[2]); // Filter C_
            for (File file : listing) {
                if (file.isFile()) { // If files in C exists
                    gDial.addNumericField("Horizontal split",
                            horizontalSplit, 0);
                    horizontalExist = true;
                    break;
                }
            }
        }

        if (!horizontalExist && !verticalExist) {
            gDial.addMessage("No meshes can be found.\n"
                    + "Check the documentation for more informations.");
            result = false;
        }
        gDial.showDialog();
        if (gDial.wasCanceled()) {
            result = false;
        } else {
            if (verticalExist) {
                verticalSplit = (int) gDial.getNextNumber();
                Prefs.set("SOC.verticalSplit", verticalSplit);
                A_SPLITS.add(new SplitRight(verticalSplit));
                B_SPLITS.add(new SplitLeft(verticalSplit));
                C_SPLITS.add(new SplitRight(verticalSplit));
                D_SPLITS.add(new SplitLeft(verticalSplit));
            }
            if (horizontalExist) {
                horizontalSplit = (int) gDial.getNextNumber();
                Prefs.set("SOC.horizontalSplit", horizontalSplit);
                A_SPLITS.add(new SplitDown(horizontalSplit));
                B_SPLITS.add(new SplitDown(horizontalSplit));
                C_SPLITS.add(new SplitUp(horizontalSplit));
                D_SPLITS.add(new SplitUp(horizontalSplit));
            }
            Prefs.savePreferences();
        }
        return result;
    }

    /**
     * Add to the meshes lists the meshes contained in the given directory.
     *
     * @param workingDirectory , the directory containing the .obj files.
     * @param objFilters , the filters for our files.
     */
    private void getMeshes(final File workingDirectory,
            final FilenameFilter[] objFilters) {
        File[] listing;
            listing = workingDirectory.listFiles(objFilters[0]); // Filter A_
            for (File file : listing) {
                if (file.isFile()) {
                    final Mesh mesh = new Mesh(A_SPLITS, file);
                    A_MESHES.add(mesh);
                }

            }
            ALL_MESHES.add(A_MESHES);

            listing = workingDirectory.listFiles(objFilters[1]); // Filter B_
            for (File file : listing) {
                if (file.isFile()) {
                    final Mesh mesh = new Mesh(B_SPLITS, file);
                    B_MESHES.add(mesh);
                }

            }
            ALL_MESHES.add(B_MESHES);

            listing = workingDirectory.listFiles(objFilters[2]); // Filter C_
            for (File file : listing) {
                if (file.isFile()) {
                    final Mesh mesh = new Mesh(C_SPLITS, file);
                    C_MESHES.add(mesh);
                }

            }
            ALL_MESHES.add(C_MESHES);

            listing = workingDirectory.listFiles(objFilters[3]); // Filter D_
            for (File file : listing) {
                if (file.isFile()) {
                    final Mesh mesh = new Mesh(D_SPLITS, file);
                    D_MESHES.add(mesh);
                }
            }
            ALL_MESHES.add(D_MESHES);
    }

    /**
     * Main method to test the plugin.
     *
     * @param args ,arguments.
     */
    public static void main(final String[] args) {
        new ij.ImageJ();
        new Stack_Object_Combiner().run("");
    }
}
