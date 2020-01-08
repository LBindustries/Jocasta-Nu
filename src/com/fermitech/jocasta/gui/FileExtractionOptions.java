package com.fermitech.jocasta.gui;

import javax.swing.*;
import java.awt.*;

public class FileExtractionOptions extends AutoPanel {
    private JTextField password;
    private JLabel p_label;
    public FileExtractionOptions(String panel_name) {
        super(panel_name);
        GridLayout lm = new GridLayout(11,1);
        this.setLayout(lm);
        this.p_label = new JLabel("Password");
        this.add(p_label);
        this.password = new JTextField(10);
        this.add(password);
    }

    public String getPassword() {
        return password.getText();
    }
}
