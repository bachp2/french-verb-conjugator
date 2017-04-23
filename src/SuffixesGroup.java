import com.google.common.base.Joiner;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;


/**
 * Created by bachp on 4/2/2017.
 */
public class SuffixesGroup implements Comparator<SuffixesGroup>, Comparable<SuffixesGroup>{
    private final String template_name;
    protected final Table<Mode, Mode.Tense, ArrayList<String>> table;
    public SuffixesGroup(String template_name){
        this.template_name = template_name;
        table = HashBasedTable.create();
    }
    public void append(Mode mode, Mode.Tense tense, ArrayList<String> prefixes){
        table.put(mode, tense, prefixes);
    }
    public boolean isEmpty(){
        return table.isEmpty();
    }
    public ArrayList<String> getPrefixes(Mode mode, Mode.Tense tense){
        return (ArrayList <String>) table.get(mode, tense);
    }
    public ArrayList<String> getPrefixesGroup(Mode mode, Mode.Tense tense){
        return (ArrayList<String>) table.get(mode, tense);
    }
    public static ArrayList<String> append(String radical, ArrayList<String> listOfPrefixes){
        // already radical
        ArrayList<String> conjugated = new ArrayList <>();
        for (String s : listOfPrefixes) {
            if(s.contains("/")){
                StringBuilder temp = new StringBuilder();
                conjugated.add(Joiner.on("/").join(s.split("/")));
            }
            else conjugated.add(radical + s);
        }
        return conjugated;
    }
    public String getTemplateName(){
        return template_name;
    }
    public boolean containsSuffix(String suffix){
        for(Collection<String> s : table.values()){
            if(s.contains(suffix)){
                return true;
            }
        }
        return false;
    }
    @Override
    public int compare(SuffixesGroup o1, SuffixesGroup o2) {
        return o1.template_name.compareTo(o2.template_name);
    }

    @Override
    public int compareTo(@NotNull SuffixesGroup o) {
        return this.template_name.compareTo(o.template_name);
    }
    public String toString(){
        Set<Mode> modes = table.rowKeySet();
        Set<Mode.Tense> tense = table.columnKeySet();
        for(Mode mode : modes){
            //todo
        }
        return null;
    }
    public int getLengthOfRadical(){
        return template_name.indexOf(':');
    }
}
