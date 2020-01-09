package com.fermitech.jocasta.gui;

import com.fermitech.jocasta.core.JobDescriptor;
import com.fermitech.jocasta.jobs.Job;
import com.fermitech.jocasta.core.JobThread;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Vector;

public class MainPanel extends AutoPanel implements ActionListener {
    JMenu menu;
    JMenuBar mb;
    JMenuItem crea, apri, about;
    JTable tabella;
    ArrayList<JobDescriptor> elenco = new ArrayList<JobDescriptor>();
    JButton start;

    public MainPanel(JFrame frame) {
        super("Pannello Principale");
        menuBarSetup(frame);
        tableSetup();
        start = new JButton("Esegui tutti i job");
        start.addActionListener(this);
        this.add(start);
    }

    private void menuBarSetup(JFrame frame){
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

    private void tableSetup(){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Bersaglio");
        model.addColumn("Dest.");
        model.addColumn("Job richiesti");
        model.addColumn("Status");
        tabella = new JTable(model){
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(tabella, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1000, 400));
        this.add(scrollPane);
    }

    private void addToTable(JobDescriptor j){
        DefaultTableModel model = (DefaultTableModel) tabella.getModel();
        model.addRow(new Object[]{j.getSrc_path(), j.getDst_path(), j.getDescrizione(), ""});
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
        if(j.getSrc_path().equals("error")||j.getDst_path().equals("error")){
            return;
        }
        j.BuildJobs();
        System.out.println(j);
        elenco.add(j);
        addToTable(j);
    }

    private void openArchive() throws FileNotFoundException {
        FileExtractionOptions opzioni = new FileExtractionOptions("Opzioni");
        String src = chooserGetFile(opzioni, "Seleziona l'archivio", false);
        String dst = chooserGetFile(null,"Seleziona la cartella di destinazione",true);
        JobDescriptor j = new JobDescriptor(src, dst, opzioni.getPassword());
        if(j.getSrc_path().equals("error")||j.getDst_path().equals("error")){
            return;
        }
        j.BuildJobs();
        System.out.println(j);
        elenco.add(j);
        addToTable(j);
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
        else if(e.getSource() == start && elenco.size()>0){
            runAllJobs();
        }
    }

    private void runAllJobs(){
        Vector<JobThread> threads = new Vector<JobThread>();
        for(JobDescriptor j:elenco){
            threads.add(new JobThread(j));
        }
        for(JobThread t:threads){
            t.run();
        }
    }
}
