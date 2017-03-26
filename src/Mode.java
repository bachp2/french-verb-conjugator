import com.beust.jcommander.IStringConverter;

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
    private Tense[] tenses;

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
    public static boolean isMode(String mode){
        for (Mode m : Mode.values()) {
            if (mode.equals(m.toString()))
                return true;
        }
        return false;
    }
    public boolean isTenseInMode(Tense tense){
        for(Tense t : tenses){
            if(t.equals(tense)){
                return true;
            }
        }
        return false;
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
    enum Tense{
        present, imperfect, future, past;
        public static Tense toTense(String s){
            for(Tense t : Tense.values()){
                if (s.equals(t.toString()) ){
                    return t;
                }
            }
            throw new IllegalArgumentException();
        }
        public String toString(Mode m){
            String tense = this.toString();
            if (this == Tense.present) {
                switch (m) {
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
            } else if (this == Tense.past) {
                switch (m) {
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
            return tense;
        }
        public static class TenseConverter implements IStringConverter<Tense>{

            @Override
            public Tense convert(String s) {
                return Tense.toTense(s);
            }
        }
    }
}
