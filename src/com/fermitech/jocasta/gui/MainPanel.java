package com.fermitech.jocasta.gui;

import com.fermitech.jocasta.core.JobDescriptor;
import com.fermitech.jocasta.jobs.Job;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

public class MainPanel extends AutoPanel implements ActionListener {
    JMenu menu;
    JMenuBar mb;
    JMenuItem crea, apri, about;
    public MainPanel(JFrame frame){
        super("Pannello Principale");
        crea = new JMenuItem("Crea archivio");
        crea.addActionListener(this);
        menu = new JMenu("Operazioni");
        menu.addActionListener(this);
        apri = new JMenuItem("Apri archivio");
        apri.addActionListener(this);
        about = new JMenuItem("Informazioni");
        about.addActionListener(this);
        menu.add(crea);
        menu.add(apri);
        menu.add(about);
        mb = new JMenuBar();
        mb.add(menu);
        frame.setJMenuBar(mb);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == crea){
            FileInputOptions opzioni = new FileInputOptions("Opzioni");
            CustomChooser cc1 = new CustomChooser(opzioni, "Seleziona un file su cui lavorare" ,false);
            int returnvalue = cc1.showOpenDialog(null);
            if(returnvalue != JFileChooser.APPROVE_OPTION){
                return;
            }
            CustomChooser cc2 = new CustomChooser(null,"Seleziona una cartella di destinazione" ,true);
            returnvalue = cc2.showOpenDialog(null);
            if(returnvalue != JFileChooser.APPROVE_OPTION){
                return;
            }
            String inputPath = cc1.getSelectedFile().getAbsolutePath();
            String outputPath = cc2.getSelectedFile().getAbsolutePath();
            JobDescriptor j = new JobDescriptor(opzioni, inputPath, outputPath);
            if(!j.getFlag()){
                System.out.println(j);
            }
            else{
                JOptionPane.showMessageDialog(null, "La stringa inserita non è un numero.\nL'operazione è stata annullata.", "Jocasta-Nu: Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
