package DataStructure;

import com.beust.jcommander.IStringConverter;

import java.util.NoSuchElementException;

/**
 * Created by bachp on 3/25/2017.
 */
public enum Mode {
    INFINITIVE(Tense.PRESENT),
    INDICATIVE(Tense.PRESENT, Tense.IMPERFECT, Tense.FUTURE, Tense.PAST),
    CONDITIONAL(Tense.PRESENT),
    SUBJUNCTIVE(Tense.PRESENT, Tense.IMPERFECT),
    IMPERATIVE(Tense.PRESENT),
    PARTICIPLE(Tense.PRESENT, Tense.PAST);
    private final Tense[] tenses;

    Mode(Tense... tenses) {
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

    public static boolean isMode(String mode) {
        for (Mode m : Mode.values()) {
            if (mode.equals(m.toString()))
                return true;
        }
        return false;
    }

    public boolean isTenseInMode(Tense tense) {
        for (Tense t : tenses) {
            if (t.equals(tense)) {
                return true;
            }
        }
        return false;
    }
    public Tense[] getTenses(){
        return tenses;
    }

    /**
     * Created by bachp on 3/25/2017.
     */
    public static class ModeConverter implements IStringConverter <Mode> {
        @Override
        public Mode convert(String s) {
            return getMode(s);
        }
    }
}
