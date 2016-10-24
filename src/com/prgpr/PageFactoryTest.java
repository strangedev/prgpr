package com.prgpr;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.prgpr.PageFactory;
import com.prgpr.helpers.Benchmark;

import java.util.Set;
// TODO: 10/24/16 complete docstrings
/**
 * Created by strange on 10/23/16.
 */
public class PageFactoryTest {

    @Test
    public void extractPages() throws Exception {


        /*int best_i = 0;
        long best_time = 0;

        for (int i = 100; i < 5000; i += 100) {
            long test = Benchmark.run(() -> {

                Set<Page> pagesContained = PageFactory.extractPages("res/infile/wikipedia_de_prgpr_subset.txt");

            });

            System.out.println("Chunksize: " + i + ", Test took " + test + " ms");

            if (test < best_time) {
                best_i = i;
                best_time = test;
            }

        }

        System.out.println("Best time was: " + best_time + ", best chunkSize was: " + best_i);
        */
        long test = Benchmark.run(() -> {

            Set<Page> pagesContained = PageFactory.extractPages("res/infile/wikipedia_de_prgpr_subset.txt");

        });
        //Set<Page> pagesContained = PageFactory.extractPages("res/infile/wikipedia_de_prgpr_subset.txt");
        System.out.println("Test took: " + test);

        //int i = 0;

        /*for (Page page: pagesContained) {

            System.out.println("------------------------------");
            System.out.println(page);
            System.out.println(page.getTitle());
            System.out.println(page.getNamespaceID());
            System.out.println(page.getId());
            page.getCategories().forEach(System.out::println);

            if (i > 100) break;
            i++;

        }*/

        //assertTrue(pagesContained.size() == 50560);


        /*or (int i = 0; i < 10; i++) {

            System.out.println("Run no.: " + i);
            // Set<Page> pagesContained2 = PageFactory.extractPages("res/infile/shortened.txt");
            Set<Page> pagesContained2 = PageFactory.extractPages("res/infile/wikipedia_de_prgpr_subset.txt");
            assertTrue(pagesContained.size() == pagesContained2.size());

        }*/

    }

}