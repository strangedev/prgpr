package com.prgpr;

import com.prgpr.helpers.Benchmark;
import com.prgpr.helpers.PageStatistics;

public class Main {
    public static void main(String[] args) {

        ArticleReader articleReader = new ArticleReader("res/infile/wikipedia_de_prgpr_subset.txt");
        PageFactory pageFactory = new PageFactory();
        PageExport pageExport = new PageExport("res/outfile/output.xml");
        PageStatistics pageStatistics = new PageStatistics();

        pageFactory.subscribeTo(articleReader);
        pageExport.subscribeTo(pageFactory);
        pageStatistics.subscribeTo(pageFactory);

        new Thread(pageFactory).start();
        new Thread(pageExport).start();
        new Thread(pageStatistics).start();

        long time = Benchmark.run(articleReader::run);
        System.out.println("IO processed in : " + time + " milliseconds");
    }
}