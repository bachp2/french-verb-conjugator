import java.util.NoSuchElementException;

/**
 * Created by bachp on 1/31/2017.
 */
public enum Mode {
    infinitive("infinitive-present"),
    indicative("present", "imperfect", "future", "simple-past"),
    conditional("present"),
    subjunctive("present", "imperfect"),
    imperative("imperative-present"),
    participle("present-participle", "past-participle");
    private String[] tenses;

    Mode(String... tenses) {
        this.tenses = tenses;
    }

    public int getTenseIndex(String t) {
        if (t.equals("present")) {
            switch (this) {
                case infinitive:
                    t = t + "-present";
                    break;
                case imperative:
                    t = "imperative-" + t;
                    break;
                case participle:
                    t = t + "-participle";
                default:
                    break;
            }
        } else if (t.equals("past")) {
            switch (this) {
                case indicative:
                    t = "simple-" + t;
                    break;
                case participle:
                    t = t + "-participle";
                    break;
                default:
                    break;
            }
        }
        int i = 0;
        for (String s : tenses) {
            if (t.equals(s))
                return i;
            i++;
        }
        throw new NoSuchElementException("tense not found");
    }

    public int length() {
        return tenses.length;
    }
}
