package test;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * Created by bachp on 6/25/2017.
 */
public class TestTrie {
    public static void main(String[] args){
        Stopwatch stopwatch = Stopwatch.createStarted();
        ProgramTestSuite.trieExhaustiveCheck();
        ProgramTestSuite.trieExhaustiveCheckRadical();
        stopwatch.stop();
        System.out.println();
        System.out.println("Elapsed time in milliseconds ==> " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
}
