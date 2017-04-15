/**
 * Created by bachp on 3/26/2017.
 */
public class OuputConsole {
    final String verbToBeConjugated;
    final Mode mode;
    final Mode.Tense tense;
    final String[][] conjugatedForm;
    final String[] pronouns = {"je", "tu", "il", "nous", "vous", "ils"};

    public OuputConsole(String verb, Mode mode, Mode.Tense tense, String[][] conjugatedForm) {
        this.verbToBeConjugated = verb;
        this.mode = mode;
        this.tense = tense;
        this.conjugatedForm = conjugatedForm;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(verbToBeConjugated);
        sb.append("%n"+mode.toString());
        sb.append("%n"+tense.toString());
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
