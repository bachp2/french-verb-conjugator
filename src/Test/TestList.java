package Test;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created by bachp on 6/24/2017.
 */
public class TestList {
    public static void main(String[] args){
        Stopwatch stopwatch = Stopwatch.createStarted();
        ProgramTestSuite.printListElementsToFile();
        //assertEquals(false, ProgramTestSuite.isTablesInVerbListEmpty());
        //ProgramTestSuite.printRadicalsFromList();
        //ProgramTestSuite.printRadicalsLength();
        stopwatch.stop();
        System.out.println("Elapsed time in milliseconds ==> " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
}
