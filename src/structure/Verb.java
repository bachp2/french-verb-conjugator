package structure;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Table;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by bachp on 3/26/2017.
 */
public class Verb implements Comparator<Verb>, Comparable<Verb> {
    private final String infinitiveForm;//verb in INFINITIVE either ends in er ir or er.
    private final String templateName;//template name of verbs with common suffix. ex..aim:er->radical:suffix
    //index to keep track of list element
    private static int index = 0;
    private static final List<Verb> list = new ArrayList<>();
    //multi map from Guava library is used to match a template name with verb in verb's list
    private static final ListMultimap<String, Verb> multiMap = ArrayListMultimap.create();
    //prefix trie to match exact verb input
    private static final Trie trie = new Trie();
    //table for each verb object, contains conjugated forms of different modes and tenses
    private Table<Mode, Tense, List<String>> table;
    //static string builder to facilitate string concatenation
    private static final StringBuilder sb = new StringBuilder();
    /**
     * constructor with INFINITIVE form and template name
     * @param infinitiveForm String
     * @param templateName String
     */
    private Verb(String infinitiveForm, String templateName){
        this.infinitiveForm = infinitiveForm;
        this.templateName = templateName;
        this.table = null;
    }

    /**
     * constructor without template name
     * @param infinitiveForm String
     */
    private Verb(String infinitiveForm){
        this.infinitiveForm = infinitiveForm;
        this.templateName = null;
        this.table = null;
    }

    /**
     * get verb's list size
     * @return int
     */
    public static int getListSize(){
        return list.size();
    }

    /**
     * get list element
     * @param index::index in the list
     * @return Verb element
     */
    public static Verb getListElement(int index){
        return list.get(index);
    }

    /**
     * sort the verb's list using Collections sort function
     */
    public static void sortList(){
        Verb.list.sort(Comparator.comparing(Verb::getInfinitiveForm));
    }

    /**
     * get radical form from the verb object
     * @return String
     */
    public String radical(){
        return infinitiveForm.substring(0, getRadicalIndex()+1);
    }

    /**
     * create Verb object without return that object like an constructor
     * @param infinitive_form
     * @param template_name
     */
    public static void create(String infinitive_form, String template_name){
        list.add(new Verb(infinitive_form, template_name));
        multiMap.put(list.get(index).getTemplateName(), list.get(index));
        index++;
    }

    /**
     * parameter should be in INFINITIVE form
     * search verb in the list
     * @param v String
     * @return Verb
     */
    public static Verb searchVerbList(String v){
        int index = Collections.binarySearch(Verb.list, new Verb(v));
        if (index >= 0)
            return Verb.list.get(index);
        else return null;
    }
    @Override
    public String toString(){
        return "{"+this.infinitiveForm +"<"+this.templateName +">}";
    }

    /**
     * check if verb is in trie
     * @param s String
     * @return boolean
     */
    public static boolean isVerbInTrie(String s){
        return Verb.trie.isVerbInTrie(s);
    }

    /**
     * check for radical in trie
     * @param s String
     * @return boolean
     */
    public static boolean isRadInTrie(String s){
        return Verb.trie.isRadInTrie(s);
    }

    /**
     * get multiMap keys
     * @return Set<String>
     */
    public static Set<String> getMultiMapKeys(){
        return multiMap.keys().elementSet();
    }

    /**
     * get list of verb corresponds with template name
     * @param key String
     * @return List<Verb>
     */
    public static List<Verb> getMultiMapValuesFromKey(String key){
        return multiMap.get(key);
    }

    /**
     * private method to check if the multimap contains an template name
     * @param template_name String
     * @return boolean
     */
    private static boolean containsTemplateName(String template_name){
        return multiMap.containsKey(template_name);
    }

    /**
     * set table to a corresponding verbs Obj with similar template name
     * @param template_name String
     * @param table Table<Mode, Tense, List<String>>
     */
    public static void setTable(String template_name, Table<Mode, Tense, List<String>> table){
        if(containsTemplateName(template_name)){
            for(Verb v : multiMap.get(template_name)){
                v.table = table;
            }
        }
    }

    /**
     *
     * @param verb
     * @return
     */
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
    public static Verb[] searchVerb(String verb){
        return Verb.trie.searchVerb(verb);
    }
    public String getInfinitiveForm(){
        return infinitiveForm;
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
        return templateName;
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
        assert templateName != null;
        int index = templateName.length() - templateName.indexOf(":");
        return infinitiveForm.length() - index;
    }

    @Override
    public int compare(Verb o1, Verb o2) {
        return o1.infinitiveForm.compareTo(o2.infinitiveForm);
    }

    @Override
    public int compareTo(@NotNull Verb o) {
        return this.infinitiveForm.compareTo(o.infinitiveForm);
    }
}
