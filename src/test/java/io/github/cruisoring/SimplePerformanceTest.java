package io.github.cruisoring;

import io.github.cruisoring.function.ConsumerThrowable;
import io.github.cruisoring.logger.Logger;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

import java.util.Random;
import java.util.stream.IntStream;

public class SimplePerformanceTest {
    public static final int timesToRun = 10;
    public static final int ArraySize = 500000;
    public static final int[] intArray = new int[ArraySize];
    public static final boolean[] booleanArray = new boolean[ArraySize];

    private static final StopWatch stopWatch = new StopWatch();
    private static final Random random = new Random();
    static {
        for (int i = 0; i < ArraySize; i++) {
            intArray[i] = random.nextInt();
        }
    }

    public static long getLeastTimeToRun(Runnable testMethod){
        long leastNano = Long.MAX_VALUE;

        for (int i = 0; i < timesToRun; i++) {
            stopWatch.reset();
            stopWatch.start();
            testMethod.run();
            stopWatch.stop();
            long elapsed = stopWatch.getNanoTime();
            if(leastNano > elapsed)
                leastNano = elapsed;
        }
        return leastNano;
    }

    @Test
    public void test_Loops(){
        Runnable testMethod = () -> {
            for (int i = 0; i < ArraySize; i++) {
                booleanArray[i] = intArray[i] > 0;
            }
        };

        long time = getLeastTimeToRun(testMethod);
        Double millis = time/1000000.0;
        Logger.D("Least time to run test_Loops of int[%d]: %.3fms", ArraySize, millis);
    }

    @Test
    public void test_Stream(){


        Runnable testMethod = () -> {
            IntStream.range(0, ArraySize).boxed().parallel().forEach(i -> {
                final boolean[] booleans = booleanArray;
                final int[] ints = intArray;
                booleans[i] = ints[i]  > 0;
            });
        };

        long time = getLeastTimeToRun(testMethod);
        Double millis = time/1000000.0;
        Logger.D("Least time to run test_StreamParallel of int[%d]: %.3fms", ArraySize, millis);
    }

    @Test
    public void testRunParallel(){
        ConsumerThrowable<Integer> testMethod = i -> {
            final boolean[] booleans = booleanArray;
            final int[] ints = intArray;
            booleans[i] = ints[i]  > 0;
        };
        long time = getLeastTimeToRun(()-> Functions.runParallel(testMethod, ArraySize));
        Double millis = time/1000000.0;
        Logger.D("Least time to run testRunParallel of int[%d]: %.3fms", ArraySize, millis);
    }

}
