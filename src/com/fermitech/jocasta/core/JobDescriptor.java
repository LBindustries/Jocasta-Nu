package com.fermitech.jocasta.core;

import com.fermitech.jocasta.gui.FileInputOptions;
import com.fermitech.jocasta.jobs.*;
import com.fermitech.jocasta.core.Coda;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.NoSuchAlgorithmException;

/**
 * This is the JobDescriptor class, the class that contains the queue of jobs to perform on a particular file.
 */
public class JobDescriptor {
    private boolean crypt, compress, cut_size, cut_parts, decompress, decrypt, fix;
    private String src_path, dst_path, password;
    private int division_value;
    private boolean error;
    private int tot_jobs, curr_jobs;
    Coda queue;
    String[] ext = null;
    String descrizione;
    private int id;
    private boolean valid;

    /**
     * This is one of the two constructor of the JobDescriptor class.
     * This one is intended to work as the constructor of a JobDescriptor whose file needs to be archived.
     * If something is wrong, the JobDescriptor is marked as faulty.
     *
     * @param options  the JPanel element in which the user defines the nature of the jobs.
     * @param src_path the string containing the path of the file.
     * @param dst_path the string containing the destination of the file.
     * @param id       the identification number of the JobDescriptor.
     */
    public JobDescriptor(FileInputOptions options, String src_path, String dst_path, int id) {
        this.crypt = options.getCrypt();
        this.compress = options.getComp();
        this.cut_size = options.getDim();
        this.cut_parts = options.getPart();
        this.password = options.getPassword();
        try {
            this.division_value = Integer.parseInt(options.get_value());
            this.error = false;
        } catch (NumberFormatException e) {
            this.error = true;
        }
        this.src_path = src_path;
        this.dst_path = dst_path;
        this.queue = new Coda();
        this.tot_jobs = 0;
        this.curr_jobs = 0;
        this.descrizione = "";
        this.id = id;
        valid = true;
    }

    /**
     * This is one of the two constructor of the JobDescriptor class.
     * This one is intended to work as the constructor of a JobDescriptor whose file needs to be unarchived.
     * Based on the extension, it enables the different jobs.
     *
     * @param src_path the string containing the path of the file.
     * @param dst_path the string containing the destination of the file.
     * @param password the password.
     * @param id       the identification number of the JobDescriptor.
     */
    public JobDescriptor(String src_path, String dst_path, String password, int id) {
        File file = new File(src_path);
        this.ext = file.getName().split("\\.");
        for (String tmp : ext) {
            if (tmp.equals("cry")) {
                decrypt = true;
            } else if (tmp.equals("zip")) {
                decompress = true;
            } else if (tmp.equals("joca")) {
                fix = true;
            }
        }
        this.password = password;
        this.src_path = src_path;
        this.dst_path = dst_path;
        this.queue = new Coda();
        this.descrizione = "";
        this.id = id;
        valid = true;
    }

