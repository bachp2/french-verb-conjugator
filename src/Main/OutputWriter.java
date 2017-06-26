package Main;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bachp on 3/26/2017.
 */
public class OutputWriter implements Cloneable{
    private String verbToBeConjugated;
    private String templateName;
    private List<String> conjugatedForms;
    public static String[] pronouns = {"je", "tu", "il", "nous", "vous", "ils"};
    private static StringBuilder sb = new StringBuilder();
    /**
     *
     * @param verb String
     * @param conjugatedForms List<String>
     */
    public OutputWriter(String verb, String templateName, List<String> conjugatedForms) {
        this.verbToBeConjugated = verb;
        this.templateName = templateName;
        this.conjugatedForms = copyList(conjugatedForms);
    }

    /**
     *
     * @return
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String e : conjugatedForms) {
            if (!e.equals("null")) {
                sb
                  .append(pronouns[i])
                  .append(":{")
                  .append(verbToBeConjugated.substring(0, templateName.indexOf(":")))
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
}
