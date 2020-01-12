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

/**
 * This is the MainPanel class, which implements the main ui elements.
 */
public class MainPanel extends AutoPanel implements ActionListener {
    private JMenu menu;
    private JMenuBar mb;
    private JMenuItem crea, apri, about;
    private JTable tabella;
    private Vector<JobDescriptor> elenco = new Vector<JobDescriptor>();
    private JButton start, delete, mod;
    private JLabel id_label;
    private JTextField id;
    private Vector<JobThread> threads;

    /**
     * This is the MainPanel constructor.
     * It initializes the ui, using the menuBarSetup and tableSetup methods.
     *
     * @param frame the frame that hosts the panel, used to setup the JMenuBar.
     */
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
        elenco = new Vector<JobDescriptor>();
    }

    /**
     * This is the MainPanel menuBarSetup method.
     * It initializes the JMenuBar.
     *
     * @param frame the frame that hosts the panel.
     */
    private void menuBarSetup(JFrame frame) {
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

    /**
     * This is the MainPanel tableSetup method.
     * It initializes the JTable.
     */
    private void tableSetup() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Bersaglio");
        model.addColumn("Dest.");
        model.addColumn("Job richiesti");
        model.addColumn("Status");
        tabella = new JTable(model) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scrollPane = new JScrollPane(tabella, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(1000, 400));
        this.add(scrollPane);
    }

    /**
     * This is the MainPanel addToTable method.
     * It adds a row to the JTable.
     *
     * @param j the JobDescriptor that needs to be represented.
     */
    private void addToTable(JobDescriptor j) {
        DefaultTableModel model = (DefaultTableModel) tabella.getModel();
        model.addRow(new Object[]{j.getId(), j.getSrc_path(), j.getDst_path(), j.getDescrizione(), "Pianificato"});
    }

    /**
     * This is the MainPanel chooserGetFile method.
     * It spawns a CustomChooser that is then used to return the path of the target using a string.
     * If the CustomChooser is closed, it signals the error.
     *
     * @param options      the optional AutoPanel that can be used to interact with the user.
     * @param title        the title of the CustomChooser.
     * @param only_folders if true, only folders will be displayed.
     * @return the path of the target.
     */
    private String chooserGetFile(AutoPanel options, String title, boolean only_folders) {
        CustomChooser cc1 = new CustomChooser(options, title, only_folders, false);
        int returnvalue = cc1.showOpenDialog(null);
        if (returnvalue != JFileChooser.APPROVE_OPTION) {
            return "error";
        }
        return cc1.getSelectedFile().getAbsolutePath();
    }

    /**
     * This is the MainPanel chooserGetMultipleFiles method.
     * It spawns a CustomChooser that is then used to return the paths of the targets using a string vector.
     * If the CustomChooser is closed, it signals the error.
     *
     * @param options      the optional AutoPanel that can be used to interact with the user.
     * @param title        the title of the CustomChooser.
     * @param only_folders if true, only folders will be displayed.
     * @return vector containing the paths of the targets.
     */
    private String[] chooserGetMultipleFiles(AutoPanel options, String title, boolean only_folders) {
        CustomChooser cc1 = new CustomChooser(options, title, only_folders, true);
        int returnvalue = cc1.showOpenDialog(null);
        if (returnvalue != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        File[] files = cc1.getSelectedFiles();
        String[] result = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            result[i] = files[i].getAbsolutePath();
        }
        return result;
    }

    /**
     * This is the MainPanel createArchive method.
     * It handles the creation of JobDescriptor aimed at the creation of archives and it spawns CustomChoosers.
     */
    private void createArchive() throws FileNotFoundException {
        FileInputOptions opzioni = new FileInputOptions("Opzioni");
        String[] files = chooserGetMultipleFiles(opzioni, "Seleziona i file", false);
        String dst = chooserGetFile(null, "Seleziona la cartella di destinazione", true);
        if (files == null) {
            return;
        }
        for (String source : files) {
            JobDescriptor j = new JobDescriptor(opzioni, source, dst, elenco.size());
            if (j.getFlag()) {
                this.summonErrorPopup("La stringa inserita non è un numero.\nL'operazione è stata annullata.");
                return;
            }
            if (j.getSrc_path().equals("error") || j.getDst_path().equals("error")) {
                return;
            }
            j.BuildJobs();
            System.out.println(j);
            elenco.add(j);
            addToTable(j);
        }
    }

    /**
     * This is the MainPanel openArchive method.
     * It handles the creation of JobDescriptor aimed at the dearchiving of archives and it spawns CustomChoosers.
     */
    private void openArchive() throws FileNotFoundException {
        FileExtractionOptions opzioni = new FileExtractionOptions("Opzioni");
        String[] files = chooserGetMultipleFiles(opzioni, "Seleziona i file", false);
        //String src = chooserGetFile(opzioni, "Seleziona l'archivio", false);
        String dst = chooserGetFile(null, "Seleziona la cartella di destinazione", true);
        if (files == null) {
            return;
        }
        for (String source : files) {
            JobDescriptor j = new JobDescriptor(source, dst, opzioni.getPassword(), elenco.size());
            if (j.getFlag()) {
                this.summonErrorPopup("La stringa inserita non è un numero.\nL'operazione è stata annullata.");
                return;
            }
            if (j.getSrc_path().equals("error") || j.getDst_path().equals("error")) {
                return;
            }
            j.BuildJobs();
            System.out.println(j);
            elenco.add(j);
            addToTable(j);
        }
    }

    /**
     * This is the MainPanel actionPerformed method.
     * It reacts to changes to the ui, and runs specific features.
     *
     * @param e the event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == crea) {
            try {
                createArchive();
            } catch (FileNotFoundException ex) {
                this.summonErrorPopup("File non trovato.\nChe gli archivi siano incompleti?");
            }
        } else if (e.getSource() == apri) {
            try {
                openArchive();
            } catch (FileNotFoundException ex) {
                this.summonErrorPopup("File non trovato.\nChe gli archivi siano incompleti?");
            }
        } else if (e.getSource() == start && elenco.size() > 0) {
            runAllJobs();
        } else if (e.getSource() == delete && elenco.size() > 0) {
            try {
                deleteJob(Integer.parseInt(id.getText()));
            } catch (Exception ex) {
                this.summonErrorPopup("Il numero inserito non è valido.");
            }
        } else if (e.getSource() == mod && elenco.size() > 0) {
            try {
                modJob(Integer.parseInt(id.getText()));
            } catch (Exception ex) {
                this.summonErrorPopup("Il numero inserito non è valido.");
            }
        }
        else if (e.getSource()==about){
            JOptionPane.showMessageDialog(null, "Jocasta-Nu è un programma scritto da Lorenzo Balugani per l'esame di Programmazione a Oggetti dell'università UNIMORE.\nIl nome del programma è una citazione al personaggio dell'archivista di \"L'attacco dei Cloni\", della serie Star Wars, il cui nome è appunto Jocasta-Nu.");
        }
    }

    /**
     * This is the MainPanel deleteJob method.
     * Given an index, it disables the job and marks it as cancelled on the table.
     *
     * @param index the index.
     */
    private void deleteJob(int index) {
        JobDescriptor j = elenco.get(index);
        if (!j.isValid()) {
            return;
        }
        j.setValid(false);
        elenco.set(index, j);
        DefaultTableModel model = (DefaultTableModel) tabella.getModel();
        model.setValueAt("Cancellato", index, 4);
    }
    /**
     * This is the MainPanel modJob method.
     * Given an index, it disables the job and marks it as cancelled on the table.
     * It then proceeds to create a new one.
     *
     * @param index the index.
     */
    private void modJob(int index) {
        boolean open = elenco.get(index).isFix();
        if (!elenco.get(index).isValid()) {
            return;
        }
        deleteJob(index);
        try {
            if (open) {
                openArchive();
            } else {
                createArchive();
            }
        } catch (FileNotFoundException e) {
            this.summonErrorPopup("File non trovato.");
        }
    }
    /**
     * This is the MainPanel runAllJobs method.
     * It runs all the jobs that haven't been deleted or completed.
     */
    private void runAllJobs() {
        Vector<JobThread> threads = new Vector<JobThread>();
        for (JobDescriptor j : elenco) {
            if (j.isValid()) {
                threads.add(new JobThread(j, this.tabella));
            }
        }
        for (JobThread t : threads) {
            t.start();
        }
        for (int i = 0; i < elenco.size(); i++) {
            elenco.get(i).setValid(false);
        }
    }
}
