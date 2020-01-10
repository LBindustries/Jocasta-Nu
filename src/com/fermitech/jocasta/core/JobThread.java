package com.fermitech.jocasta.core;

import com.fermitech.jocasta.gui.AutoPanel;
import com.fermitech.jocasta.jobs.Job;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;

/**
 * This is the JobThread class, wich allows the jobs to be executed concurrently.
 */
public class JobThread extends Thread {
    private JobDescriptor job_list;
    private JTable tabella;
    private int progress;

    /**
     * This is the JobThread class constructor.
     * It takes the JobDescriptor and the table from the main view in order to update it.
     *
     * @param j     the JobDescriptor.
     * @param table the table from the main view.
     */
    public JobThread(JobDescriptor j, JTable table) {
        super();
        this.job_list = j;
        progress = 0;
        this.tabella = table;
    }

    /**
     * This is the JobThread run method.
     * For each job inside the job_list's queue, the job gets executed and, after each step, the table gets updated.
     */
    @Override
    public void run() {
        try {
            DefaultTableModel model = (DefaultTableModel) tabella.getModel();
            model.setValueAt("In esecuzione", job_list.getId(), 4);
            for (Job job : job_list.getQueue().getList()) {
                System.out.println(job);
                model.setValueAt("In esecuzione " + (job_list.getCurr_jobs() + 1) + "/" + job_list.getTot_jobs(), job_list.getId(), 4);
                job.execute();
                progress = job_list.getCurr_jobs();
                if (progress >= 1) {
                    File file = new File(job.getSource());
                    file.delete();
                }
                job_list.setCurr_jobs(progress + 1);
            }
        } catch (IOException e) {
            AutoPanel a = new AutoPanel("Error");
            a.summonErrorPopup("Qualcosa è andato storto durante l'elaborazione del job " + job_list.getId() + ".");
        }
        DefaultTableModel model = (DefaultTableModel) tabella.getModel();
        model.setValueAt("Completato", job_list.getId(), 4);
    }

}
