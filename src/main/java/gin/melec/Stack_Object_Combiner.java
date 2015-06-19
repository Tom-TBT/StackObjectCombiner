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
import ij.plugin.PlugIn;



/**
 *
 * @author tom
 */
public class Stack_Object_Combiner implements PlugIn {

    /**
     * The start of the plugin.
     * @param arg , parameter for the plugin.
     */
    @Override
    public final void run(String arg) {
        Scene scene = new Scene2Part(IJ.getDirectory("Give the file containing the "
                + ".obj file to combine"),291);
        IJ.showProgress(0.0);
        scene.shiftMeshes();
        IJ.showProgress(50.0);
        scene.createLimit();
        IJ.showProgress(100.0);
        IJ.showMessage("Fin !");

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
