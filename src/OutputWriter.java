import DataStructure.Mode;
import DataStructure.Tense;
import java.util.List;

/**
 * Created by bachp on 3/26/2017.
 */
public class OutputWriter {
    private String verbToBeConjugated;
    private Mode mode;
    private Tense tense;
    private List<String> conjugatedForm;
    private static String[] pronouns = {"je", "tu", "il", "nous", "vous", "ils"};

    /**
     *
     * @param verb
     * @param mode
     * @param tense
     * @param conjugatedForm
     */
    public OutputWriter(String verb, Mode mode, Tense tense, List<String> conjugatedForm) {
        this.verbToBeConjugated = verb;
        this.mode = mode;
        this.tense = tense;
        this.conjugatedForm = conjugatedForm;
    }

    /**
     *
     * @return
     */
    public String toString() {
        StringBuilder sb = new StringBuilder(verbToBeConjugated);
        sb.append("\n"+mode.toString()+"\n"+tense.toString());
        int i = 0;
        for (String e : conjugatedForm) {
            if (!e.equals("null")) {
                sb.append(pronouns[i]).append(" ").append(e).append("\n");
            }
            i++;
        }
        return sb.toString();
    }

    public String writeToConsole(){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String e : conjugatedForm) {
            if (!e.equals("null")) {
                sb.append(pronouns[i]).append(":{").append(e).append("} ");
            }
            i++;
        }
        return sb.toString();
    }
}
