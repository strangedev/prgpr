import java.util.TreeSet;
import java.util.HashSet;

/**
 * Created by strange on 10/22/16.
 *
 * Purpose:
 *  Testing the performance hit HashMap/HashSet
 *  introduces due to copying/resizing.
 *
 * Result:
 *  TreeSet: 4809 ms
 *  HashSet: 2581 ms
 *
 * Conclusion:
 *  HashMap/HashSet is faster, even for large numbers
 *  of items.
 *
 */
public class HashSet_TreeSet {

    public static void main(String[] args) {

        TreeSet<Long> treeSet = new TreeSet<>();
        HashSet<Long> hashSet = new HashSet<>();

        long startOfTreeSetBenchmark;
        long startOfHashSetBenchmark;
        long endOfTreeSetBenchmark;
        long endOfHashSetBenchmark;

       startOfTreeSetBenchmark = System.currentTimeMillis();

        for(long i = 0; i < 5000000; i++) {

            treeSet.add(i);

        }

        endOfTreeSetBenchmark = System.currentTimeMillis();

        startOfHashSetBenchmark = System.currentTimeMillis();

        for(long i = 0; i < 5000000; i++) {

            hashSet.add(i);

        }

        endOfHashSetBenchmark = System.currentTimeMillis();

        long treeSetTime = endOfTreeSetBenchmark - startOfTreeSetBenchmark;
        long hashSetTime = endOfHashSetBenchmark - startOfHashSetBenchmark;

        System.out.print("TreeSet: ");
        System.out.println(treeSetTime);

        System.out.print("HashSet: ");
        System.out.println(hashSetTime);

    }

}
