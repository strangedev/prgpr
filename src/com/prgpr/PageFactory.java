package com.prgpr;

import com.prgpr.data.Page;
import com.prgpr.data.ProtoPage;
import com.prgpr.framework.ConsumerProducer;

/**
 * Created by strange on 10/21/16.
 * @author Noah Hummel
 *
 * A Factory class which creates a Set of Page objects from parsing a file of wikidata.
 */
public class PageFactory extends ConsumerProducer<Page, ProtoPage> {

    @Override
    public void consume(ProtoPage consumable) {

        consumable.getPage()
                 .setCategories(
                         LinkExtraction.extractCategories(
                                 consumable.getHtmlData().toString()
                         )
                 );
        this.emit(consumable.getPage());

    }
}
