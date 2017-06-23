import DataStructure.Mode;
import DataStructure.Tense;
import com.google.common.base.Joiner;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;


/**
 * Created by bachp on 4/2/2017.
 */
public class SuffixesGroup implements Comparator<SuffixesGroup>, Comparable<SuffixesGroup>, Cloneable{
    private final String template_name;
    protected Table<Mode, Tense, List<String>> table;
    public SuffixesGroup(String template_name){
        this.template_name = template_name;
        table = HashBasedTable.create();
    }

    /**
     * copy constructor
     * @param copy
     */
    public SuffixesGroup(SuffixesGroup copy){
        this.template_name = copy.template_name;
        this.table = HashBasedTable.create(copy.table);
    }

    public void append(Mode mode, Tense tense, List<String> prefixes){
        table.put(mode, tense, prefixes);
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
    public List<String> getPrefixes(Mode mode, Tense tense){
        return table.get(mode, tense);
    }

    public static List<String> appendString(String radical, List<String> listOfSuffixes){
        List<String> conjugated = new ArrayList <>();
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
    public boolean containsSuffix(String suffix){
        for(Collection<String> s : table.values()){
            if(s.contains(suffix)){
                return true;
            }
        }
        return false;
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
    public List<ModeTensePair> contains(String suffix){
        List<ModeTensePair> modeTensePairs = new ArrayList<>();
        for(Mode m : Mode.values()){
            for(Tense t : m.getTenses()){
                List<String> list = table.get(m, t);
                if(list.contains(suffix)) modeTensePairs.add(new ModeTensePair(m,t));
            }
        }
        return modeTensePairs;
    }
    @Override
    public int compare(SuffixesGroup o1, SuffixesGroup o2) {
        return o1.template_name.compareTo(o2.template_name);
    }
    public Object clone() throws CloneNotSupportedException{
        SuffixesGroup temp = new SuffixesGroup(this.template_name);
        temp.table = ImmutableTable.copyOf(this.table);
        return temp;
    }
    @Override
    public int compareTo(@NotNull SuffixesGroup o) {
        return this.template_name.compareTo(o.template_name);
    }
    public String toString(){
        return template_name;
    }
    public int getLengthOfRadical(){
        return template_name.indexOf(':');
    }
}
