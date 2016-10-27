package com.prgpr;

import com.prgpr.helpers.Benchmark;
import com.prgpr.helpers.PageConsumer;

public class Main {
    public static void main(String[] args) {

        ArticleReader articleReader = new ArticleReader("res/infile/wikipedia_de_prgpr_subset.txt");
        PageFactory pageFactory = new PageFactory();
        PageExport pageExport = new PageExport("res/outfile/output.xml");

        pageFactory.subscribeTo(articleReader);
        pageExport.subscribeTo(pageFactory);
        pageFactory.subscribe(new PageConsumer());

        long test = Benchmark.run(articleReader);

        System.out.println("Test took: " + test);

    }
}