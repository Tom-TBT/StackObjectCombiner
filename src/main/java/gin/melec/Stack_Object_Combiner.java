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
import ij.plugin.PlugIn;

/**
 *
 * @author tom
 */
public class Stack_Object_Combiner implements PlugIn {

    /**
     * Run method called when an .obj file is opened.
     * @param arg
     */
    @Override
    public final void run(String arg) {
        IJ.showMessage("Ma boite", arg);


    }

    /**
     * Ma méthode main.
     *
     * @param args, arguments passés à main
     */
    public static void main(String[] args) {
        new ij.ImageJ();
        new Stack_Object_Combiner().run("Plugin appelé par main");
    }

}
