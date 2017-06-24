import DataStructure.Mode;
import DataStructure.Tense;
import DataStructure.Trie;
import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Table;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by bachp on 3/26/2017.
 */
public class Verb implements Comparator<Verb>, Comparable<Verb> {
    private final String infinitive_form;
    private final String template_name;
    private static int index = 0;
    //todo: refactor verb collection to static variable of verb class
    public static List<Verb> list = new ArrayList<>();
    private static ListMultimap<String, Verb> multiMap = ArrayListMultimap.create();
    public static Trie trie = new Trie();
    protected Table<Mode, Tense, List<String>> table;


    private Verb(){
        this.infinitive_form = null;
        this.template_name = null;
        this.table = null;
    }
    private Verb(String infinitive_form, String template_name){
        this.infinitive_form = infinitive_form;
        this.template_name = template_name;
        this.table = null;
    }
    private Verb(String infinitive_form){
        this.infinitive_form = infinitive_form;
        this.template_name = null;
        this.table = null;
    }
    public boolean matchesRadical(String radical){
        return this.radical().equals(radical);
    }
    public static void create(String infinitive_form, String template_name){
        list.add(new Verb(infinitive_form, template_name));
        multiMap.put(list.get(index).getInfinitiveForm(), list.get(index));
        index++;
    }

    /**
     * parameter should be in infinitive form
     * @param v
     * @return
     */
    public static Verb searchVerb(String v){
        int index = Collections.binarySearch(Verb.list, new Verb(v));
        if (index >= 0)
            return Verb.list.get(index);
        else return null;
    }
    /**
     * sear
     *
     * @param verb
     * @return
     */
    public static List<Verb> matchesWithVerbs(String verb) {
        //searchVerb for radical that matchRadical the conjugated verb
        List<Verb> temp = new ArrayList <>();
        for(String v : Verb.trie.search(verb)){
            temp.add(searchVerb(v));
        }
        return temp;
    }
    public static boolean containsTemplateName(String template_name){
        return multiMap.containsKey(template_name);
    }
    public static void setTable(String template_name, Table<Mode, Tense, List<String>> table){
        if(containsTemplateName(template_name)){
            for(Verb v : multiMap.get(template_name)){
                if(v.table != null) break;
                v.table = table;
            }
        }
    }
    public boolean containsConjugatedSuffix(String suffix){
        return this.table.containsValue(suffix);
    }
    public static void setTrie(){
        for(Verb v : list){
            trie.insert(v.infinitive_form, v.template_name.indexOf(":") - 1);
        }
    }
    public static List<Verb> list(){
        return list;
    }
    public String getInfinitiveForm(){
        return infinitive_form;
    }

    /**
     * check if the table is empty
     * @return boolean
     */
    public boolean isEmpty(){
        return table.isEmpty();
    }

    /**
     * get prefixes of conjugation of a verb
     * @param mode Mode
     * @param tense Tense
     * @return ArrayList<String>
     */
    public List<String> getSuffixes(Mode mode, Tense tense){
        return table.get(mode, tense);
    }
    public static List<String> appendString(String radical, List<String> listOfSuffixes){
        List<String> conjugated = new ArrayList<>();
        for (String s : listOfSuffixes) {
            if(s.contains("/")){
                StringBuilder temp = new StringBuilder();
                conjugated.add(Joiner.on("/").join(joiner(radical, s.split("/"))));
            }
            else conjugated.add(radical + s);
        }
        return conjugated;
    }
    private static String[] joiner(String rad, String[] temp){
        String[] as = new String[temp.length];
        for(int i = 0; i < temp.length; i++){
            as[i] = rad + temp[i];
        }
        return as;
    }

    public static String appendString(String radical, String prefix){
        return radical+prefix;
    }
    public String getTemplateName(){
        return template_name;
    }
    class ModeTensePair{
        private Mode mode;
        private Tense tense;
        public ModeTensePair(Mode mode, Tense tense){
            this.mode = mode;
            this.tense = tense;
        }
        public Mode getMode(){
            return mode;
        }
        public Tense getTense(){
            return tense;
        }
    }
    private List<Verb.ModeTensePair> contains(String suffix){
        List<Verb.ModeTensePair> modeTensePairs = new ArrayList<>();
        for(Mode m : Mode.values()){
            for(Tense t : m.getTenses()){
                List<String> list = table.get(m, t);
                if(list.contains(suffix)) modeTensePairs.add(new Verb.ModeTensePair(m,t));
            }
        }
        return modeTensePairs;
    }


    public int getLengthOfRadical(){
        return template_name.indexOf(':');
    }

    /**
     * helper method to radical a verb into remaining radical (discard prefix)
     *
     * @param temp::String
     * @param v::String
     * @return
     */
    public static String radical(String temp, String v) {
        int index = 0;
        for (int i = 0; i < temp.length(); i++) {
            char c = temp.charAt(i);
            if (c == ':') {
                index = temp.length() - 1 - i;
            }
        }
        return v.substring(0, v.length() - index);
    }
    public String radical(){
        int index = 0;
        for (int i = 0; i < template_name.length(); i++) {
            char c = template_name.charAt(i);
            if (c == ':') {
                index = template_name.length() - 1 - i;
            }
        }
        return infinitive_form.substring(0, infinitive_form.length() - index);
    }
    public static String suffix(String verb, String template_name){
        int start = template_name.indexOf(":");
        return verb.substring(start, verb.length());
    }
    @Override
    public int compare(Verb o1, Verb o2) {
        return o1.infinitive_form.compareTo(o2.infinitive_form);
    }

    @Override
    public int compareTo(@NotNull Verb o) {
        return this.infinitive_form.compareTo(o.infinitive_form);
    }
    public String toString(){
        return String.format("infinitive form: %s, template name: %s", infinitive_form, template_name);
    }
    public boolean isRadical(String rad){
        return radical(template_name, infinitive_form).equals(rad);
    }
}
