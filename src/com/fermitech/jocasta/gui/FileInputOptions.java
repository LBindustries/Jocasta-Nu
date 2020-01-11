package com.fermitech.jocasta.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * This is the FileInputOptions class, used to provide the user an iterface from which he can select the jobs needed on
 * a certain file.
 */
public class FileInputOptions extends AutoPanel implements ActionListener {

    private JCheckBox comprimi, cifra;
    private JRadioButton parti, dimensione;
    private ButtonGroup split;
    private JTextField password, nparti, ndimensione;
    private JLabel lpass, lpart, ldim;

    /**
     * This is the FileInputOptions constructor.
     * It initializes the ui.
     *
     * @param panel_name the name of the panel.
     */
    public FileInputOptions(String panel_name) {
        super(panel_name);
        GridLayout lm = new GridLayout(11, 1);
        this.setLayout(lm);
        this.comprimi = new JCheckBox("Comprimi");
        this.cifra = new JCheckBox("Cifra");
        this.cifra.addActionListener(this);
        this.parti = new JRadioButton("Dividi in n° parti");
        this.parti.addActionListener(this);
        this.dimensione = new JRadioButton("Dividi in n° kb");
        this.dimensione.addActionListener(this);
        this.split = new ButtonGroup();
        this.parti.setSelected(true);
        split.add(parti);
        split.add(dimensione);
        this.password = new JTextField(10);
        this.password.setEnabled(false);
        this.lpass = new JLabel("Password");
        this.nparti = new JTextField(5);
        this.nparti.setEnabled(true);
        this.lpart = new JLabel("N° parti");
        this.ndimensione = new JTextField(5);
        this.ndimensione.setEnabled(false);
        this.ldim = new JLabel("Dimensione in Kb");
        this.add(comprimi);
        this.add(cifra);
        this.add(parti);
        this.add(dimensione);
        this.add(lpass);
        this.add(password);
        this.add(lpart);
        this.add(nparti);
        this.add(ldim);
        this.add(ndimensione);
    }

    /**
     * This is the FileInputOptions actionPerformed method.
     * It reacts to changes to the ui, and enables certain types of jobs accordingly to the user input.
     *
     * @param e the event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cifra) {
            password.setEnabled(cifra.isSelected());
        } else if (e.getSource() == parti) {
            nparti.setEnabled(parti.isSelected());
            ndimensione.setEnabled(false);
        } else if (e.getSource() == dimensione) {
            ndimensione.setEnabled(dimensione.isSelected());
            nparti.setEnabled(false);
        }
    }

    /**
     * This is the FileInputOptions getPassword method.
     * It returns the contents of password.
     *
     * @return the password.
     */
    public String getPassword() {
        return password.getText();
    }

    /**
     * This is the FileInputOptions getCrypt method.
     * It returns the boolean inside cifra.
     *
     * @return the boolean cifra.
     */
    public boolean getCrypt() {
        return cifra.isSelected();
    }

    /**
     * This is the FileInputOptions getComp method.
     * It returns the boolean inside comprimi.
     *
     * @return the boolean comprimi.
     */
    public boolean getComp() {
        return comprimi.isSelected();
    }

    /**
     * This is the FileInputOptions getParti method.
     * It returns the boolean inside parti.
     *
     * @return the boolean parti.
     */
    public boolean getPart() {
        return parti.isSelected();
    }

    /**
     * This is the FileInputOptions getDim method.
     * It returns the boolean inside dimensione.
     *
     * @return the boolean dimensione.
     */
    public boolean getDim() {
        return dimensione.isSelected();
    }

    /**
     * This is the FileInputOptions getValue method.
     * It returns, accordingly with the user's choice, the desired size or desired number of files.
     *
     * @return the division value.
     */
    public String get_value() {
        if (dimensione.isSelected()) {
            return ndimensione.getText();
        } else {
            return nparti.getText();
        }
    }

    /**
     * This is the FileInputOptions setCifra method.
     * It sets the condition of cifra.
     *
     * @param set the desired value.
     */
    public void setCifra(boolean set) {
        this.cifra.setSelected(set);
    }
    /**
     * This is the FileInputOptions setPassword method.
     * It sets the condition of password.
     *
     * @param set the desired value.
     */
    public void setPassword(String set) {
        this.password.setText(set);
    }
    /**
     * This is the FileInputOptions setComprimi method.
     * It sets the condition of comprimi.
     *
     * @param set the desired value.
     */
    public void setComprimi(boolean set) {
        this.comprimi.setSelected(set);
    }
    /**
     * This is the FileInputOptions setParti method.
     * It sets the condition of parti.
     *
     * @param set the desired value.
     */
    public void setParti(boolean set) {
        this.parti.setSelected(set);
    }
    /**
     * This is the FileInputOptions setDimensione method.
     * It sets the condition of dimensione.
     *
     * @param set the desired value.
     */
    public void setDimensione(boolean set) {
        this.dimensione.setSelected(set);
    }
    /**
     * This is the FileInputOptions setValue method.
     * It sets the condition of the selected division method JTextField.
     *
     * @param set the desired value.
     */
    public void set_value(String set) {
        if (dimensione.isSelected()) {
            ndimensione.setText(set);
        } else {
            nparti.setText(set);
        }
    }
}
