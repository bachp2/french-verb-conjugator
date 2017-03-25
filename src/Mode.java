import com.beust.jcommander.IStringConverter;

import java.util.NoSuchElementException;

/**
 * Created by bachp on 3/25/2017.
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

    /**
     * get index of a given mode
     *
     * @param mode
     * @return
     */
    public static Mode getMode(String mode) {
        try {
            for (Mode m : Mode.values()) {
                if (mode.equals(m.toString()))
                    return m;
            }
            throw new NoSuchElementException();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static boolean isMode(String mode){
        for (Mode m : Mode.values()) {
            if (mode.equals(m.toString()))
                return true;
        }
        return false;
    }
    public boolean isTense(String tense){
        if (tense.equals("present")) {
            switch (this) {
                case infinitive:
                    tense = tense + "-present";
                    break;
                case imperative:
                    tense = "imperative-" + tense;
                    break;
                case participle:
                    tense = tense + "-participle";
                default:
                    break;
            }
        } else if (tense.equals("past")) {
            switch (this) {
                case indicative:
                    tense = "simple-" + tense;
                    break;
                case participle:
                    tense = tense + "-participle";
                    break;
                default:
                    break;
            }
        }
        for(String t : tenses){
            if(t.equals(tense)){
                return true;
            }
        }
        return false;
    }
    public String getTense(String tense) {
        if (tense.equals("present")) {
            switch (this) {
                case infinitive:
                    tense = tense + "-present";
                    break;
                case imperative:
                    tense = "imperative-" + tense;
                    break;
                case participle:
                    tense = tense + "-participle";
                default:
                    break;
            }
        } else if (tense.equals("past")) {
            switch (this) {
                case indicative:
                    tense = "simple-" + tense;
                    break;
                case participle:
                    tense = tense + "-participle";
                    break;
                default:
                    break;
            }
        }
        for(String t : tenses){
            if(t.equals(tense))
                return t;
        }
        return null;
    }

    public int length() {
        return tenses.length;
    }

    /**
     * Created by bachp on 3/25/2017.
     */
    public static class ModeConverter implements IStringConverter<Mode> {
        @Override
        public Mode convert(String s) {
            return getMode(s);
        }
    }
}
