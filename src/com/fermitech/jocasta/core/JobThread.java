package com.fermitech.jocasta.core;

import com.fermitech.jocasta.gui.AutoPanel;

import java.io.IOException;

public class JobThread extends Thread{
    private JobDescriptor job_list;
    private int progress;
    public JobThread(JobDescriptor j){
        this.job_list = j;
        progress = 0;
    }

    @Override
    public void run() {
        try {
            this.job_list.RunJobs();
        } catch (IOException e) {
            AutoPanel a = new AutoPanel("Error");
            a.summonErrorPopup("Qualcosa è andato storto durante l'elaborazione del job.");
        }
    }
}
