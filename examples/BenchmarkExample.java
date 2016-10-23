import com.prgpr.helpers.Benchmark;

/**
 * Created by kito on 10/23/16.
 */
public class BenchmarkExample {

    public static void main(String[] args) {

        long test = Benchmark.run(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Test took " + test + " ms");
    }
}
