package com.fermitech.jocasta.gui;

import javax.swing.*;
import java.awt.*;

/**
 * This is the FileExtractionOptions class, used to ask a user for the password of an encrypted archive.
 */
public class FileExtractionOptions extends AutoPanel {
    private JTextField password;
    private JLabel p_label;

    /**
     * This is the FileExtractionOptions class constructor.
     * It initializes the ui.
     *
     * @param panel_name the name of the panel.
     */
    public FileExtractionOptions(String panel_name) {
        super(panel_name);
        GridLayout lm = new GridLayout(11, 1);
        this.setLayout(lm);
        this.p_label = new JLabel("Password");
        this.add(p_label);
        this.password = new JTextField(10);
        this.add(password);
    }

    /**
     * This is the getPassword method.
     * It returns the text of password.
     *
     * @return the text inside the password JTextField.
     */
    public String getPassword() {
        return password.getText();
    }
}
