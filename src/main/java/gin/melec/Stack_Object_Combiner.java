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

    private static final List<AbstractSplit> UP_LEFT_SPLITS = new ArrayList();
    private static final List<AbstractSplit> UP_RIGHT_SPLITS = new ArrayList();
    private static final List<AbstractSplit> DOWN_LEFT_SPLITS = new ArrayList();
    private static final List<AbstractSplit> DOWN_RIGHT_SPLITS = new ArrayList();

    private static final List<Mesh> UP_LEFT_MESHES = new ArrayList();
    private static final List<Mesh> UP_RIGHT_MESHES = new ArrayList();
    private static final List<Mesh> DOWN_LEFT_MESHES = new ArrayList();
    private static final List<Mesh> DOWN_RIGHT_MESHES = new ArrayList();
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
        getSplits();
        getMeshes(workingDirectory);

        boolean notCanceled = true;
        while (notCanceled) {
            notCanceled = proposeAction();
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
        final GenericDialog gDial = new GenericDialog("Stack_Object_Combiner");
        gDial.addMessage("Choose the action to perform");
        gDial.enableYesNoCancel("Replace the meshes", "Merge two meshes");
        gDial.showDialog();
        if (gDial.wasCanceled()) {
            result = false;
        } else if (gDial.wasOKed()) {
            MeshMover.putBackMeshes(ALL_MESHES);
            result = true;
        } else {
            MeshMerger.work(ALL_MESHES);
            result = true;
        }
        return result;
    }

    /**
     * Open a dialog box that ask for the position of the splits.
     */
    private void getSplits() {
        int verticalSplit = 0, horizontalSplit = 0;
        final GenericDialog gDial = new GenericDialog("Indicate the positions "
                + "of the splits");
        gDial.hideCancelButton();
        gDial.addNumericField("Vertical split", verticalSplit, 0);
        gDial.addNumericField("Horizontal split", horizontalSplit, 0);
        gDial.showDialog();
        verticalSplit = (int) gDial.getNextNumber();
        horizontalSplit = (int) gDial.getNextNumber();
        // TODO think to a control
        if (verticalSplit > 0) {
            UP_LEFT_SPLITS.add(new SplitRight(verticalSplit));
            UP_RIGHT_SPLITS.add(new SplitLeft(verticalSplit));
            DOWN_LEFT_SPLITS.add(new SplitRight(verticalSplit));
            DOWN_RIGHT_SPLITS.add(new SplitLeft(verticalSplit));
        }
        if (horizontalSplit > 0) {
            UP_LEFT_SPLITS.add(new SplitDown(horizontalSplit));
            UP_RIGHT_SPLITS.add(new SplitDown(horizontalSplit));
            DOWN_LEFT_SPLITS.add(new SplitUp(horizontalSplit));
            DOWN_RIGHT_SPLITS.add(new SplitUp(horizontalSplit));
        }
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
                final Mesh mesh = new Mesh(UP_LEFT_SPLITS, name);
                UP_LEFT_MESHES.add(mesh);
            }
            ALL_MESHES.add(UP_LEFT_MESHES);

            listing = Files.newDirectoryStream(workingDirectory, "B_*.obj");
            for (Path name : listing) {
                final Mesh mesh = new Mesh(UP_RIGHT_SPLITS, name);
                UP_RIGHT_MESHES.add(mesh);
            }
            ALL_MESHES.add(UP_RIGHT_MESHES);

            listing = Files.newDirectoryStream(workingDirectory, "C_*.obj");
            for (Path name : listing) {
                final Mesh mesh = new Mesh(DOWN_LEFT_SPLITS, name);
                DOWN_LEFT_MESHES.add(mesh);
            }
            ALL_MESHES.add(DOWN_LEFT_MESHES);

            listing = Files.newDirectoryStream(workingDirectory, "D_*.obj");
            for (Path name : listing) {
                final Mesh mesh = new Mesh(DOWN_RIGHT_SPLITS, name);
                DOWN_RIGHT_MESHES.add(mesh);
            }
            ALL_MESHES.add(DOWN_RIGHT_MESHES);
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
