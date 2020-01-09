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
    private JMenu menu;
    private JMenuBar mb;
    private JMenuItem crea, apri, about;
    private JTable tabella;
    private ArrayList<JobDescriptor> elenco = new ArrayList<JobDescriptor>();
    private JButton start, delete, mod;
    private JLabel id_label;
    private JTextField id;
    private Vector<JobThread> threads;

    public MainPanel(JFrame frame) {
        super("Pannello Principale");
        menuBarSetup(frame);
        tableSetup();
        id_label = new JLabel("Id del Job:");
        id = new JTextField(4);
        delete = new JButton("Elimina il Job");
        delete.addActionListener(this);
        mod = new JButton("Modifica il Job");
        mod.addActionListener(this);
        start = new JButton("Esegui tutti i Job");
        start.addActionListener(this);
        this.add(id_label);
        this.add(id);
        this.add(delete);
        this.add(mod);
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
        model.addColumn("ID");
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
        model.addRow(new Object[]{j.getId(),j.getSrc_path(), j.getDst_path(), j.getDescrizione(), ""});
    }

    private String chooserGetFile(AutoPanel options, String title, boolean only_folders) {
        CustomChooser cc1 = new CustomChooser(options, title, only_folders);
        int returnvalue = cc1.showOpenDialog(null);
        if (returnvalue != JFileChooser.APPROVE_OPTION) {
            return "error";
        }
        return cc1.getSelectedFile().getAbsolutePath();
    }

    private void createArchive() throws FileNotFoundException {
        FileInputOptions opzioni = new FileInputOptions("Opzioni");
        JobDescriptor j = new JobDescriptor(opzioni, chooserGetFile(opzioni, "Seleziona un file", false), chooserGetFile(null, "Seleziona la cartella di destinazione", true), elenco.size());
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
        JobDescriptor j = new JobDescriptor(src, dst, opzioni.getPassword(), elenco.size());
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
        else if(e.getSource() == delete && elenco.size()>0){
            try{
                deleteJob(Integer.parseInt(id.getText()));
            } catch (Exception ex) {
                this.summonErrorPopup("Il numero inserito non è valido.");
            }
        }
        else if(e.getSource() == mod && elenco.size()>0){
            try{
                modJob(Integer.parseInt(id.getText()));
            } catch (Exception ex) {
                this.summonErrorPopup("Il numero inserito non è valido.");
            }
        }
    }

    private void deleteJob(int index){
        JobDescriptor j = elenco.get(index);
        j.setValid(false);
        elenco.set(index, j);
        DefaultTableModel model = (DefaultTableModel) tabella.getModel();
        model.setValueAt("Cancellato", index, 4);
    }

    private void modJob(int index){
        boolean open = elenco.get(index).isFix();
        deleteJob(index);
        try {
            if (open) {
                openArchive();
            }
            else{
                createArchive();
            }
        } catch (FileNotFoundException e) {
            this.summonErrorPopup("File non trovato.");
        }
    }

    private void runAllJobs(){
        Vector<JobThread> threads = new Vector<JobThread>();
        for(JobDescriptor j:elenco){
            if(j.isValid()){
                threads.add(new JobThread(j));}
        }
        for(JobThread t:threads){
            t.run();
        }
        for(int i=0; i<elenco.size();i++){
            elenco.remove(i);
        }
    }
}
