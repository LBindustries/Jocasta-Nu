package com.fermitech.jocasta.gui;

import javax.swing.*;
import java.util.Vector;

/**
 * This is the AutoPanel class, an extension of JPanel.
 */
public class AutoPanel extends JPanel {

    String panel_name;

    /**
     * This is the AutoPanel class constructor.
     *
     * @param panel_name the name of the panel.
     */
    public AutoPanel(String panel_name) {
        super();
        this.panel_name = panel_name;
    }

    /**
     * This is the AutoPanel setPanel_name method.
     * It sets the panel_name to a certain value.
     *
     * @param panel_name the new name of the panel.
     */
    public void setPanel_name(String panel_name) {
        this.panel_name = panel_name;
    }

    /**
     * This is the AutoPanel getPanel_name method.
     * It returns the panel_name.
     *
     * @return panel_name the new name of the panel.
     */
    public String getPanel_name() {
        return panel_name;
    }

    /**
     * This is the AutoPanel summonErrorPopup method.
     * It sets the summons an error dialog.
     *
     * @param message the message that gets displayed.
     */
    public void summonErrorPopup(String message) {
        JOptionPane.showMessageDialog(null, message, "Jocasta-Nu: Errore", JOptionPane.ERROR_MESSAGE);
    }
}
