import java.io.IOException;

public class ConjugationDriver {
    public static void main(String[] args) throws IOException {
        Mode.imperative.getTense("past");

    }

    public static void displayAll(String verb) {
        String[] modes = {"infinitive", "indicative", "conditional",
                            "subjunctive", "imperative", "participle"};
        int count = 0;
        for (String m : modes) {
            String[] tenses = mode.get(m);
            for (String t : tenses) {
                System.out.printf("%d. %s %s", count++, m, t);
                System.out.println();
                display();
            }
        }
    }
}



