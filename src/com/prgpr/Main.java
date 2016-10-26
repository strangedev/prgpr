package com.prgpr;

import com.prgpr.helpers.Benchmark;
import com.prgpr.helpers.PageConsumer;

public class Main {
    public static void main(String[] args) {

        ArticleReader articleReader = new ArticleReader("res/infile/wikipedia_de_prgpr_subset.txt");
        PageFactory pageFactory = new PageFactory();
        PageConsumer pageConsumer = new PageConsumer();

        pageConsumer.subscribeTo(pageFactory);
        pageFactory.subscribeTo(articleReader);
        //articleReader.subscribe(pageFactory);
        //pageFactory.subscribe(pageConsumer);

        long test = Benchmark.run(articleReader::run);

        System.out.println("Test took: " + test);

    }
}