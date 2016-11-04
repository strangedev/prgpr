package com.prgpr;

import com.prgpr.helpers.PageStatistics;

/**
 * @author Elizaveta Kovalevskaya
 *
 * The Main class which puts together the other classes and runs the program
 */
public class Main {

    /**
     * Creates instances of the classes and subscribes them to each other so that the program runs in the right order
     *
     * @param args input and output file paths
     */
    public static void main(String[] args) {

        ArticleReader articleReader = new ArticleReader("res/infile/wikipedia_de_prgpr_subset.txt");
        PageFactory pageFactory = new PageFactory();
        PageExport pageExport = new PageExport("res/outfile/output.xml");

        pageFactory.subscribeTo(articleReader);
        pageExport.subscribeTo(pageFactory);
        new PageStatistics().subscribeTo(pageFactory);

        articleReader.run();
    }
}