import DataStructure.Mode;
import DataStructure.Tense;

/**
 * Created by bachp on 3/26/2017.
 */
public class ConsoleOutput  {
    private String verbToBeConjugated;
    private Mode mode;
    private Tense tense;
    private String[][] conjugatedForm;
    private String[] pronouns = {"je", "tu", "il", "nous", "vous", "ils"};
    private ConsoleOutput instance;

    public ConsoleOutput(String verb, Mode mode, Tense tense, String[][] conjugatedForm) {
        this.verbToBeConjugated = verb;
        this.mode = mode;
        this.tense = tense;
        this.conjugatedForm = conjugatedForm;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(verbToBeConjugated);
        sb.append("\n"+mode.toString()+"\n"+tense.toString());
        int index = 0;
        for (String[] e : conjugatedForm) {
            if (e != null) {
                for (String v : e) {
                    sb.append(pronouns[index] + v + "%n");
                }
            }
            index++;
        }
        return sb.toString();
    }
}
