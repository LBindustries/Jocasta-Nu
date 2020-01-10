package com.fermitech.jocasta.gui;

import javax.swing.*;
import java.awt.*;

/**
 * This is the CustomChooser class, an extension of JFileChooser.
 */

public class CustomChooser extends JFileChooser{
    /**
     * This is the CustomChooser class constructor.
     *
     * @param accessory the options panel.
     * @param title the title of the window.
     * @param only_folders if true, only folders will be displayed.
     * @param multi if true, multiple selection of files will be enabled.
     */
    public CustomChooser(JPanel accessory,String title, boolean only_folders, boolean multi){
        super();
        this.setDialogTitle(title);
        if(only_folders){
            this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        if(multi){
            this.setMultiSelectionEnabled(true);
        }
        this.setAccessory(accessory);
    }
}
