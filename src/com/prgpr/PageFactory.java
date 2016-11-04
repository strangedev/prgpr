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
