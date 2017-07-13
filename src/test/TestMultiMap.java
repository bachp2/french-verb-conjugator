package test;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * Created by bachp on 6/24/2017.
 */
public class TestMultiMap {
    public static void main(String[] args){
        Stopwatch stopwatch = Stopwatch.createStarted();
        //ProgramTestSuite.printVerbsWithSameTemplateName();
        ProgramTestSuite.printTNToFile();
        stopwatch.stop();
        System.out.println("Elapsed time in milliseconds ==> " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
}
