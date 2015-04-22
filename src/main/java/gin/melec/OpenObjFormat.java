package gin.melec;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import ij.IJ;
import ij.ImagePlus;
import ij.io.OpenDialog;
import ij.plugin.PlugIn;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class OpenObjFormat extends ImagePlus implements PlugIn {

    protected static final int NB_DIMENSIONS = 3;

    @Override
    public final void run(String arg) {
        IJ.showMessage("Ma boite", arg);
        String path = getPath(arg);
        if (path != null && parse(path)) {
            IJ.showMessage("Ma boite", "L'image existe !!");
        } else {
            IJ.showMessage("Ma boite", "L'image n'existe pas !!");
        }
    }

    /**
     * Méthode main.
     *
     * @param args arguments passés à l'appel
     */
    public static void main(String[] args) {
        new ij.ImageJ();
        new OpenObjFormat().run("/home/tom/ObjetsIlastik/Cellule.obj");
    }

    /**
     * Method which search if the file exists and if not, ask for an other file.
     * If the file exist locally or if it is an url, this method will just
     * return the path given. Else, this method will ask to the user to give a
     * file from an OpenDialog.
     *
     * @param arg The path of the file to check.
     * @return The path of the file to read.
     */
    private String getPath(final String arg) {
        if (arg != null
                && arg.indexOf("http://") == 0 || new File(arg).exists()) {
            return arg;
        }
        OpenDialog dialog;
        dialog = new OpenDialog("Choose a .obj file", null);
        String dir = dialog.getDirectory();
        if (null == dir) {
            return null; // dialog was canceled
        }
        dir = dir.replace('\\', '/'); // Windows safe
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        return dir + dialog.getFileName();
    }

    /**
     * This method read file in the .obj format to convert it in an ImagePlus.
     * The imagePlus is a stack.
     *
     * @param path Path of the file to read.
     * @return Return true if the file has been read without errors, else return
     * false.
     */
    private boolean parse(String path) {
        final File objFile = new File(path);
        //TODO Mettre les fileInfo
        try {
            final InputStream ips = new FileInputStream(objFile);
            final InputStreamReader ipsr = new InputStreamReader(ips);
            final BufferedReader br = new BufferedReader(ipsr);
            String line;
            ArrayList vertexList;
            vertexList = new ArrayList();
            try {
                line = br.readLine();
                while (line != null) {
                    addVertex(line, vertexList);
                    line = br.readLine();
                }
            } catch (EndOfVertexException ex) {
                // TODO Enchainer avec les F
            }
            br.close();
            IJ.showMessage("Ma boite", "" + vertexList.size());
        } catch (Exception e) {
            IJ.handleException(e);
        }
        return true;
    }

    /**
     * Extract values from the line, to get the coordinates of a vertex.
     *
     * @param line
     * @param vertexList
     * @throws EndOfVertexException
     */
    private void addVertex(final String line, final List vertexList)
            throws EndOfVertexException {
        String[] lineValues;
        lineValues = line.split(" ");
        // Vérifier que l'on a bien 4 valeurs ? sinon problème dans le format
        if (lineValues[0].equalsIgnoreCase("V")) {
            final double[] coordinates = new double[NB_DIMENSIONS];
            coordinates[0] = Double.parseDouble(lineValues[1]);
            coordinates[1] = Double.parseDouble(lineValues[2]);
            coordinates[2] = Double.parseDouble(lineValues[3]);
            vertexList.add(coordinates);
        } else {
            throw new EndOfVertexException();
        }
    }

}
