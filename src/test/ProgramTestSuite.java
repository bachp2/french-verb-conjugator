package test;

import structure.Mode;
import structure.Tense;
import structure.Verb;
import app.Program;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by bachp on 6/25/2017.
 */
public class ProgramTestSuite extends Program {
    public static void printListElementsToFile(){
        try {
            PrintWriter pw = new PrintWriter("./src/test/list.txt", "UTF-16");
            for(int i = 0; i < Verb.getListSize(); i++){
                pw.println(Verb.getListElement(i).toString());
            }
            pw.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
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
            PrintWriter pw = new PrintWriter("./src/test/multimapTN.txt", "UTF-16");
            int i = 0;
            for(String key : Verb.getMultiMapKeys()){
                pw.println(i++ +" "+key);
            }
            pw.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void printVerbsWithSameTemplateName(){
        try {
            PrintWriter pw = new PrintWriter("./src/test/multimap.txt", "UTF-16");
            for(String key : Verb.getMultiMapKeys()){
                pw.println(key);
                for(Verb v : Verb.getMultiMapValuesFromKey(key)){
                    pw.println("    "+v.getInfinitiveForm());
                }
            }
            pw.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
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

    public static void trieExhaustiveCheckRadical(){
        int passes = Verb.getListSize();
        for(int i = 0; i < Verb.getListSize(); i++){
            if(i != 0 && i%100 == 0) System.out.println();
            if(Verb.isRadInTrie(Verb.getListElement(i).radical()))
                System.out.print(".");
            else{
                System.out.println(Verb.getListElement(i));
                passes--;
            }
        }
        System.out.println();
        System.out.printf("passes: %d, fails: %d", passes, Verb.getListSize() - passes);
    }

    public static void printRandomConjugation(){
        int count = rand.nextInt(100);
        try {
            PrintWriter pw = new PrintWriter("./src/test/conjugation.txt", "UTF-16");
            while(count>0){
                pw.println(conjugateInfinitiveVerb());
                pw.println();
                count--;
            }
            pw.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public static void testConjugation(){
//        try {
//            int passes = 0;
//            PrintWriter pw = new PrintWriter("./src/test/testDecon.txt", "UTF-16");
//            for (int i = 0; i < Verb.getListSize(); i++) {
//                Verb v = Verb.getListElement(i);
//                String c = getRandomConjugatedVerb(v);
//                Verb[] vv = Verb.searchVerb(c);
//                pw.println(v+":"+c+">"+vv);
//                System.out.println(v+":"+c+">"+vv);
//                for(Verb verb : vv){
//                    if(v == verb) passes++;
//                }
//            }
//            System.out.print(passes);
//            pw.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        int count = 8000;
        int i = 0;
        while(i < count){
            Verb verbFromList = getRandomVerb();
            String conjugatedVerb = getRandomConjugatedVerb(verbFromList);
            System.out.println(verbFromList+" "+conjugatedVerb);
            for(Verb v : Verb.searchVerb(conjugatedVerb)){
                System.out.print(v+" ");
            }
            System.out.println();
            System.out.print(i++);
        }
    }
    public static void printRadicalsFromList(){
        try {
            PrintWriter pw = new PrintWriter("./src/test/listRadicals.txt", "UTF-16");
            for(int i = 0; i < Verb.getListSize(); i++){
                pw.println(Verb.getListElement(i).toString());
                pw.println(Verb.getListElement(i).radical());
            }
            pw.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void printRadicalsLength(){
        try {
            PrintWriter pw = new PrintWriter("./src/test/listRadicalsLength.txt", "UTF-16");
            for(int i = 0; i < Verb.getListSize(); i++){
                if(Verb.getListElement(i).radical().length() == 0){
                    pw.println(Verb.getListElement(i).toString());
                    pw.println(Verb.getListElement(i).radical().length());
                }
            }
            pw.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * return a random verb from verbsGroup
     *
     * @return String
     */

    public static Verb getRandomVerb() {
        int index = rand.nextInt(Verb.getListSize());
        return Verb.getListElement(index);
    }

    /**
     *
     * @return
     */

    public static Mode getRandomMode() {
        int index = rand.nextInt(Mode.values().length);
        return Mode.values()[index];//performance insensitive
    }

    /**
     *
     * @return
     */

    public static Tense getRandomTenseFromMode(Mode mode) {
        int index = rand.nextInt(mode.getTenses().length);
        return mode.getTenses()[index];//performance insensitive
    }
    /**
     *
     * @return
     */
    private static String getRandomConjugatedVerb(Verb v) {
        Mode m = getRandomMode();
        List <String> s = v.getSuffixes(m, getRandomTenseFromMode(m));
        String temp = s.get(rand.nextInt(s.size()));
        while(temp.equals("null")) temp = s.get(rand.nextInt(s.size()));
        if(temp.contains("/")){
            String[] tmp = temp.split("/");
            return Verb.appendString(v.radical(), tmp[rand.nextInt(tmp.length)]);
        }
        return Verb.appendString(v.radical(), temp);
    }
}
