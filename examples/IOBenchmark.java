import com.prgpr.helpers.Benchmark;

import java.io.IOException;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by strange on 10/25/16.
 */
public class IOBenchmark {



    public static void main(String[] args) {

        long test = Benchmark.run(() -> {
            try (Stream<String> stream = Files.lines(Paths.get("res/infile/wikipedia_de_prgpr_subset.txt"))) {
                stream.forEach(x -> {

                });

            } catch (IOException e) {
                System.out.println(e.getMessage());

            }
        });

        System.out.println("Test took " + test + " ms");
    }

}
