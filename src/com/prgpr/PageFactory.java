package com.prgpr;

import com.prgpr.collections.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashSet;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.prgpr.exceptions.MalformedWikidataException;

/**
 * Created by strange on 10/21/16.
 * @author Noah Hummel
 *
 * A Factory class which creates a Set of Page objects from parsing a file of wikidata.
 */
public class PageFactory extends Producer<Page> implements Consumer<ProtoPage> {

    @Override
    public void consume(Consumable<ProtoPage> consumable) {

        ProtoPage protoPage = consumable.get();
        protoPage.getInstance()
                 .setCategories(
                         LinkExtraction.extractCategories(
                                 protoPage.getHtmlData()
                         )
                 );
        this.emit(new Consumable<>(protoPage.getInstance()));

    }

    @Override
    public void unsubscribed(Producer<ProtoPage> producer) {
        this.done();
    }
}
