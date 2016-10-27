package com.prgpr;

import com.prgpr.data.Page;
import com.prgpr.data.ProtoPage;
import com.prgpr.framework.Consumer;
import com.prgpr.framework.ConsumerProducer;
import com.prgpr.framework.Producer;

/**
 * Created by strange on 10/21/16.
 * @author Noah Hummel
 *
 * A Factory class which creates a Set of Page objects from parsing a file of wikidata.
 */
public class PageFactory extends ConsumerProducer<Page, ProtoPage> {

    @Override
    public void consume(ProtoPage consumable) {

        ProtoPage protoPage = consumable;
        protoPage.getInstance()
                 .setCategories(
                         LinkExtraction.extractCategories(
                                 protoPage.getHtmlData().toString()
                         )
                 );
        this.emit(protoPage.getInstance());

    }
}
