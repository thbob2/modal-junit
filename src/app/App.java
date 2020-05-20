package app;

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

import java.util.List;
import java.util.Scanner;

import P1.*;

public class App {
    public static void menu(){
        System.out.println("|***********************************MENU************************************|");
        System.out.println("|_____________________________Choose number_________________________________|");
        System.out.println("| (1) Generate project summary  text file                                   |");
        System.out.println("| (2) Generate Respective Xml files for each project class                  |");
        System.out.println("| (3) Generate Test Class                                                   |");
        System.out.println("| (4) show Prject summary text File                                         |");
        System.out.println("| (5) show Xml project Files                                                |");
        System.out.println("|                                (0) Exit                                   |");
        System.out.println("|______________________________Developer Options____________________________|");
        System.out.println("| (9) Print Exceptions log                                                  |");
    }
    public static void main(String[] args) throws Exception {
        ExceptionSaver es = new ExceptionSaver();
        Reflector ref = new Reflector(System.getProperty("user.dir")+"/bin/P1");

        Scanner sc = new Scanner(System.in);
        Boolean ok = true;

        while(ok){
            menu();
            switch(sc.nextInt()){
                case 0: {ok = false; break;}
                case 1: {
                    System.out.println("|_____________________(1) Generation du fichier_____________________________________|"); 
                    ref.reflectText();
                    System.out.println("|Fichier Results Generer Verifier les doissier output dans votre repertoire java    |");
                    break;}
                case 2: {
                    System.out.println("|_____________________(2) Generation du fichier_____________________________________|"); 
                    ref.reflectXml();
                    System.out.println("|Fichiers Xml Generer Verifier le doissier output dans votre repertoire java        |");
                    
                    break;}
                case 3: {
                        try {
                            File classFile = new File(System.getProperty("user.dir")+"/bin/P1/Calcule.class");
                        URL[] url = {ref.getProject().toURI().toURL()};
                        URLClassLoader ucl = new URLClassLoader(url);
                        Class currentClass = ucl.loadClass("P1."+classFile.getName().substring(0,classFile.getName().lastIndexOf(".")));
                        try {
                            File currentFile = new File(ref.getOutput().getAbsolutePath()+"/test/"+currentClass.getName()+"Test.java");
                            FileWriter fw = new FileWriter(currentFile,false);
                            BufferedWriter bw = new BufferedWriter(fw);

                            bw.write("import static org.junit.Assert.assertEquals;");
                            bw.newLine();
                            bw.write("import org.junit.jupiter.api.BeforeEach;");
                            bw.newLine();
                            bw.write("import org.junit.jupiter.api.Test;");
                            bw.newLine();
                            bw.write("import java.util.Random;");
                            bw.newLine();
                            bw.write("import java.util.Scanner;");
                            bw.newLine();
                            bw.write("import " + currentClass.getName() + ";");
                            bw.newLine();
                            bw.write("public class " + currentClass.getSimpleName() + "Test {");
                            bw.newLine();
                            bw.write(currentClass.getSimpleName() +" "+ currentClass.getSimpleName().toLowerCase()+" = new " + currentClass.getSimpleName() + "();");
                            bw.newLine();
                            
                            bw.write("Random rand = new Random();");
                            bw.newLine();
                            bw.write("int upperbound = 21;");
                            bw.newLine();
                            bw.write("@BeforeEach");
                            bw.newLine();
                            bw.write("public void setUp() throws Exception {");
                            bw.newLine();
                            bw.write("System.out.println(\"What are your expectations: \");");
                            bw.newLine();
                            bw.write("}");
                            bw.newLine();

                            Method[] declaredMethodes = currentClass.getDeclaredMethods();
                            int count=1;
					
                            for (Method method : declaredMethodes) {
                                int pCounter = method.getParameterCount();
                                if (pCounter == 0) {
                                    bw.write("@Test");
                                    bw.newLine();
                                    bw.write("void Test" + method.getName() + "() {");
                                    bw.newLine();
                                    bw.write("System.out.println(\""+method.getName() + "():\");");
                                    bw.newLine();
                                    bw.write("Scanner object"+ count +" = new Scanner(System.in);");
                                    bw.newLine();
                                    if (method.getReturnType().getSimpleName().equalsIgnoreCase("int")) {
                                        bw.write("int res"+count+" = object"+ count +".nextInt();");
                                        bw.newLine();
                                    }
                                    if (method.getReturnType().getSimpleName().equalsIgnoreCase("string")) {
                                        bw.write("String res"+count+" = object"+ count +".nextLine();");
                                        bw.newLine();
                                    }
                                    bw.write("assertEquals(res"+count+ ","+currentClass.getSimpleName().toLowerCase()+"." + method.getName()+"());");
                                    count++;
                                    bw.newLine();
                                    bw.write("}");
                                    bw.newLine();

                                }else {
                                    Class[] parameterTypes = method.getParameterTypes();
                                    for (Class pt: parameterTypes) {
                                        int count2 = 1;
                                        if (pt.getSimpleName().equalsIgnoreCase("String") == false) {
                                            bw.write(pt.getSimpleName() + " rand" + (count2) + (count) + " = rand.nextInt(upperbound);");
                                            bw.newLine();
                                        }
                                        if (pt.getSimpleName().equalsIgnoreCase("String")) {
                                            bw.write(pt.getSimpleName() + " rand" + (count2) + (count) + " = \"BBCCAA\";");
                                            bw.newLine();
                                        }
                                        count2++;
                                    }
                                    bw.write("@Test");
                                    bw.newLine();
                                    bw.write("void Test" + method.getName() + "() {");
                                    bw.newLine();
                                    bw.write("System.out.println(\""+method.getName() + "(\"");
                                    for (int k = 0; k < pCounter; k++) {
                                        if (k == (pCounter - 1)) {
                                            bw.write("+ rand"+ (k+1) + (count) +" + ");
                                        }
                                        else {
                                            bw.write("+ rand"+ (k+1) + (count) +" + \",\"");
                                        }
                                    }
                                    bw.write("\")\");");
                                    bw.newLine();
                                    bw.write("Scanner object"+ count +" = new Scanner(System.in);");
                                    bw.newLine();
                                    if (method.getReturnType().getSimpleName().equalsIgnoreCase("int")) {
                                        bw.write("int result"+count+" = object"+ count +".nextInt();");
                                        bw.newLine();
                                    }
                                    if (method.getReturnType().getSimpleName().equalsIgnoreCase("string")) {
                                        bw.write("String result"+count+" = object"+ count +".nextLine();");
                                        bw.newLine();
                                    }
                                    bw.write("assertEquals(result"+count+ ","+currentClass.getSimpleName().toLowerCase()+"." + method.getName()+"(");
                                    for (int k = 0; k < pCounter; k++) {
                                        if (k == (pCounter -1) ) {
                                            bw.write("rand"+ (k+1) + (count));
                                        }else {
                                            bw.write("rand"+ (k+1) + (count) + ",");
                                        }	
                                    }
                                    bw.write("));");
                                    count++;
                                    bw.newLine();
                                    bw.write("}");
                                    bw.newLine();
                                }	
                            }
                            bw.newLine();
                            bw.write("}");
                            bw.close() ;
                            fw.close();
                            // exception généré 
                            System.out.println(5/0);
                        } catch ( IOException ioe) {
                            es.add(ioe);
                            es.saveXml();
                            
                            
                
                        } catch ( Exception ee) {
                            System.out.println("General Exception catched and saved");
                            es.add(ee);
                            es.saveXml();
                            
                        
                        }
                    }catch (Exception e) {
                            es.add(e);
                            es.saveXml();
                        }
                        
                        
                    break;}
                case 4: {
                    System.out.println("|_____________________(4) Affichage du fichier Results______________________________|"); 
                    ref.printResults();
                    System.out.println("|___________________________________________________________________________________|");
                    break;}
                case 5: {
                    System.out.println("|_____________________(5) Affichage des fichiers Xml________________________________|"); 
                    ref.printXmls();
                    System.out.println("|___________________________________________________________________________________|");
                    break;}
                case 9: {
                    System.out.println("|***********************************DEVMODE*****************************************|"); 
                    es.printEXml();
                    System.out.println("|***********************************************************************************|");
                    break;}
            }
        }
        
    

    System.out.println("done....");
    }
}