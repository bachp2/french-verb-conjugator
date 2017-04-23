package DataStructure;

import com.beust.jcommander.IStringConverter;

import java.util.ArrayList;

import static DataStructure.Mode.getMode;

/**
 * Created by bachp on 4/22/2017.
 */
public enum Tense {
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

    public static class TenseConverter implements IStringConverter<ArrayList<Tense>> {

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


