package com.prgpr;

import com.prgpr.data.ProtoPage;
import com.prgpr.data.Page;
import com.prgpr.helpers.PageStatistics;
import com.prgpr.helpers.ProducerLogger;

/**
 * @author Elizaveta Kovalevskaya
 *
 * The Main class which puts together the other classes and runs the program
 */
public class Main {

    /**
     * creates instances of the classes and subscribes them to each other so that the program runs in the right order
     *
     * @param args input and output file paths
     */
    public static void main(String[] args) {

        ArticleReader articleReader = new ArticleReader("res/infile/wikipedia_de_prgpr_subset.txt");
        PageFactory pageFactory = new PageFactory();
        PageExport pageExport = new PageExport("res/outfile/output.xml");

        ProducerLogger<ProtoPage> protoPageProducerLogger = new ProducerLogger<>(true);
        ProducerLogger<Page> pageProducerLogger = new ProducerLogger<>(true);

        protoPageProducerLogger.subscribeTo(articleReader);
        pageFactory.subscribeTo(articleReader);
        pageProducerLogger.subscribeTo(pageFactory);
        pageExport.subscribeTo(pageFactory);


        articleReader.run();
    }
}