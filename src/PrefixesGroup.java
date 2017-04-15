import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;


/**
 * Created by bachp on 4/2/2017.
 */
public class PrefixesGroup implements Comparator<PrefixesGroup>, Comparable<PrefixesGroup>{
    public final String template_name;
    protected Table<Mode, Mode.Tense, ArrayList<String>> table;
    public PrefixesGroup(String template_name){
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
        return table.get(mode, tense);
    }
    public ArrayList<String> getPrefixesGroup(Mode mode, Mode.Tense tense){
        return (ArrayList<String>) table.get(mode, tense).clone();
    }
    @Override
    public int compare(PrefixesGroup o1, PrefixesGroup o2) {
        return o1.template_name.compareTo(o2.template_name);
    }

    @Override
    public int compareTo(@NotNull PrefixesGroup o) {
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
}
