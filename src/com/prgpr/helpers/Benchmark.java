package com.prgpr.helpers;

/**
 * Created by kito on 10/23/16.
 * @author Kyle Rinfreschi
 *
 * Better Benchmark to count teh timez
 */
public class Benchmark {
    public static long run(Runnable fn){
        long start = System.currentTimeMillis(); // put teh timez in teh m1cr0w4vezz
        fn.run();  // m1rc0w4ve le t1m3zzzz
        long end = System.currentTimeMillis();
        return end - start;  // timez is ready
    }
}
