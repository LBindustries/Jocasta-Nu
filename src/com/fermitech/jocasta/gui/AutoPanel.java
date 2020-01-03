package com.fermitech.jocasta.gui;

import javax.swing.*;
import java.util.Vector;

public class AutoPanel extends JPanel {

    String panel_name;

    public AutoPanel(String panel_name) {
        super();
        this.panel_name = panel_name;
    }

    public void add_vector(Vector<JComponent> vettore) {
        for (JComponent oggetto : vettore) {
            this.add(oggetto);
        }
    }

    public void setPanel_name(String panel_name) {
        this.panel_name = panel_name;
    }

    public String getPanel_name() {
        return panel_name;
    }
}
