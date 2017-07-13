package app;

import structure.Mode;
import structure.Tense;
import structure.Verb;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by bachp on 3/26/2017.
 */
public class OutputWriter {
    private final String verbToBeConjugated;//String input
    private final String infVerb;//output from verb object
    private final Mode mode;//mode of conjugation forms
    private final Tense tense;//tense of conjugation forms
    private final String templateName;//output from verb object
    private final List<String> conjugatedForms;

    public static String[] pronouns = {"Je", "Tu", "Il", "Nous", "Vous", "Ils"};
    private static StringBuilder sb = new StringBuilder();
    //static string builder for concatenating radical and suffixes of conjugated forms

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
                    if(e.contains("/")){
                        List<String> temp = new ArrayList <>();
                        for(String s : e.split("/")){
                            sb.setLength(0);
                            sb.append(Verb.radical(templateName, infVerb)).append(s);
                            temp.add(sb.toString());
                        }
                        this.conjugatedForms.add(Joiner.on("/").join(temp));
                        continue;
                    }
                    sb.setLength(0);
                    sb.append(Verb.radical(templateName, infVerb)).append(e);
                    this.conjugatedForms.add(sb.toString());
                }
            }
        }
        //reset String builder
        sb.setLength(0);
        this.tense = builder.tense;
        this.mode = builder.mode;
    }

    /**
     * private method to check if a list contains "null" strings
     * @param list
     * @return
     */
    private boolean isListNull(List<String> list){
        for(String e : list){
            if(!e.equals("null")) return false;
        }
        return true;
    }
    /**
     *  toString method for OutputWriter class
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
    public String toHTMLFormat(){
        sb.append("<!DOCTYPE html><html><head><style>table, th, td {border: none;}</style></head><body>")
          .append("<h2 style=\"color:#4078c0;\">").append(infVerb).append("</h2><hr />");

        sb.append("<p>")
                .append("\n").append(mode.toString().toLowerCase())
                .append("-").append(tense.toString().toLowerCase())
                .append("<p>");

        //initialize table
        sb.append("<table style=\"width:90%\">");

        int c = 0;
        for(int i : new int[]{0,3,1,4,2,5}){
            String e = getElementHelper(this.conjugatedForms, i);
            if(!e.equals("null")) {
                if(c%2==0) sb.append("<tr>");
                sb.append("<td>")
                    .append(pronouns[i]).append(" <strong style=\"color:#4078c0;\">")
                    .append(e).append("</strong>")
                .append("</td>");
                if(c%2==1) sb.append("</tr>");
                c++;
            }
        }

        //finalize
        sb.append("</table></body></html>");
        String result = sb.toString();
        sb.setLength(0);
        return result;
    }
    private String getElementHelper(List<String> A, int i){
        return (i < A.size() && i >=0) ? A.get(i) : "null";
    }
    /**
     * getter for input verb
     * @return String
     */
    public String getVerbToBeConjugated(){
        return verbToBeConjugated;
    }

    /**
     * getter for conjugated forms(these forms are already concatenated with radical in the template name)
     * @return List<String>
     */
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

    /**
     * Builder static class for Output Writer
     */
    public static class Builder{
        //instance variables
        private final List<String> conjugatedForms;
        private final List<String>[] multipleConjugatedForms;
        private String verb;
        private String infVerb;
        private Mode mode;
        private Tense tense;
        private String templateName;

        /**
         * constructor
         * @param conjugatedForms List<String>
         */
        public Builder(List<String> conjugatedForms){
            this.conjugatedForms = conjugatedForms;
            this.multipleConjugatedForms = null;
        }

        /**
         * constructor when there are multiple conjugated forms
         * @param multipleConjugatedForms
         */
        public Builder(List<String>... multipleConjugatedForms){
            this.conjugatedForms = null;
            this.multipleConjugatedForms = multipleConjugatedForms;
        }

        /**
         * input verb string
         * @param verb String
         * @return Builder obj
         */
        public Builder verb(String verb){
            this.verb = verb;
            return this;
        }

        /**
         * output from verb object in INFINITIVE form
         * @param infVerb String
         * @return Builder
         */
        public Builder infVerb(String infVerb){
            this.infVerb = infVerb;
            return this;
        }

        /**
         * Mode input
         * @param mode Mode
         * @return Builder
         */
        public Builder mode(Mode mode){
            this.mode = mode;
            return this;
        }

        public Builder tense(Tense tense){
            if(mode == null)
                throw new IllegalArgumentException("mode enum should be put as a parameter before tense enum");
            else if(!this.mode.isTenseInMode(tense))
                throw new IllegalArgumentException("tense is not compatible with a given mode");
            this.tense = tense;
            return this;
        }

        /**
         * input template name which is taken from verb Object
         * @param templateName
         * @return
         */
        public Builder templateName(String templateName){
            this.templateName = templateName;
            return this;
        }

        /**
         * generic build method
         * @return OutputWriter obj
         */
        public OutputWriter build(){
            return new OutputWriter(this);
        }
    }
}
