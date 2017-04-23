import DataStructure.Mode;
import DataStructure.Tense;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by bachp on 3/25/2017.
 */
public class Main {
    @Parameter(names = {"--verb", "-v"})
    private List <String> verbs = new ArrayList <>();
    @Parameter(names = {"--mode", "-m"}, converter = Mode.ModeConverter.class)
    private List <Mode> modes = new ArrayList <>();
    @Parameter(names = {"--tense", "-t"}, converter = Tense.TenseConverter.class)
    List <ArrayList <Tense>> tenses = new ArrayList <>();

    /**
     * build the program
     *
     * @param s
     */
    public static void build(Scanner s, Main m) {
        Program prg = new Program();
        String intro = "+-----------------------+\n" +
                "|french-verb-conjugator |\n" +
                "|\t\t\t\t\t\t|\n" +
                "|@author: Bach Phan\t\t|\n" +
                "|\t\t\t\t\t\t|\n" +
                "|@version: 3/25/2017\t|\n" +
                "|\t\t\t\t\t\t|\n" +
                "+-----------------------+";
        while (true) {
            System.out.println(intro);
            System.out.print(">>> ");
            String input = s.nextLine();
            if (input.matches("exit|q"))
                System.exit(0);
            new JCommander(m, input.split(" "));
            m.run(prg);
        }
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        Main main = new Main();
        build(s, main);
    }

    public void run(Program prg) {

    }
}
