import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by strange on 10/22/16.
 *
 * Purpose:
 *  Trying to determine, whether Streams match the performance
 *  of iterators and if parallel Streams are able to boost
 *  performance in when using static methods.
 *
 * Result:
 *  Sample size: 500000
 *  For: 10560 ms
 *  Stream: 11069 ms
 *  Parallel Stream: 5605 ms
 *
 */
public class ForVsStreamVsParallelStream {

    private static String someTimeIntensiveFunction(String input) {

        for(int i = 0; i < 2000; i++) {

            if(i % 2 == 0) {
                input = input.toUpperCase();
            } else {
                input = input.toLowerCase();
            }

        }

        return input;

    }

    public static void main(String args[]) {

        long startOfForBenchmark;
        long startOfStreamBenchmark;
        long startOfParallelStreamBenchmark;

        long endOfForBenchmark;
        long endOfStreamBenchmark;
        long endOfParallelStreamBenchmark;

        ArrayList<String> sampleInput = new ArrayList<>();

        // Generate sampleInput
        String tmpString;
        for(long i = 0; i < 500000; i++) {

           tmpString = "";

            for(short a = 0; a < 5; a++){
                tmpString = tmpString.concat(String.valueOf(String.valueOf(i).charAt(0)));
            }

            sampleInput.add(tmpString);

        }

        // Traditional iterator
        startOfForBenchmark = System.currentTimeMillis();

        for (ListIterator<String> iter = sampleInput.listIterator(); iter.hasNext();) {
            String element = iter.next();

            element = someTimeIntensiveFunction(element);

        }

        endOfForBenchmark = System.currentTimeMillis();

        // Stream API
        startOfStreamBenchmark = System.currentTimeMillis();

        sampleInput.stream().forEach(ForVsStreamVsParallelStream::someTimeIntensiveFunction);

        endOfStreamBenchmark = System.currentTimeMillis();

        //Using .parallelStream()
        startOfParallelStreamBenchmark = System.currentTimeMillis();

        sampleInput.parallelStream().forEach(ForVsStreamVsParallelStream::someTimeIntensiveFunction);

        endOfParallelStreamBenchmark = System.currentTimeMillis();

        long forTime = endOfForBenchmark - startOfForBenchmark;
        long streamTime = endOfStreamBenchmark - startOfStreamBenchmark;
        long parallelStreamTime = endOfParallelStreamBenchmark - startOfParallelStreamBenchmark;

        System.out.print("Sample size: ");
        System.out.println(sampleInput.size());

        System.out.print("For: ");
        System.out.print(forTime);
        System.out.println(" ms");

        System.out.print("Stream: ");
        System.out.print(streamTime);
        System.out.println(" ms");

        System.out.print("Parallel Stream: ");
        System.out.print(parallelStreamTime);
        System.out.println(" ms");

    }

}
