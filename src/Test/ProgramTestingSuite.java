package Test;

import DataStructure.Mode;
import DataStructure.Tense;
import DataStructure.Verb;
import Main.OutputWriter;
import Main.Program;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by bachp on 6/25/2017.
 */
public class ProgramTestingSuite extends Program {
    public static void printListElementsToFile(){
        try {
            PrintWriter pw = new PrintWriter("./src/Test/list.txt", "UTF-16");
            for(int i = 0; i < Verb.getListSize(); i++){
                pw.println(Verb.getListElement(i).toString());
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public static boolean isTablesInVerbListEmpty(){
        for(int i = 0; i < Verb.getListSize(); i++){
            if(Verb.getVerbFromList(i).isTableEmpty()) return true;
        }
        return false;
    }
    public static void printTNToFile(){
        try {
            PrintWriter pw = new PrintWriter("./src/Test/multimapTN.txt", "UTF-16");
            int i = 0;
            for(String key : Verb.getMultiMapKeys()){
                pw.println(i++ +" "+key);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public static void printVerbsWithSameTemplateName(){
        try {
            PrintWriter pw = new PrintWriter("./src/Test/multimap.txt", "UTF-16");
            for(String key : Verb.getMultiMapKeys()){
                pw.println(key);
                for(Verb v : Verb.getMultiMapValuesFromKey(key)){
                    pw.println("    "+v.getInfinitiveForm());
                }
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public static void trieExhaustiveCheck(){
        int passes = Verb.getListSize();
        for(int i = 0; i < Verb.getListSize(); i++){
            if(i != 0 && i%100 == 0) System.out.println();
            if(Verb.isVerbInTrie(Verb.getListElement(i).getInfinitiveForm()))
                System.out.print(".");
            else{
                System.out.println(Verb.getListElement(i));
                passes--;
            }
        }
        System.out.println();
        System.out.printf("passes: %d, fails: %d", passes, Verb.getListSize() - passes);
    }
    public static void printConjugation(){
        System.out.println(conjugateInfinitiveVerb());
    }

}
