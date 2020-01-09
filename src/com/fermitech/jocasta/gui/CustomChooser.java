package com.fermitech.jocasta.gui;

import javax.swing.*;
import java.awt.*;


public class CustomChooser extends JFileChooser{
    public CustomChooser(JPanel accessory,String title, boolean only_folders){
        super();
        this.setDialogTitle(title);
        if(only_folders){
            this.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
        this.setAccessory(accessory);
    }
}
