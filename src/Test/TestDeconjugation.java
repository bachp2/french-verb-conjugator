package Test;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * Created by bachp on 6/25/2017.
 */
public class TestDeconjugation {
    public static void main(String[] args){
        Stopwatch stopwatch = Stopwatch.createStarted();
        ProgramTestSuite.printDeconjugation();
        ProgramTestSuite.testDeconjugation();
        stopwatch.stop();
        System.out.println("Elapsed time in milliseconds ==> " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
}
