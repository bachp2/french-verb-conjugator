package Main;

import DataStructure.Mode;
import DataStructure.Tense;
import DataStructure.Verb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bachp on 3/26/2017.
 */
public class OutputWriter{
    private final String verbToBeConjugated;
    private final String infVerb;
    private final Mode mode;
    private final Tense tense;
    private final String templateName;
    private final List<String> conjugatedForms;

    public static String[] pronouns = {"je", "tu", "il", "nous", "vous", "ils"};
    private static StringBuilder sb = new StringBuilder();

    //todo create a builder for OUtputwriter
    public OutputWriter(Builder builder) {
        this.verbToBeConjugated = builder.verb;
        this.infVerb = builder.infVerb;
        this.templateName = builder.templateName;
        if(isListNull(builder.conjugatedForms))
            this.conjugatedForms = Collections.emptyList();
        else {
            this.conjugatedForms = new ArrayList <>();
            for(String e : builder.conjugatedForms){
                if(!e.equals("null")){
                    sb.setLength(0);
                    sb.append(Verb.radical(templateName, infVerb)).append(e);
                    this.conjugatedForms.add(sb.toString());
                }
            }
        }
        this.tense = builder.tense;
        this.mode = builder.mode;
    }
    private boolean isListNull(List<String> list){
        for(String e : list){
            if(!e.equals("null")) return false;
        }
        return true;
    }
    /**
     *
     * @return
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        sb.append(verbToBeConjugated).append("<").append(infVerb).append(",").append(templateName).append(">\n");
        sb.append("<").append(mode).append(",").append(tense).append(">\n");
        for (String e : this.conjugatedForms) {
            if (!e.equals("null")) {
                sb
                  .append(pronouns[i])
                  .append(":{")
                  .append(e)
                  .append("} ");
            }
            i++;
        }
        return sb.toString();
    }

    public String getVerbToBeConjugated(){
        return verbToBeConjugated;
    }
    public List<String> getConjugatedForms(){
        return copyList(conjugatedForms);
    }
    private <E> List<E> copyList(List<E> list){
        List<E> newList = new ArrayList<>();
        for(E e : list){
            newList.add(e);
        }
        return newList;
    }
    public static class Builder{
        private final List<String> conjugatedForms;
        private String verb;
        private String infVerb;
        private Mode mode;
        private Tense tense;
        private String templateName;
        public Builder(List<String> conjugatedForms){
            this.conjugatedForms = conjugatedForms;
        }
        public Builder verb(String verb){
            this.verb = verb;
            return this;
        }
        public Builder infVerb(String infVerb){
            this.infVerb = infVerb;
            return this;
        }
        public Builder mode(Mode mode){
            this.mode = mode;
            return this;
        }
        public Builder tense(Tense tense){
            this.tense =tense;
            return this;
        }
        public Builder templateName(String templateName){
            this.templateName = templateName;
            return this;
        }
        public OutputWriter build(){
            return new OutputWriter(this);
        }
    }
}
