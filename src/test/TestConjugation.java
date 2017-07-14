package test;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * Created by bachp on 6/25/2017.
 */
class TestConjugation {
    public static void main(String[] args){
        //todo redo Tense enummap
        Stopwatch stopwatch = Stopwatch.createStarted();
        ProgramTestSuite.printRandomConjugation();
        stopwatch.stop();
        System.out.println("Elapsed time in milliseconds ==> " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
}
