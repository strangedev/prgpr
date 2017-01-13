package com.prgpr.TextAnalysis.Testing;

import com.prgpr.PageFinder;
import com.prgpr.TextAnalysis.Convolution.ApplicationOrder;
import com.prgpr.TextAnalysis.Convolution.ConvolutionKernel;
import com.prgpr.TextAnalysis.Data.Document;
import com.prgpr.TextAnalysis.Evaluators.Evaluators;
import com.prgpr.TextAnalysis.Evaluators.TraitEvaluator;
import com.prgpr.TextAnalysis.Parsing.HtmlParser;
import com.prgpr.data.Page;
import com.prgpr.framework.database.EmbeddedDatabase;
import com.prgpr.framework.database.EmbeddedDatabaseFactory;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by strange on 1/13/17.
 */
public class Main {

    public static void main(String[] args) {

        assert args.length == 4;

        EmbeddedDatabase graphDb = EmbeddedDatabaseFactory.newEmbeddedDatabase(args[0]);
        PageFinder.setDatabase(graphDb);
        Page page = PageFinder.findByNamespaceAndTitle(Integer.valueOf(args[1]), args[2]);

        System.out.print(graphDb.getAllElements().count());
        System.out.println(" entries in db");

        System.out.print("][ ");
        for (String s :
                args) {
            System.out.print(s);
            System.out.print(" ][ ");
        }
        System.out.println("");

        if (page == null) {
            System.out.println("Not found.");
            return;
        }

        HtmlParser parser = new HtmlParser();
        TraitEvaluator[] evaluators = {Evaluators::isNumeric};
        ApplicationOrder ao = new ApplicationOrder(evaluators);
        Document doc = Document.fromFormattedWords(parser.asWords(page.getHtml()), ao);

        doc.words.forEach(w -> {
            System.out.print(w.formattedWord.modifiers.size());
            System.out.print(" ~*~ ");
            System.out.print(w.formattedWord.content);
            w.formattedWord.modifiers.forEach( x -> {
                System.out.print(" !! ");
                System.out.print(x);
            }
            );
            System.out.println("");
        }
        );

        System.out.println(doc.usefulness);

        float w[][] = {{1}, {1}};
        ConvolutionKernel kernel = new ConvolutionKernel(2, 1, w, doc);

        Stream<Document> frameStream = StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(kernel, Spliterator.ORDERED),
                false);

        frameStream.filter(x -> x.usefulness >= 0.0)
                   .forEach( x -> {
            x.words.forEach(word -> {
                System.out.print(word.formattedWord.content);
                System.out.print(" ");
            });
            System.out.print(" ~> ");
            System.out.println(x.usefulness);
        });

        /*while (kernel.hasNext()) {
            Document frame = kernel.next();
            frame.words.forEach(word -> {
                System.out.print(word.formattedWord.content);
                System.out.print(" ");
            });
            System.out.print(" ~> ");
            System.out.println(frame.usefulness);
        }*/

    }

}
