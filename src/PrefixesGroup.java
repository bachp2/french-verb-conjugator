import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by bachp on 4/2/2017.
 */
public class PrefixesGroup implements Comparator<PrefixesGroup>, Comparable<PrefixesGroup>{
    private String template_name;
    protected Table<Mode, Mode.Tense, ArrayList<String>> table;
    public PrefixesGroup(String template_name){
        this.template_name = template_name;
        Table<Mode, Mode.Tense, ArrayList<String>> table = HashBasedTable.create();
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

    @Override
    public int compare(PrefixesGroup o1, PrefixesGroup o2) {
        return o1.template_name.compareTo(o2.template_name);
    }

    @Override
    public int compareTo(@NotNull PrefixesGroup o) {
        return this.template_name.compareTo(o.template_name);
    }
    public String toString(){
        
    }
}
