package com.prgpr.helpers;

/**
 * Created by kito on 10/23/16.
 */
public class Benchmark {
    public static long run(Runnable fn){
        long start = System.currentTimeMillis();
        fn.run();
        long end = System.currentTimeMillis();
        return end - start;
    }
}