    /**
     * This is the BuildOutJobs method of the JobDescriptor class.
     * It creates the needed jobs, daisy-chains them together and changes the src path to accomodate filename changes.
     */
    private void BuildOutJobs() throws FileNotFoundException {
        String src, dst;
        src = this.src_path;
        dst = this.dst_path;
        File tmp = new File(this.src_path);
        descrizione += "Crea ";
        if (!crypt && !compress) {
            if (cut_parts) {
                queue.add(new FixedNumberSplitJob(src, dst, this.division_value));
                descrizione += "Dividi in " + division_value + " file";
            } else {
                queue.add(new SizeSplitJob(src, dst, this.division_value));
                descrizione += "Dividi in file da " + division_value + " kb";
            }
            tot_jobs++;
        } else {
            if (crypt) {
                try {
                    queue.add(new CryptoJob(src, dst, this.password));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                src = dst + "/" + tmp.getName() + ".cry";
                tot_jobs++;
                descrizione += "Cifra ";
            }
            if (compress) {
                queue.add(new ZipJob(src, dst));
                src = dst + "/" + tmp.getName();
                if (crypt) {
                    src = src + ".cry";
                }
                src = src + ".zip";
                tot_jobs++;
                descrizione += "Comprimi ";
            }
            if (cut_parts) {
                queue.add(new FixedNumberSplitJob(src, dst, this.division_value));
                descrizione += "Dividi in " + division_value + " file";
            } else {
                queue.add(new SizeSplitJob(src, dst, this.division_value));
                descrizione += "Dividi in file da " + division_value + " kb";
            }
            tot_jobs++;
        }
    }

    /**
     * This is the BuildInJobs method of the JobDescriptor class.
     * It does the same thing BuildOutJobs does, but for files that need to be unarchived.
     */
    private void BuildInJobs() throws FileNotFoundException {
        String src, dst;
        src = this.src_path;
        dst = this.dst_path;
        descrizione += "Apri ";
        if (fix) {
            queue.add(new FixJob(src, dst));
            src = nextFileNameGenerator("joca");
            descrizione += "Ricompatta ";
            tot_jobs++;
        }
        if (decompress) {
            queue.add(new UnZipJob(src, dst));
            src = nextFileNameGenerator("zip");
            descrizione += "Decomprimi ";
            tot_jobs++;
        }
        if (decrypt) {
            queue.add(new DecryptJob(src, dst, password));
            src = nextFileNameGenerator("cry");
            descrizione += "Decifra ";
            tot_jobs++;
        }
    }

    /**
     * This is the BuildJobs method of the JobDescriptor class.
     * It calls BuildOutJobs or BuildInJobs according to the purpose of the JobDescriptor.
     */
    public void BuildJobs() throws FileNotFoundException {
        if (cut_size || cut_parts) {
            BuildOutJobs();
        } else {
            BuildInJobs();
        }
    }

    /**
     * This is the RunNextJob method of the JobDescriptor class.
     * It returns the Job located on top of the queue.
     *
     * @return the Job on top of the queue.
     */
    public Job RunNextJob() {
        return queue.pop();
    }

    /**
     * This is the getQueue method of the JobDescriptor class.
     * It returns the queue.
     *
     * @return the queue
     */
    public Coda getQueue() {
        return queue;
    }

    /**
     * This is the getFlag method of the JobDescriptor class.
     * It returns the error flag.
     *
     * @return the error flag.
     */
    public boolean getFlag() {
        return error;
    }

    /**
     * This is the nextFileNameGenerator method of the JobDescriptor class.
     * It generates the next filename given a vector representing the current filename.
     *
     * @param exte a vector of extensions.
     * @return the next filename.
     */
    protected String nextFileNameGenerator(String exte) {
        String result = "";
        for (String tmp : this.ext) {
            if (tmp.equals(exte)) {
                break;
            }
            if (result.equals("")) {
                result = tmp;
            } else {
                result = result + "." + tmp;
            }
        }
        return dst_path + "/" + result;
    }

    /**
     * This is the getDst_path method of the JobDescriptor class.
     * It returns the destination path.
     *
     * @return the destination path.
     */
    public String getDst_path() {
        return dst_path;
    }

    /**
     * This is the getSrc_path method of the JobDescriptor class.
     * It returns the source path.
     *
     * @return the source path.
     */
    public String getSrc_path() {
        return src_path;
    }

    /**
     * This is the getDivision_value method of the JobDescriptor class.
     * It returns the number used to divide files.
     *
     * @return the division number.
     */
    public int getDivision_value() {
        return division_value;
    }

    /**
     * This is the isCrypt method of the JobDescriptor class.
     * It returns the crypt boolean.
     *
     * @return if it has to encrypt or not.
     */
    public boolean isCrypt() {
        return crypt;
    }

    /**
     * This is the isCompress method of the JobDescriptor class.
     * It returns the compress boolean.
     *
     * @return if it has to compress or not.
     */
    public boolean isCompress() {
        return compress;
    }

    /**
     * This is the isCutParts method of the JobDescriptor class.
     * It returns the cut_parts boolean.
     *
     * @return if it has to cut the file in n° parts or not.
     */
    public boolean isCut_parts() {
        return cut_parts;
    }

    /**
     * This is the isCutSize method of the JobDescriptor class.
     * It returns the cut_size boolean.
     *
     * @return if it has to cut the file in parts of n° kb or not.
     */
    public boolean isCut_size() {
        return cut_size;
    }

    /**
     * This is the isDecompress method of the JobDescriptor class.
     * It returns the decompress boolean.
     *
     * @return if it has to decompress the file or not.
     */
    public boolean isDecompress() {
        return decompress;
    }

    /**
     * This is the isDecrypt method of the JobDescriptor class.
     * It returns the decrypt boolean.
     *
     * @return if it has to decrypt the file or not.
     */
    public boolean isDecrypt() {
        return decrypt;
    }

    /**
     * This is the isError method of the JobDescriptor class.
     * It returns the error boolean.
     *
     * @return if something went wrong or not.
     */
    public boolean isError() {
        return error;
    }

    /**
     * This is the isFix method of the JobDescriptor class.
     * It returns the fix boolean.
     *
     * @return if it has to regrup files or not.
     */
    public boolean isFix() {
        return fix;
    }

    /**
     * This is the getDescrizione method of the JobDescriptor class.
     * It returns the description string which it's used inside the table to represent the needed jobs.
     *
     * @return if it has to regrup files or not.
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * This is the getCurrJobs method of the JobDescriptor class.
     * It returns the number of completed jobs.
     *
     * @return the number of completed jobs.
     */
    public int getCurr_jobs() {
        return curr_jobs;
    }

    /**
     * This is the setCurrJobs method of the JobDescriptor class.
     * It sets the curr_jobs at a certain value.
     *
     * @param curr_jobs the new number of jobs.
     */
    public void setCurr_jobs(int curr_jobs) {
        this.curr_jobs = curr_jobs;
    }

    /**
     * This is the getId method of the JobDescriptor class.
     * It returns the id of the JobDescriptor.
     *
     * @return the id.
     */
    public int getId() {
        return id;
    }

    /**
     * This is the isValid method of the JobDescriptor class.
     * It returns the value of the valid boolean.
     *
     * @return if the JobDescriptor can be executed or not.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * This is the setValid method of the JobDescriptor class.
     * It sets the valid at a certain value.
     *
     * @param valid the new value of valid.
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * This is the getTotJobs method of the JobDescriptor class.
     * It returns the total number of required jobs.
     *
     * @return the number of jobs.
     */
    public int getTot_jobs() {
        return tot_jobs;
    }

    @Override
    public String toString() {
        return "Cr: " + this.crypt + " Co: " + this.compress + " Cp: " + this.cut_parts + " Cs: " + this.cut_size + " Pw: " + this.password + " Va: " + this.division_value + "\nIn: " + this.src_path + " Ou: " + this.dst_path;
    }
}
