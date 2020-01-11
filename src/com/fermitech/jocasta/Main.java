package com.fermitech.jocasta;
import com.fermitech.jocasta.gui.*;

import javax.swing.*;
import java.awt.*;
/**
 * @author Lorenzo Balugani
 * @version 1.0
 */
public class Main {
    /**
     * This is the runner. It creates the frame, and loads up the panel.
     * If requested by the user, it starts the Command Line Interface.
     */
    public static void main(String[] args) {
        boolean force_CLI = false;
        if(args.length == 0 && !force_CLI) {
            JFrame f = new JFrame("Jocasta-Nu archive manager");
            Container container = f.getContentPane();
            AutoPanel pannello = new MainPanel(f);
            container.add(pannello);
            f.setVisible(true);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1050, 500);
            f.setResizable(false);
        }
        else{
            if(force_CLI || args[0].equals("no-gui")){
                System.out.println("Avvio del programma in modalita' CLI");
                CommandLineInterface cli = new CommandLineInterface();
                cli.run();
            }
            else{
                System.out.println("Per avviare il programma senza CLI, usare [nome].jar no-gui.");
            }
        }
    }
}
