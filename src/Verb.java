import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * Created by bachp on 3/26/2017.
 */
public class Verb implements Comparator<Verb>, Comparable<Verb>{
    final String infinitive_form;
    final String template_name;
    public Verb(String infinitive_form){
        this.infinitive_form = infinitive_form;
        this.template_name = null;
    }
    public Verb(String infinitive_form, String template_name){
        this.infinitive_form = infinitive_form;
        this.template_name = template_name;
    }
    public Verb(Verb copy){
        this.infinitive_form = copy.infinitive_form;
        this.template_name = copy.template_name;
    }
    public String getInfinitive_form(){
        return infinitive_form;
    }

    public static Verb newInstance(Verb copy){
        return new Verb(copy);
    }
    public String getTemplate_name(){
        return template_name;
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
