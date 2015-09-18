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

// Basics import of imageJ
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

    private static final List<AbstractSplit> SPLITS_UP_LEFT = new ArrayList();
    private static final List<AbstractSplit> SPLITS_UP_MIDDLE = new ArrayList();

    private static final List<Mesh> ALL_MESHES = new ArrayList();
    private static final List<Mesh> UP_LEFT_MESHES = new ArrayList();
    private static final List<Mesh> UP_MIDDLE_MESHES = new ArrayList();

    /**
     * The start of the plugin.
     *
     * @param arg , parameter for the plugin.
     */
    @Override
    public final void run(String arg) {
        Path workingDirectory = Paths.get(IJ.getDirectory("Give the file containing the"
                + " .obj file to combine"));
        dialogBoxSplit();

        IJ.showProgress(0.0);
        try {
            DirectoryStream<Path> listing = Files.newDirectoryStream(workingDirectory, "A_*.obj");
            for (Path name : listing) {
                Mesh mesh = new Mesh(SPLITS_UP_LEFT, name);
                UP_LEFT_MESHES.add(mesh);
                ALL_MESHES.add(mesh);
            }
            listing = Files.newDirectoryStream(workingDirectory, "B_*.obj");
            for (Path name : listing) {
                Mesh mesh = new Mesh(SPLITS_UP_MIDDLE, name);
                UP_MIDDLE_MESHES.add(mesh);
                ALL_MESHES.add(mesh);
            }
        } catch (IOException ex) {
            Logger.getLogger(Stack_Object_Combiner.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<Mesh> meshes = dialogBoxChooseMeshes(workingDirectory);
        Mesh mesh1 = meshes.get(0), mesh2 = meshes.get(1);
        mesh1.importMesh(); mesh2.importMesh();
        //mesh1.shift(); mesh2.shift();
        mesh1.exportMesh(); mesh2.exportMesh();
        mesh1.createBorders();
        mesh2.createBorders();
        System.out.println("Fini");

        IJ.showProgress(100.0);

    }

    private void dialogBoxSplit() {
        int splitLeft = 0, splitUp = 0, splitRight = 0, splitDown = 0;
        GenericDialog gd = new GenericDialog("Stack_Object_Combiner");
        gd.hideCancelButton();
        gd.addNumericField("Split Left", splitLeft, 0);
        gd.addNumericField("Split Up", splitUp, 0);
        gd.addNumericField("Split Right", splitRight, 0);
        gd.addNumericField("Split Down", splitDown, 0);
        gd.showDialog();
        splitLeft = (int) gd.getNextNumber();
        splitUp = (int) gd.getNextNumber();
        splitRight = (int) gd.getNextNumber();
        splitDown = (int) gd.getNextNumber();

        SPLITS_UP_LEFT.add(new SplitRight(splitLeft));
        SPLITS_UP_MIDDLE.add(new SplitLeft(splitLeft));
    }

    private List<Mesh> dialogBoxChooseMeshes(Path workingDirectory) {
        // TODO add verifications on files and which should be associate
        List<String> list = new ArrayList(); final String[] choices;
        String meshName1, meshName2; List<Mesh> result;
        try {
            final DirectoryStream<Path> listing = Files.newDirectoryStream(
                    workingDirectory, "*.obj");
            for (Path name : listing) {
                list.add(name.getFileName().toString());
            }
        } catch (IOException ex) {
            Logger.getLogger(Stack_Object_Combiner.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        choices = new String[list.size()];
        list.toArray(choices);

        GenericDialog gd = new GenericDialog("Choose_meshes");
        gd.hideCancelButton();
        gd.addChoice("Part 1", choices, choices[0]);
        gd.addChoice("Part 2", choices, choices[1]);
        gd.showDialog();
        meshName1 = gd.getNextChoice();
        meshName2 = gd.getNextChoice();
        result = new ArrayList();
        result.add(findMesh(meshName1));
        result.add(findMesh(meshName2));

        return result;
    }

    private Mesh findMesh(final String meshName) {
        Mesh result = null;
        for (Mesh mesh : ALL_MESHES) {
            if (mesh.getPath().toString().contains(meshName)) {
                result = mesh;
                break;
            }
        }
        return result;
    }

    /**
     * Iterate on all the meshes to call their shift method.
     */
    private void shiftMeshes() {
        for (final Mesh mesh : ALL_MESHES) {
            mesh.shift();
        }
    }

    /**
     * Main method to test the plugin.
     *
     * @param args ,arguments.
     */
    public static void main(String[] args) {
        new ij.ImageJ();
        new Stack_Object_Combiner().run("");
    }

}
