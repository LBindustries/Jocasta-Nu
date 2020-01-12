package com.fermitech.jocasta;

import com.fermitech.jocasta.gui.*;

import javax.swing.*;
import java.awt.*;
/**
 * @author Lorenzo Balugani
 * @version 1.0
 */

/**
 * To whom it may interest,
 * this program was written for the Programmazione a Oggetti exam in UNIMORE. It is a simple archive manager written
 * in Java, that both supports a GUI and a command line interface. Writing this program was a challenge, but I think
 * I've managed to meet all the requests of the exercise.
 * In this section, I will explain some of the implementation choices and the major problems encountered while working
 * on this project.
 * The project is divided in 3 packages: Core, which is the package that contains all the utilities and mechanisms that
 * allow jobs to work, gui, which is the package that contains the code of all the different gui elements, and jobs, the
 * package that contains all the specialized and un-specialized classes that create the files step by step.
 * <p>
 * The main concept behind everything is to have a series of entities, the jobs, that have a specialized method (execute)
 * that, given a source path and a destination path, are able to take care of the required action.
 * A complex job, such as Crypt->Compress->Split is done via composition of 3 different jobs, that are daisy-chained
 * toghether: the destination of the first job will be the source of the second, and so on. This composition is
 * done by the JobDescriptor class, which is a spawner of jobs: for each file that needs to be worked on, there's a
 * JobDescriptor containing a queue of jobs, that get removed after completition. When the user decides to execute all
 * the jobs, each JobDescriptor is given to a JobThread which will execute all the jobs available.
 * In the classes, the Vector class was chosen in spite of ArrayList, this is because Vector is thread-safe and, in
 * earlier implementations, it had to be like this.
 * In order to not load up the entire file into system memory, the system operates in a 8-KB chunk size, meaning that
 * it reads 8KB from disk, it writes them and then goes on.
 * In the Enigma module I took a kindof disagreeable choice: the function makeChipher (which is not a typo, it's just
 * an inside-joke) uses two salts, one for the keySpec and the other one for the initialization vector. Since the two
 * salts need to be the same while crypting and decrypting, I had three possible choices:
 * 1) Make the salts static, which kinda sounds unsafe
 * 2) Generate the salts and save them in the file
 * 3) Generate the salts using the password as a salt
 * Since I wanted to keep the complexity of an already complex program simple, and I didn't want to break anything,
 * I choose the 3rd method.
 * <p>
 * This project had some major stressful moments, usually when I couldn't locate bugs and Java wasn't helping:
 * for example, I took me hours to notice what was going on with BufferedStreams and FileStreams. Coming from other programming
 * languages, my rationale around readers and writers was that when the file is over, it simply stops and does not fill
 * the buffer with copies of the last byte. Turns out that this was not the case, and that the program wasn't working
 * because of this.
 * <p>
 * I hope your experience with Jocasta-Nu will be flawless and satisfying.
 * If you're interested, you can expand it or improve the code, or translate it to english,
 * as long as long as your job is GPL3 compliant.
 * I thank you for your attention.
 * <p>
 * Best regards,
 * Lorenzo Balugani
 */
public class Main {
    /**
     * This is the runner. It creates the frame, and loads up the panel.
     * If requested by the user, it starts the Command Line Interface.
     */
    public static void main(String[] args) {
        boolean force_CLI = false;
        if (args.length == 0 && !force_CLI) {
            JFrame f = new JFrame("Jocasta-Nu archive manager");
            Container container = f.getContentPane();
            AutoPanel pannello = new MainPanel(f);
            container.add(pannello);
            f.setVisible(true);
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setSize(1050, 500);
            f.setResizable(false);
        } else {
            if (force_CLI || args[0].equals("no-gui")) {
                System.out.println("Avvio del programma in modalita' CLI");
                CommandLineInterface cli = new CommandLineInterface();
                cli.run();
            } else {
                System.out.println("Per avviare il programma senza CLI, usare java -jar [nome].jar no-gui.");
            }
        }
    }
}
