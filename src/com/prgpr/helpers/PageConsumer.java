package com.prgpr.helpers;

import com.prgpr.framework.Consumer;
import com.prgpr.data.Page;

import java.util.LinkedHashSet;

/**
 * Created by strange on 10/26/16.
 */
public class PageConsumer implements Consumer<Page> {

    private LinkedHashSet<Page> pages = new LinkedHashSet<>();

    @Override
    public void consume(Page consumable) {
        pages.add(consumable);
        System.out.println(pages.size() + " | " + consumable.getTitle());
    }
}
