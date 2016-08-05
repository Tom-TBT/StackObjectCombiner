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

import ij.gui.GenericDialog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class ObjWriter {
    static boolean AUTOSAVE = false;

    /**
     * A private constructor because this is an utilitary class and it should
     * never be instanciate.
     */
    private ObjWriter() {
    }

    public static ObjWriter getInstance() {
        return ObjWriterHolder.INSTANCE;
    }

    private static class ObjWriterHolder {
        private static final ObjWriter INSTANCE = new ObjWriter();
    }

    /**
     * Replace the given mesh by the new mesh contained in the array.
     *
     * @param file , the file of the mesh.
     * @param shiftX
     * @param shiftY
     * @param mesh , the mesh to write.
     * @throws IOException , thrown by the writer.
     */
    public static void writeMesh(final Mesh mesh, final double shiftX,
            final double shiftY, final double shiftZ)
            throws IOException {
        CustomFrame.appendToLog("Saving the mesh " + mesh.getFile().getName());
        final FileWriter fiW = new FileWriter(mesh.getFile().toString());
        final BufferedWriter bfW = new BufferedWriter(fiW);
        final PrintWriter prW = new PrintWriter(bfW);
        try {
            if (mesh.isMoved()) {
                prW.write("#movedBySOC X:"+shiftX+" Y:"+shiftY+" Z:"+shiftZ+"\n");
            }
            for (Vertex vertex : mesh.getVertices()) {
                prW.write(vertex.toString() + "\n");

            }
            for (Face face : mesh.getFaces()) {
                prW.write(face.toString() + "\n");
            }
        } finally {
            prW.close();
        }
    }

    static void writeResult(final List<Vertex> vertices, final List<Face> faces,
            final String newName, final File parentDirectory) throws IOException {
        File newMesh = new File(parentDirectory, newName);
        if (newMesh.exists()) {
            boolean replaceYN = false;
            if (!AUTOSAVE) {
                final GenericDialog gDial = new GenericDialog("");
                gDial.addMessage("The file already exist, replace it?");
                gDial.enableYesNoCancel("Yes", "No");
                gDial.hideCancelButton();
                gDial.showDialog();
                replaceYN = gDial.wasOKed();
            }
            if (!replaceYN) {
                int i = 0;
                while (newMesh.exists()) {
                    i++;
                    newMesh = new File(parentDirectory,
                            newName.substring(0, newName.length()-4)
                                    + "-" + i + ".obj");
                }
            }
        }
        CustomFrame.appendToLog("Saving the new object as "+ newMesh.getName());
        final FileWriter fiW = new FileWriter(newMesh.toString());
        final BufferedWriter bfW = new BufferedWriter(fiW);
        final PrintWriter prW = new PrintWriter(bfW);
        try {
            prW.write("#movedBySOC Merged\n");// A mesh merged cannot be moved anymore
            for (Vertex vertex : vertices) {
                prW.write(vertex.toString() + "\n");
            }
            for (Face face : faces) {
                prW.write(face.toString() + "\n");
            }
        } finally {
            prW.close();
        }
        CustomFrame.appendToLog(newMesh.getName()+" successfully saved");
    }

    protected static void exportFusion(Mesh mesh1, Mesh mesh2,
            List<Face> newFaces) throws IOException {
        String newName;
        if (!AUTOSAVE) {
            final GenericDialog gDial = new GenericDialog("Enter the name of the "
                + "new mesh");
            gDial.addStringField("Name", mesh1.getFile().getName(), 30);
            gDial.hideCancelButton();
            gDial.showDialog();
            newName = gDial.getNextString();
        } else {
            String mesh1Name = mesh1.getFile().getName();
            newName = mesh1Name.substring(0, mesh1Name.length()-4) + "_"
                    + mesh2.getFile().getName();
        }
        List<Vertex> verticesToWrite = new ArrayList();
        List<Face> facesToWrite = new ArrayList();

        for (Vertex vertex : mesh1.getVertices()) {
            verticesToWrite.add(vertex);
        }
        for (Vertex vertex : mesh2.getVertices()) {
            vertex.incrementId(mesh1.getVertices().size());
            verticesToWrite.add(vertex);
        }
        for (Face face : mesh1.getFaces()) {
            facesToWrite.add(face);
        }
        for (Face face : mesh2.getFaces()) {
            facesToWrite.add(face);
        }
        for (Face face : newFaces) {
            facesToWrite.add(face);
        }
        ObjWriter.writeResult(verticesToWrite, facesToWrite, newName,
                new File(mesh1.getFile().getParent()));

    }
}
