package app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date; 

public class Reflector {
    
    private String projectPath ;
    private File project;
    private File[] files ; 
    private File[] dirs;
    private File output = new File(System.getProperty("user.dir") + "/output");
    private  String results = System.getProperty("user.dir") + "/"+output.getName()+"/results.txt";

    public Reflector( String path) {
        projectPath = path;
        project = new File(projectPath);
        files = new File(project.getPath()).listFiles();
        dirs = new File(project.getPath()).listFiles(new FileFilter() {
            @Override
            public boolean accept( File file) {
                return file.isDirectory();
            }
        });
    }

    public void reflectXml() {
        ExceptionSaver es = new ExceptionSaver();
        try {
            int counter = 1;
            for (File f : files) {
                String fname = f.getName().substring(0, f.getName().lastIndexOf('.'));
                URL[] classURL = { f.toURI().toURL() };
                URLClassLoader urlClassLoader = new URLClassLoader(classURL);
                Class currentClass = urlClassLoader.loadClass("P1." + fname);

                String currentFile = System.getProperty("user.dir")+"/output/"+fname+".xml";
                FileWriter fw = new FileWriter(currentFile, false);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.newLine();
                bw.write("<class name='" + currentClass.getSimpleName() + "'>");

                bw.newLine();

                bw.write("\t<package name='" + currentClass.getPackage().getName() + "'/>");

                bw.newLine();
                bw.write("\t<attributs nbr='" + currentClass.getDeclaredFields().length + "'>");
                bw.newLine();
                for ( Field fi : currentClass.getDeclaredFields()) {
                    bw.write("\t\t<attribut name='" + fi.getName() + "'>" + fi.getType().getSimpleName()
                            + "</attrubut>");
                    bw.newLine();
                }
                bw.write("\t</attributs>\n");
                bw.write("\t<constructors nbr='" + currentClass.getDeclaredConstructors().length + "'>");
                bw.newLine();
                for ( Constructor c : currentClass.getDeclaredConstructors()) {
                    String cons = "\t\t<constructor name='" + c.getName() + "' parametres='" + c.getParameterCount()
                            + "' >\n";
                    if (c.getParameterCount() > 0) {
                        for ( Parameter p : c.getParameters()) {
                            cons += "\t\t\t<parameter name='" + p.getName() + "'>" + p.getType().getSimpleName()
                                    + "</parameter>\n";
                        }
                    }
                    cons += "\t\t</constructor>\n";
                    bw.write(cons);
                }
                bw.write("\t</constructors>");
                bw.newLine();
                // mthods
                bw.write("\t<methods nbr='" + currentClass.getDeclaredMethods().length + "' >");
                bw.newLine();
                for ( Method m : currentClass.getDeclaredMethods()) {

                    String metho = "\t\t<method name='" + m.getName() + "' parametre='" + m.getParameterCount()
                            + "' retour='" + m.getReturnType().getSimpleName() + "' " + ">";

                    for ( Parameter p : m.getParameters()) {
                        metho += "\n\t\t\t<parametre name='" + p.getName() + "' type='" + p.getType().getSimpleName()
                                + "' >" + p.getType() + "</parametre>";
                    }

                    bw.write(metho);
                    bw.newLine();
                    bw.write("\t\t</method>");
                    bw.newLine();
                }

                bw.newLine();
                bw.write("\t</methods>");
                bw.newLine();

                bw.write("</class>");
                bw.close();
                fw.close();
                counter += 1;
            }

        } catch ( FileNotFoundException fnf) {
            es.add(fnf);
            es.saveXml();

            System.out.println("File not found exception Saved create file");
        } catch ( Exception ee) {
            System.out.println("General Exception catched and saved");
            es.add(ee);
            es.saveXml();

        }
    }

