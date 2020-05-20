package app;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import java.lang.Exception;

/**
 * ExceptionSaver
 */
public class ExceptionSaver implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1958651366709928859L;

    class PException implements Serializable{
        /**
         *
         */
        private static final long serialVersionUID = 5657364999298734535L;
        int linenbr;
        String pakage;
        String method;
        String clas;
        Date timeocc;
        String message;
        
        PException( final Exception e) {
            this.linenbr = e.getStackTrace()[0].getLineNumber();
            this.pakage = e.getStackTrace()[0].getFileName();
            this.method = e.getStackTrace()[0].getMethodName();
            this.clas = e.getStackTrace()[0].getClassName();
            this.timeocc = new Date();
            this.message = e.getMessage();
        }

        public String toString() {
            return timeocc.toString() + " :::exception generer à: " + pakage + " " + clas + " " + " " + method + " "
                    + linenbr + " " + message;
        }
    }

    ArrayList<PException> logEx;
    private final String textFname = System.getProperty("user.dir") + "/ExcetpionsLog.txt";
    private final String serFname = System.getProperty("user.dir") + "/ExceptionsLogser.ser";
    private final String xmlFname = System.getProperty("user.dir") +"/ExceoptionsLog.xml";
    public ExceptionSaver() {
        logEx = new ArrayList<PException>();
    }

    public void add(final Exception e) {
        final PException pe = new PException(e);
        logEx.add(pe);
    }

    public void printJSer() {
        for (final PException pException : logEx) {
            System.out.println(pException);
        }
    }

    public void printLogtxt() {
        try {
            final FileReader fr = new FileReader(textFname);
            final BufferedReader br = new BufferedReader(fr);
            try {
                String ln = br.readLine();
                while (ln != null) {
                    System.out.println(ln);
                    ln = br.readLine();
                }
                br.close();
                fr.close();
            } catch (final IOException ioe) {
            
                System.out.println("error while reading");
                this.save(ioe);
                this.add(ioe);
            }

        } catch (final FileNotFoundException fnf) {
            
            this.save(fnf);
            this.add(fnf);
            System.out.println("File not found exception saved create file");
        } catch (final Exception ee) {
            System.out.println("General Exception catched and saved");
            this.save(ee);
            this.add(ee);
        }
    }

    // text file save
    public void save(final Exception e) {
            final PException pe = new PException(e);
        try {
            final File file = new File(textFname);
            final FileWriter fw = new FileWriter(file, true);
            final BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            bw.write(pe.toString());
            bw.close();
            fw.close();
        } catch (final IOException ioe) {
            this.add(ioe);
            this.save(ioe);
            System.out.println("IO error occured had to do a recurssion save");
        } catch (final Exception ee) {
            System.out.println("General Exception catched and saved");
            this.save(ee);
            this.add(ee);
        }
    }

    public void saveTxt() {

        try {
            final File file = new File(textFname);
            final FileWriter fw = new FileWriter(file, true);
            final BufferedWriter bw = new BufferedWriter(fw);
            bw.newLine();
            for (final PException pe : logEx) {
                bw.write(pe.toString());
                bw.newLine();
            }
            bw.close();
            fw.close();
        } catch (final IOException ioe) {

            this.save(ioe);
            this.add(ioe);
            System.out.println("IO error occured had to do a recurssion save");
        } catch (final Exception ee) {
            System.out.println("General Exception catched and saved");
            this.save(ee);
            this.add(ee);
        }
    }

    // Serialisation
    public void saveSer() {
        try {
            final FileOutputStream fos = new FileOutputStream(new File(serFname));
            final ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.logEx);
            oos.close();
            fos.close();

        } catch (final FileNotFoundException fnf) {
            this.save(fnf);
            this.add(fnf);
            System.out.println("File not found create it and reload");
        } catch (final IOException ioe) {
            this.save(ioe);
            System.out.println("IO error occured had to do a recurssion save");
        } catch (final Exception e) {
            System.out.println("General Exception catched and saved");
            this.save(e);
            this.add(e);
        }
    }

    // loading a ser file
    public void loadSer() {

        try {
            final FileInputStream fis = new FileInputStream(serFname);
            final ObjectInputStream ois = new ObjectInputStream(fis);
            this.logEx = (ArrayList<PException>) ois.readObject();

            ois.close();
            fis.close();

        } catch (final FileNotFoundException fnf) {
            this.save(fnf);
            this.add(fnf);
            System.out.println("the file not found impossible we are supposed to create it");
        } catch (final IOException ioe) {
            this.save(ioe);
            this.add(ioe);
            System.out.println("IO error occured had to do a recurssion save");
        } catch (final Exception e) {
            System.out.println("General Exception catched and saved");
            this.save(e);
            this.add(e);
        }


    }
    

    public void saveXml() {
        try {
            FileWriter fw = new FileWriter(xmlFname, false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("<exceptions>");
            bw.newLine();
                for(PException pe : logEx){
                    String ex = "\t<exception>\n";
                    ex += "\t\t<Date>"+pe.timeocc.toString()+"</Date>\n\t\t<ClassPackage>"+pe.pakage+"</ClassPackage>\n\t\t<ClassName>"+pe.clas+"</ClassName>\n\t\t<MethodName>"+pe.method+"</MethodName>\n\t\t<LigneNumber>"+pe.linenbr+"</LigneNumber>\n";                    
                    ex+="\t</exception>\n";
                    bw.write(ex);
                    bw.newLine();
                }
            bw.write("</exceptions>");
            bw.newLine();
            bw.close();
            fw.close();
        } catch (final IOException ioe) {
            this.add(ioe);
            this.save(ioe);
            System.out.println("IO error occured had to do a recurssion save");
        } catch (final Exception ee) {
            System.out.println("General Exception catched and saved");
            this.save(ee);
            this.add(ee);
        }
    }

    public void printEXml(){
        try {
            System.out.println("|___________________________________________________________________________________|");
            FileReader fr = new FileReader(xmlFname);
            BufferedReader br = new BufferedReader(fr);
            try {
                String ln = br.readLine();
                while (ln != null) {
                    System.out.println(ln);
                    ln = br.readLine();
                }
                br.close();
                fr.close();
            } catch ( IOException ioe) {

                System.out.println("error while reading");
                this.add(ioe);
                this.saveTxt();

            }

        } catch ( FileNotFoundException fnf) {
            this.add(fnf);
            this.saveTxt();

            System.out.println("File not found exception Saved create file");
        } catch ( Exception ee) {
            System.out.println("General Exception catched and Saved");
            this.add(ee);
            this.saveTxt();

        }
    }

}