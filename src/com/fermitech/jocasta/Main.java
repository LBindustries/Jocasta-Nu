package com.fermitech.jocasta;
import com.fermitech.jocasta.gui.*;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        JFrame f = new JFrame("Jocasta-Nu archive manager");
        Container container = f.getContentPane();
        AutoPanel pannello = new MainPanel(f);
        container.add(pannello);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(1050, 550);
        f.setResizable(false);
    }
}