    public void reflectText() {
        ExceptionSaver es = new ExceptionSaver();
        try {
            FileWriter fw = new FileWriter(results, false);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.newLine();
            bw.write("Description textuelle -- Date : " + new Date());
            bw.newLine();
            bw.write(
                    "-------------------------------------------------------------------------------------------------------------------------------------");
            bw.newLine();
            bw.write("Information projet");
            bw.newLine();
            bw.write(
                    "-------------------------------------------------------------------------------------------------------------------------------------");
            bw.newLine();
            bw.write("Nom du projet: " + project.getName());
            bw.newLine();
            bw.write("Nombre de packages: " + (1 + dirs.length));
            bw.newLine();
            bw.write("Nombre de classe: " + files.length);
            bw.newLine();
            bw.write("Java version: " + System.getProperty("java.version"));
            bw.newLine();
            bw.write("OS: " + System.getProperty("os.name"));
            bw.newLine();
            bw.write(
                    "-------------------------------------------------------------------------------------------------------------------------------------");
            bw.newLine();
            bw.write("Informations classes");
            bw.newLine();
            bw.write(
                    "-------------------------------------------------------------------------------------------------------------------------------------");
            bw.newLine();

            int j = 1;
            for ( File f : files) {

                URL[] classURL = { f.toURI().toURL() };
                URLClassLoader urlClassLoader = new URLClassLoader(classURL);
                Class currentClass = urlClassLoader.loadClass("P1." + f.getName().substring(0, f.getName().lastIndexOf('.')));

                try {
                    bw.write("Class " + j + ":");
                    bw.newLine();
                    bw.write("Nom Package: " + currentClass.getPackageName());

                    bw.newLine();
                    bw.write("Nom Class: " + currentClass.getSimpleName());
                    bw.newLine();
                    bw.write("Nombre des attributs publics: " + currentClass.getFields().length);
                    bw.newLine();
                    bw.write("Liste et types des attributs publics: [ ");
                    Field[] fields = currentClass.getFields();
                    for ( Field oneField : fields) {
                        Field field = currentClass.getField(oneField.getName());
                        bw.write(field.getName() + " - " + field.getType().getSimpleName() + ", ");
                    }
                    bw.write("]");
                    bw.newLine();
                    bw.write("Nombre des attributs privés: "
                            + (currentClass.getDeclaredFields().length - currentClass.getFields().length));
                    bw.newLine();
                    bw.write("Liste et types des attributs privés : [ ");
                    ArrayList<Field> listAtr = new ArrayList<Field>();
                    Field[] df = currentClass.getDeclaredFields();
                    Field[] types = currentClass.getFields();
                    for ( Field field : df) {
                        listAtr.add(field);
                    }
                    for ( Field t : types) {
                        listAtr.remove(t);
                    }
                    for (int k = 0; k < listAtr.size(); k++) {
                        Field privateField = listAtr.get(k);
                        bw.write(privateField.getName() + " - " + privateField.getType().getSimpleName() + ", ");
                    }
                    bw.write("]");
                    bw.newLine();
                    bw.write("Liste des constructeurs: [ ");
                    Constructor[] constructor = currentClass.getDeclaredConstructors();
                    for ( Constructor c : constructor) {
                        bw.write(c.getName() + " ");
                    }
                    bw.write("]");
                    bw.newLine();
                    Method[] declaredMethodes = currentClass.getDeclaredMethods();
                    bw.write("Nombre de méthodes: " + declaredMethodes.length);
                    bw.newLine();
                    bw.write("Liste des méthodes: [ ");
                    for ( Method meth : declaredMethodes) {
                        bw.write(meth.getName() + ", ");
                    }
                    bw.write("]");
                    bw.newLine();
                    bw.write("Type de retour des méthodes: [ ");
                    for ( Method meth : declaredMethodes) {
                        bw.write(meth.getName() + ": " + meth.getReturnType().getSimpleName() + ", ");
                    }
                    bw.write("]");
                    bw.newLine();
                    bw.write("Paramètres et types des méthodes: ");
                    for ( Method meth : declaredMethodes) {
                        bw.write(meth.getName() + "[");
                        Class[] pTypes = meth.getParameterTypes();
                        for (int k = 0; k < pTypes.length; k++) {
                            bw.write("param" + (k + 1) + ": " + pTypes[k].getSimpleName() + ", ");
                        }
                        bw.write("], ");
                    }
                    bw.newLine();
                    bw.write(
                            "-------------------------------------------------------------------------------------------------------------------------------------");
                    bw.newLine();
                } catch ( Exception e) {
                    System.out.println("Exception generer dans Class 1");
                    es.add(e);
                    es.saveXml();
                }
                j += 1;
            }
            bw.close();
            fw.close();
        } catch ( IOException ioe) {
            es.add(ioe);
            es.saveXml();
            
            

        } catch ( Exception ee) {
            System.out.println("General Exception catched and saved");
            es.add(ee);
            es.saveXml();
            
        }
    }

    public void printResults() {
        ExceptionSaver es = new ExceptionSaver();
        try{
            FileReader fr = new FileReader(results);
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
                es.add(ioe);
                es.saveXml();
                
            }

        } catch ( FileNotFoundException fnf) {
            es.add(fnf);
            es.saveXml();
            
            System.out.println("File not found exception Saved create file");
        } catch ( Exception ee) {
            System.out.println("General Exception catched and Saved");
            es.add(ee);
            es.saveXml();
            
        }
    }

    public void printXmls(){
        ExceptionSaver es = new ExceptionSaver();
        try {
            for ( String f : output.list()) {
                if(f.contains(".xml")){
                    try {
                        System.out.println("|___________________________________________________________________________________|");
                        FileReader fr = new FileReader(output.getPath()+"/"+f);
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
                            es.add(ioe);
                            es.saveXml();

                        }

                    } catch ( FileNotFoundException fnf) {
                        es.add(fnf);
                        es.saveXml();

                        System.out.println("File not found exception Saved create file");
                    } catch ( Exception ee) {
                        System.out.println("General Exception catched and Saved");
                        es.add(ee);
                        es.saveXml();

                    }
                    
                }
            }
            System.out.println("|___________________________________________________________________________________|");
        } catch ( Exception e) {
                es.add(e);
                es.saveXml();
            }
        
    }

    public String getProjectPath() {
        return this.projectPath;
    }

    public File getProject() {
        return this.project;
    }

    public File[] getFiles() {
        return this.files;
    }

    public File[] getDirs() {
        return this.dirs;
    }

    public String getResults() {
        return this.results;
    }
    public File getOutput() {
        return this.output;
    }

}