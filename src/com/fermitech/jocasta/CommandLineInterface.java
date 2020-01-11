package com.fermitech.jocasta;

import com.fermitech.jocasta.core.JobDescriptor;
import com.fermitech.jocasta.core.JobThread;
import com.fermitech.jocasta.gui.AutoPanel;
import com.fermitech.jocasta.gui.FileInputOptions;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
/**
 * This is the CommandLineInterface class, used to provide the same functionalities to a machine not capable of driving
 * a GUI. It's a bit of a hack, but it works just fine.
 */
public class CommandLineInterface {
    private ArrayList<JobDescriptor> elenco;
    private Vector<JobThread> threads;
    /**
     * This is the CommandLineInterface class constructor.
     * It initializes the Jobs ArrayList and the threads Vector.
     */
    public CommandLineInterface() {
        this.elenco = new ArrayList<JobDescriptor>();
        this.threads = new Vector<JobThread>();
    }
    /**
     * This is the CommandLineInterface run() method.
     * It displays the menu, and registers user input.
     */
    public void run() {
        String menu = "1- Crea archivio\n2- Apri archivio\n3- Visualizza i Job pianificati\n4- Elimina un job\n5- Modifica un job\n6- Esegui i job\n7- esci\n";
        Scanner s = new Scanner(System.in);
        int scelta, id;
        boolean loop = true;
        while (loop) {
            System.out.println(menu);
            scelta = s.nextInt();
            switch (scelta) {
                case 1:
                    createArchive();
                    break;
                case 2:
                    openArchive();
                    break;
                case 3:
                    showJobs();
                    break;
                case 4:
                    System.out.println("Immetti l'id del job da eliminare:");
                    id = s.nextInt();
                    delJob(id);
                    break;
                case 5:
                    System.out.println("Immetti l'id del job da modificare:");
                    id = s.nextInt();
                    modJob(id);

                    break;
                case 6:
                    runAllJobs();
                    break;
                case 7:
                    loop = false;
                    System.out.println("Jocasta-Nu è un programma creato da Lorenzo Balugani per l'esame di Programmazione a Oggetti dell'univeristà UNIMORE.\n");
                    break;
            }
        }
    }
    /**
     * This is the CommandLineInterface createArchive() method.
     * It allows the creation of archives.
     */
    private void createArchive() {
        FileInputOptions file = new FileInputOptions("cli");
        String src, dst, scelta, password, div_value;
        Scanner s = new Scanner(System.in);
        System.out.println("Immetti il percorso del file da archiviare: ");
        src = s.nextLine();
        System.out.println("Immetti il percorso della cartella di destinazione: ");
        dst = s.nextLine();
        System.out.println("Il file deve essere crittografato? [y/n] ");
        scelta = s.nextLine();
        if (scelta.equals("y")) {
            System.out.println("Immetti la password. ");
            password = s.nextLine();
            file.setCifra(true);
            file.setPassword(password);
        }
        System.out.println("Il file deve essere compresso? [y/n] ");
        scelta = s.nextLine();
        if (scelta.equals("y")) {
            file.setComprimi(true);
        }
        System.out.println("Il file deve essere divisi in tot kb? [y/n] ");
        scelta = s.nextLine();
        if (scelta.equals("y")) {
            file.setDimensione(true);
            System.out.println("Immetti la dimensione in kb. ");
            div_value = s.nextLine();
            file.set_value(div_value);
        } else {
            file.setParti(true);
            System.out.println("Immetti il numero di file. ");
            div_value = s.nextLine();
            file.set_value(div_value);
        }
        JobDescriptor j = new JobDescriptor(file, src, dst, elenco.size());
        try {
            j.BuildJobs();
            elenco.add(j);
        } catch (FileNotFoundException e) {
            System.out.println("Uno dei percorsi non è corretto. Aggiunta arrestata.");
        }
        System.out.println("Vuoi aggiungere un altro file? [y/n] ");
        scelta = s.nextLine();
        if (scelta.equals("y")) {
            createArchive();
        }
    }
    /**
     * This is the CommandLineInterface openArchive() method.
     * It allows archives to be open.
     */
    private void openArchive() {
        String src, dst, password;
        Scanner s = new Scanner(System.in);
        System.out.println("Immetti il percorso del file da dearchiviare: ");
        src = s.nextLine();
        System.out.println("Immetti il percorso della cartella di destinazione: ");
        dst = s.nextLine();
        System.out.println("Immetti la password con il quale è stato protetto il file. Se non è cifrato, metti uno spazio bianco. ");
        password = s.nextLine();
        JobDescriptor j = new JobDescriptor(src, dst, password, elenco.size());
        try {
            j.BuildJobs();
            elenco.add(j);
        } catch (FileNotFoundException e) {
            System.out.println("Uno dei percorsi non è corretto. Aggiunta arrestata.");
        }
    }
    /**
     * This is the CommandLineInterface showJobs() method.
     * It displays the valid jobs.
     */
    private void showJobs() {
        System.out.println("\nLista dei job pianificati.");
        for (JobDescriptor j : elenco) {
            if (j.isValid()) {
                System.out.println(j.getId() + " " + j.getSrc_path() + " " + j.getDst_path() + " " + j.getDescrizione()+"\n");
            }
        }
    }
    /**
     * This is the CommandLineInterface delJobs() method.
     * It deletes a certain job.
     */
    private void delJob(int index){
        try{
            elenco.get(index).setValid(false);
        } catch (IndexOutOfBoundsException e){
            System.out.println("Non esiste un job con id "+index+".");
        }
    }
    /**
     * This is the CommandLineInterface modJobs() method.
     * It edits a certain job.
     */
    private void modJob(int index){
        boolean apri = elenco.get(index).isFix();
        delJob(index);
        if(apri){
            openArchive();
        }
        else{
            createArchive();
        }
    }
    /**
     * This is the CommandLineInterface runJobs() method.
     * It runs all valid Jobs.
     */
    private void runAllJobs() {
        Vector<JobThread> threads = new Vector<JobThread>();
        for (JobDescriptor j : elenco) {
            if (j.isValid()) {
                threads.add(new JobThread(j, null));
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
