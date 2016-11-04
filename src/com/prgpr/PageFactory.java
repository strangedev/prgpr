package com.prgpr;

import com.prgpr.data.Page;
import com.prgpr.data.ProtoPage;
import com.prgpr.framework.ConsumerProducer;

/**
 * Created by strange on 10/21/16.
 * @author Noah Hummel
 *
 * A ConsumerProducer which turns ProtoPages into Pages by performing extraction
 * methods on the ProtoPage's html data.
 */
public class PageFactory extends ConsumerProducer<Page, ProtoPage> {

    /**
     * Consumer method which processes one ProtoPage at a time.
     * Uses LinkExtraction to extract categories from the ProtoPage's
     * htmlData and emits the final Page.
     *
     * Deviation from assignment:
     * PageFactory is not responsible for reading the input file.
     * A separate class ArticleReader is responsible for performing I/O and
     * reading the metadata line for each article.
     *
     * Reasoning:
     * Decoupling Page creation from parsing is necessary to keep a clear
     * distinction between class responsibilities.
     * As this project progresses, PageFactory will have to invoke more
     * extraction methods and grow in functionality. Performing all of
     * the I/O as well would lead to a god-object which can't be understood
     * by others anymore.
     *
     * @param consumable A ProtoPage object with missing category data.
     */
    @Override
    public void consume(ProtoPage consumable) {

        consumable.getPage()
                 .setCategories(
                         LinkExtraction.extractCategories(
                                 consumable.getHtmlData().toString()  // Uses StringBuilder class
                         )
                 );

        this.emit(consumable.getPage());  // only emit the resulting Page object

    }
}
