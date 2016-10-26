package com.prgpr.helpers;

import com.prgpr.Consumable;
import com.prgpr.Consumer;
import com.prgpr.Page;

import java.util.LinkedHashSet;

/**
 * Created by strange on 10/26/16.
 */
public class PageConsumer implements Consumer<Page> {

    //private long articlesConsumed = 0;
    private LinkedHashSet<Page> pages = new LinkedHashSet<>();

    @Override
    public void consume(Consumable<Page> consumable) {
        pages.add(consumable.get());
        //rticlesConsumed++;
        //System.out.println(articlesConsumed + " | " + consumable.get().getTitle());
    }
}
