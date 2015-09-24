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
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Stack_Object_Combiner implements PlugIn {

    private static final List<AbstractSplit> A_SPLITS = new ArrayList();
    private static final List<AbstractSplit> B_SPLITS = new ArrayList();
    private static final List<AbstractSplit> C_SPLITS = new ArrayList();
    private static final List<AbstractSplit> D_SPLITS = new ArrayList();

    private static final List<Mesh> A_MESHES = new ArrayList();
    private static final List<Mesh> B_MESHES = new ArrayList();
    private static final List<Mesh> C_MESHES = new ArrayList();
    private static final List<Mesh> D_MESHES = new ArrayList();
    private static final List<List> ALL_MESHES = new ArrayList();

    /**
     * The start of the plugin.
     *
     * @param arg , parameter for the plugin.
     */
    @Override
    public final void run(final String arg) {
        final Path workingDirectory = Paths.get(IJ.getDirectory("Give the file "
                + "containing the .obj files"));
        if (getSplits(workingDirectory)) {
            getMeshes(workingDirectory);
            boolean notCanceled = true;
            while (notCanceled) {
                notCanceled = proposeAction();
            }
        }
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
            MeshMerger.work(ALL_MESHES);
            result = true;
        }
        return result;
    }

    /**
     * Open a dialog box that ask for the position of the splits.
     *
     * @param workingDirectory , the current directory.
     * @return true if the panel hasn't been canceled.
     */
    private boolean getSplits(final Path workingDirectory) {
        int verticalSplit = 0, horizontalSplit = 0;
        boolean result = true;
        boolean verticalExist = false, horizontalExist = false;
        final GenericDialog gDial = new GenericDialog("Indicate the positions "
                + "of the splits.");

        // TODO think to a control
        DirectoryStream<Path> listing;
        try {
            listing = Files.newDirectoryStream(workingDirectory, "D_*.obj");
            if (listing.iterator().hasNext()) { // If files in D exists
                gDial.addNumericField("Vertical split", verticalSplit, 0);
                gDial.addNumericField("Horizontal split", horizontalSplit, 0);
                verticalExist = true;
                horizontalExist = true;
            } else {
                listing = Files.newDirectoryStream(workingDirectory, "B_*.obj");
                if (listing.iterator().hasNext()) { // If files in B exists
                    gDial.addNumericField("Vertical split", verticalSplit, 0);
                    verticalExist = true;
                }
                listing = Files.newDirectoryStream(workingDirectory, "C_*.obj");
                if (listing.iterator().hasNext()) { // If files in C exists
                    gDial.addNumericField("Horizontal split", horizontalSplit
                            , 0);
                    horizontalExist = true;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Stack_Object_Combiner.class.getName()).
                    log(Level.SEVERE, null, ex);
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
                A_SPLITS.add(new SplitRight(verticalSplit));
                B_SPLITS.add(new SplitLeft(verticalSplit));
                C_SPLITS.add(new SplitRight(verticalSplit));
                D_SPLITS.add(new SplitLeft(verticalSplit));
            }
            if (horizontalExist) {
                horizontalSplit = (int) gDial.getNextNumber();
                A_SPLITS.add(new SplitDown(horizontalSplit));
                B_SPLITS.add(new SplitDown(horizontalSplit));
                C_SPLITS.add(new SplitUp(horizontalSplit));
                D_SPLITS.add(new SplitUp(horizontalSplit));
            }
        }
        return result;
    }

    /**
     * Add to the meshes lists the meshes contained in the given directory.
     *
     * @param workingDirectory , the directory containing the .obj files.
     */
    private void getMeshes(final Path workingDirectory) {
        DirectoryStream<Path> listing;
        try {
            listing = Files.newDirectoryStream(workingDirectory, "A_*.obj");
            for (Path name : listing) {
                final Mesh mesh = new Mesh(A_SPLITS, name);
                A_MESHES.add(mesh);
            }
            ALL_MESHES.add(A_MESHES);

            listing = Files.newDirectoryStream(workingDirectory, "B_*.obj");
            for (Path name : listing) {
                final Mesh mesh = new Mesh(B_SPLITS, name);
                B_MESHES.add(mesh);
            }
            ALL_MESHES.add(B_MESHES);

            listing = Files.newDirectoryStream(workingDirectory, "C_*.obj");
            for (Path name : listing) {
                final Mesh mesh = new Mesh(C_SPLITS, name);
                C_MESHES.add(mesh);
            }
            ALL_MESHES.add(C_MESHES);

            listing = Files.newDirectoryStream(workingDirectory, "D_*.obj");
            for (Path name : listing) {
                final Mesh mesh = new Mesh(D_SPLITS, name);
                D_MESHES.add(mesh);
            }
            ALL_MESHES.add(D_MESHES);
        } catch (IOException ex) {
            Logger.getLogger(Stack_Object_Combiner.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
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
