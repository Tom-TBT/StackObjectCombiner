

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ij.IJ;
import ij.plugin.PlugIn;

/**
 *
 * @author tom
 */
public class Open_obj_format implements PlugIn {

    @Override
    public final void run(final String string) {
        IJ.showMessage("Ma boite", string);
    }

    /**
     * Ma méthode main.
     *
     * @param args, arguments passés à main
     */
    public static void main(String[] args) {
        new ij.ImageJ();
        new Open_obj_format().run("Et voila un plugin");
    }

}
