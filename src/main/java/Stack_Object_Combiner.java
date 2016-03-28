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

import gin.melec.CustomFrame;
import ij.plugin.PlugIn;

/**
 * @author Tom Boissonnet
 * <a href="mailto:tom.boissonnet@hotmail.fr">tom.boissonnet@hotmail.fr</a>
 */
public class Stack_Object_Combiner implements PlugIn {


    /**
     * The start of the plugin.
     *
     * @param arg , parameter for the plugin.
     */
    @Override
    public final void run(final String arg) {
        CustomFrame cW = new CustomFrame();
        cW.setVisible(true);
        

        //TODO Clean on exit
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

    /*private void clearMeshes() {
        A_MESHES.clear();
        B_MESHES.clear();
        C_MESHES.clear();
        D_MESHES.clear();
        ALL_MESHES.clear();
    }*/
}
