package Test;

import DataStructure.Mode;
import DataStructure.Tense;
import DataStructure.Trie;
import DataStructure.Verb;
import Main.Program;
import org.junit.Assert;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by bachp on 6/25/2017.
 */
public class ProgramTestSuite extends Program {
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
            PrintWriter pw = new PrintWriter("./src/Test/conjugation.txt", "UTF-16");
            while(count>0){
                pw.println(conjugateInfinitiveVerb());
                pw.println();
                count--;
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void printRadicalsFromList(){
        try {
            PrintWriter pw = new PrintWriter("./src/Test/listRadicals.txt", "UTF-16");
            for(int i = 0; i < Verb.getListSize(); i++){
                pw.println(Verb.getListElement(i).toString());
                pw.println(Verb.getListElement(i).radical());
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public static void exhaustiveDeconjugation(){
        try {
            PrintWriter pw = new PrintWriter("./src/Test/Deconjugation.txt", "UTF-16");
            for(int i = 0; i < Verb.getListSize(); i++){
                Verb verbObj = Verb.getListElement(i);
                testDeconjugation2(verbObj.getInfinitiveForm(), pw);
                //pw.println(Verb.getListElement(i).toString());
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void testDeconjugation2(String s, PrintWriter pw){
        Verb compare = Verb.searchVerbList(s);
        String temp = ProgramTestSuite.getRandomConjugatedVerb(compare);
        //String temp = "avoir";
        pw.println(temp+" "+compare);
        pw.println(Verb.matchesWithVerbs(temp));
    }
    public static void printRadicalsLength(){
        try {
            PrintWriter pw = new PrintWriter("./src/Test/listRadicalsLength.txt", "UTF-16");
            for(int i = 0; i < Verb.getListSize(); i++){
                if(Verb.getListElement(i).radical().length() == 0){
                    pw.println(Verb.getListElement(i).toString());
                    pw.println(Verb.getListElement(i).radical().length());
                }
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void printDeconjugation(){
        String verb = getRandomConjugatedVerb();
        Verb v = deconjugate(verb);
        System.out.print(verb+" ");
        System.out.println(v.getInfinitiveForm());
    }

    public static void testDeconjugation(){
        Trie newTrie = new Trie();
        List<Verb> l = new ArrayList <>();
        int i = 0;
        while(i < 20){
            Verb v = getRandomVerb();
            newTrie.insert(v, v.getInfinitiveForm(), v.radical().length() - 1);
            l.add(v);
            i++;
        }
        Verb t = l.get(rand.nextInt(l.size()));
        String test = getRandomConjugatedVerb(t);
        System.out.println(test);
        System.out.println(Verb.matchesWithVerbs(test));
    }

    public static Verb testDeconjugation(String conjugatedVerb){
        Verb v = deconjugate(conjugatedVerb);
        return v;
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

    public static String getRandomVerbString(){
        int index = rand.nextInt(Verb.getListSize());
        return Verb.getListElement(index).getInfinitiveForm();
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
    public static String getRandomConjugatedVerb(Verb v) {
        Mode m = getRandomMode();
        List <String> s = v.getSuffixes(m, getRandomTenseFromMode(m));
        return Verb.appendString(v.radical(), s.get(rand.nextInt(s.size())));
    }
    public static String getRandomConjugatedVerb() {
        Verb v = getRandomVerb();
        Mode m = getRandomMode();
        List <String> s = v.getSuffixes(m, getRandomTenseFromMode(m));
        return Verb.appendString(v.radical(), s.get(rand.nextInt(s.size())));
    }
}
