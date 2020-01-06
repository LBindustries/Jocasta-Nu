package com.fermitech.jocasta.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class FileInputOptions extends AutoPanel implements ActionListener {

    private JCheckBox comprimi, cifra;
    private JRadioButton parti, dimensione;
    private ButtonGroup split;
    private JTextField password, nparti, ndimensione;
    private JLabel lpass, lpart, ldim;


    public FileInputOptions(String panel_name) {
        super(panel_name);
        GridLayout lm = new GridLayout(11,1);
        this.setLayout(lm);
        this.comprimi = new JCheckBox("Comprimi");
        this.cifra = new JCheckBox("Cifra");
        this.cifra.addActionListener(this);
        this.parti = new JRadioButton("Dividi in n° parti");
        this.parti.addActionListener(this);
        this.dimensione = new JRadioButton("Dividi in n° MB");
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

    public void printpass(){
        System.out.println(this.password.getText());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==cifra){
            password.setEnabled(cifra.isSelected());
        }
        else if (e.getSource()==parti){
            nparti.setEnabled(parti.isSelected());
            ndimensione.setEnabled(false);
        }
        else if (e.getSource()==dimensione){
            ndimensione.setEnabled(dimensione.isSelected());
            nparti.setEnabled(false);
        }
    }

    public String getPassword() {
        return password.getText();
    }

    public boolean getCrypt(){
        return cifra.isSelected();
    }

    public boolean getComp(){
        return cifra.isSelected();
    }

    public boolean getPart(){
        return parti.isSelected();
    }

    public boolean getDim(){
        return dimensione.isSelected();
    }

    public String get_value(){
        if(dimensione.isSelected()){
            return ndimensione.getText();
        }
        else{
            return nparti.getText();
        }
    }
}
