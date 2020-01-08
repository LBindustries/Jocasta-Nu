package com.fermitech.jocasta.gui;

import com.fermitech.jocasta.core.JobDescriptor;
import com.fermitech.jocasta.jobs.Job;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Queue;

public class MainPanel extends AutoPanel implements ActionListener {
    JMenu menu;
    JMenuBar mb;
    JMenuItem crea, apri, about;

    public MainPanel(JFrame frame) {
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

    private String chooserGetFile(AutoPanel options, String title, boolean only_folders) {
        CustomChooser cc1 = new CustomChooser(options, title, only_folders);
        int returnvalue = cc1.showOpenDialog(null);
        if (returnvalue != JFileChooser.APPROVE_OPTION) {
            return "error"; //Todo: I dont think this really works
        }
        return cc1.getSelectedFile().getAbsolutePath();
    }

    private void createArchive() throws FileNotFoundException {
        FileInputOptions opzioni = new FileInputOptions("Opzioni");
        JobDescriptor j = new JobDescriptor(opzioni, chooserGetFile(opzioni, "Seleziona un file", false), chooserGetFile(null, "Seleziona la cartella di destinazione", true));
        if (j.getFlag()) {
            this.summonErrorPopup("La stringa inserita non è un numero.\nL'operazione è stata annullata.");
            return;
        }
        j.BuildJobs();
        System.out.println(j);
        try {
            j.RunJobs();
        } catch (IOException e) {
            this.summonErrorPopup("Errore durante l'esecuzione del Job indicato.");
        }
    }

    private void openArchive() throws FileNotFoundException {
        FileExtractionOptions opzioni = new FileExtractionOptions("Opzioni");
        String src = chooserGetFile(opzioni, "Seleziona l'archivio", false);
        String dst = chooserGetFile(null,"Seleziona la cartella di destinazione",true);
        JobDescriptor j = new JobDescriptor(src, dst, opzioni.getPassword());
        j.BuildJobs();
        System.out.println(j);
        try {
            j.RunJobs();
        } catch (IOException e) {
            this.summonErrorPopup("Errore durante l'esecuzione del Job indicato.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == crea) {
            try {
                createArchive();
            } catch (FileNotFoundException ex) {
                this.summonErrorPopup("File non trovato.\nChe gli archivi siano incompleti?");
            }
        }
        else if(e.getSource() == apri){
            try {
                openArchive();
            } catch (FileNotFoundException ex) {
                this.summonErrorPopup("File non trovato.\nChe gli archivi siano incompleti?");
            }
        }
    }
}
