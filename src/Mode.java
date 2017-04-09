import com.beust.jcommander.IStringConverter;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Created by bachp on 3/25/2017.
 */
public enum Mode {
    infinitive(Tense.present),
    indicative(Tense.present, Tense.imperfect, Tense.future, Tense.past),
    conditional(Tense.present),
    subjunctive(Tense.present, Tense.imperfect),
    imperative(Tense.present),
    participle(Tense.present, Tense.past);
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
    enum Tense {
        present("infinitive-present", "imperative-present", "present-participle"),
        imperfect,
        future,
        past("simple-past", "past-participle");
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
            if (this == Tense.present) {
                switch (m) {
                    case infinitive:
                        return this.tenses[0];
                    case imperative:
                        return this.tenses[1];
                    case participle:
                        return this.tenses[2];
                    default:
                        break;
                }
            } else if (this == Tense.past) {
                switch (m) {
                    case indicative:
                        return this.tenses[0];
                    case participle:
                        return this.tenses[1];
                    default:
                        break;
                }
            }
            return this.toString();
        }

        public static class TenseConverter implements IStringConverter <ArrayList <Tense>> {

            @Override
            public ArrayList <Tense> convert(String s) {
                String[] temp = s.split(",|/.|;");
                ArrayList <Tense> tmp = new ArrayList <>();
                for (String e : temp) {
                    tmp.add(Tense.toTense(e));
                }
                return tmp;
            }
        }
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
