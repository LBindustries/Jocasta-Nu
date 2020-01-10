package com.fermitech.jocasta;
import com.fermitech.jocasta.gui.*;

import javax.swing.*;
import java.awt.*;
/**
 * @author Lorenzo Balugani
 * @version 1.0
 */
public class Main {
    /**
     * This is the runner. It creates the frame, and loads up the panel.
     */
    public static void main(String[] args) {
        JFrame f = new JFrame("Jocasta-Nu archive manager");
        Container container = f.getContentPane();
        AutoPanel pannello = new MainPanel(f);
        container.add(pannello);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1050, 500);
        f.setResizable(false);
    }
}
