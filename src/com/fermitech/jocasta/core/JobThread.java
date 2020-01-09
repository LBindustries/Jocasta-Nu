package com.fermitech.jocasta.core;

import com.fermitech.jocasta.gui.AutoPanel;
import com.fermitech.jocasta.jobs.Job;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.IOException;

public class JobThread extends Thread {
    private JobDescriptor job_list;
    private JTable tabella;
    private int progress;

    public JobThread(JobDescriptor j, JTable table) {
        this.job_list = j;
        progress = 0;
        this.tabella = table;
    }

    @Override
    public void run() {
        try {
            for (Job job : job_list.getQueue().getList()) {
                System.out.println(job);
                job.execute();
                progress = job_list.getCurr_jobs();
                if (progress >= 1) {
                    File file = new File(job.getSource());
                    file.delete();
                }
                job_list.setCurr_jobs(progress+1);
                DefaultTableModel model = (DefaultTableModel) tabella.getModel();
                model.setValueAt(progress+"/"+job_list.getTot_jobs(), job_list.getId(), 4);
            }
        } catch (IOException e) {
            AutoPanel a = new AutoPanel("Error");
            a.summonErrorPopup("Qualcosa è andato storto durante l'elaborazione del job.");
        }
        DefaultTableModel model = (DefaultTableModel) tabella.getModel();
        model.setValueAt("Completato", job_list.getId(), 4);
    }

}
