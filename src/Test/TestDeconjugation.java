package Test;

import DataStructure.Verb;
import com.google.common.base.Stopwatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by bachp on 6/25/2017.
 */
public class TestDeconjugation {
    public static void main(String[] args){
        Stopwatch stopwatch = Stopwatch.createStarted();
//        ProgramTestSuite.printDeconjugation();
//        ProgramTestSuite.testDeconjugation();
       //ProgramTestSuite.testDeconjugation2("acculturer");
        //assertEquals(compare, test);
        stopwatch.stop();
        System.out.println();
        System.out.println("Elapsed time in milliseconds ==> " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
}
