package structure;

/**
 * Created by bachp on 4/22/2017.
 */
public enum Tense {
    PRESENT("INFINITIVE-PRESENT", "IMPERATIVE-PRESENT", "PRESENT-PARTICIPLE"),
    IMPERFECT,
    FUTURE,
    PAST("SIMPLE-PAST", "PAST-PARTICIPLE");
    final String[] tenses;
    Tense(String... tenses){
        this.tenses = tenses;
    }

    public static Tense toTense(String s) {
        try {
            for (Tense t : Tense.values()) {
                if (s.contains(t.toString())) {
                    return t;
                }
            }
            throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toString(Mode m) {
        if (this == Tense.PRESENT) {
            switch (m) {
                case INFINITIVE:
                    return this.tenses[0];
                case IMPERATIVE:
                    return this.tenses[1];
                case PARTICIPLE:
                    return this.tenses[2];
                default:
                    break;
            }
        } else if (this == Tense.PAST) {
            switch (m) {
                case INDICATIVE:
                    return this.tenses[0];
                case PARTICIPLE:
                    return this.tenses[1];
                default:
                    break;
            }
        }
        return this.toString();
    }
}