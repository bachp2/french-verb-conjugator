package DataStructure;

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
    private static final List<Verb> list = new ArrayList<>();
    private static final ListMultimap<String, Verb> multiMap = ArrayListMultimap.create();
    private static final Trie trie = new Trie();
    private Table<Mode, Tense, List<String>> table;
    private static final StringBuilder sb = new StringBuilder();

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

    public static int getListSize(){
        return list.size();
    }
    public static Verb getListElement(int index){
        return list.get(index);
    }
    public static void sortList(){
        Collections.sort(Verb.list, (o1, o2) -> o1.getInfinitiveForm().compareTo(o2.getInfinitiveForm()));
    }
    public String radical(){
        return infinitive_form.substring(0, getRadicalIndex()+1);
    }
    public static void create(String infinitive_form, String template_name){
        list.add(new Verb(infinitive_form, template_name));
        multiMap.put(list.get(index).getTemplateName(), list.get(index));
        index++;
    }

    /**
     * parameter should be in infinitive form
     * @param v
     * @return
     */
    public static Verb searchVerbList(String v){
        int index = Collections.binarySearch(Verb.list, new Verb(v));
        if (index >= 0)
            return Verb.list.get(index);
        else return null;
    }
    public String toString(){
        return "{"+this.infinitive_form+"<"+this.template_name+">}";
    }
    public static boolean isVerbInTrie(String s){
        return Verb.trie.isVerbInTrie(s);
    }
    public static boolean isRadInTrie(String s){
        return Verb.trie.isRadInTrie(s);
    }
    /**
     * sear
     *
     * @param verb
     * @return
     */
    public static List<Verb> matchesWithVerbs(String verb) {
        //searchVerbList for radical that matchRadical the conjugated verb
        List<Verb> temp =  Verb.trie.matchesWithInfVerbsInTrie(verb);
        //for testing
        //System.out.println(temp);
        return temp;
    }
    public static Set<String> getMultiMapKeys(){
        return multiMap.keys().elementSet();
    }
    public static List<Verb> getMultiMapValuesFromKey(String key){
        return multiMap.get(key);
    }
    private static boolean containsTemplateName(String template_name){
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
    public boolean containsConjugatedForm(String verb){
        for(Mode m : Mode.values()){
            for(Tense t : m.getTenses()){
                List<String> tempList = helperConjugatedForm(table.get(m, t), this.radical());
                if(tempList.contains(verb)) return true;
            }
        }
        return false;
    }
    private List<String> helperConjugatedForm(List<String> list, String radical){
        List<String> temp = new ArrayList <>();
        for(String s : list){
            if(s.equals("null")) continue;
            sb.append(radical).append(s);
            temp.add(sb.toString());
            sb.setLength(0);
        }
        return temp;
    }
    public static void setTrie(){
        for(Verb v : list){
            trie.insert(v ,v.table.values(), v.getRadicalIndex());
        }
    }
    public String getInfinitiveForm(){
        return infinitive_form;
    }
    public static Verb getVerbFromList(int index){
        return list.get(index);
    }
    /**
     * check if the table is empty
     * @return boolean
     */
    public boolean isTableEmpty(){
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

    public static String appendString(String radical, String prefix){
        return radical+prefix;
    }

    public String getTemplateName(){
        return template_name;
    }

    /**
     * helper method to radical a verb into remaining radical (discard prefix)
     * verb is not conjugated
     * @param temp::String
     * @param v::String
     * @return
     */
    public static String radical(String temp, String v) {
        int index = temp.length() - temp.indexOf(":") - 1;
        return v.substring(0, v.length() - index);
    }
    private int getRadicalIndex(){
        int index = template_name.length() - template_name.indexOf(":");
        return infinitive_form.length() - index;
    }

    @Override
    public int compare(Verb o1, Verb o2) {
        return o1.infinitive_form.compareTo(o2.infinitive_form);
    }

    @Override
    public int compareTo(@NotNull Verb o) {
        return this.infinitive_form.compareTo(o.infinitive_form);
    }
}
