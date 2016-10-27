package com.prgpr.helpers;

import com.prgpr.framework.Consumer;
import com.prgpr.data.Page;
import com.prgpr.framework.Producer;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by strange on 10/26/16.
 */
public class PageStatistics implements Consumer<Page> {

    private int consumedPages = 0;
    private long lastTime = 0;
    private long seconds = 0;
    private Set<Long> articlesPerSecond;

    @Override
    public void consume(Page consumable) {
        this.consumedPages++;

        long timeNow = System.currentTimeMillis();
        long timeSince = timeNow - this.lastTime;

        if(timeSince > 1000){
            this.seconds += timeSince / 1000;
            articlesPerSecond.add(this.consumedPages / this.seconds);
            this.lastTime = timeNow;
        }
    }

    @Override
    public void onSubscribe(Producer<Page> producer) {
        this.lastTime = System.currentTimeMillis();
        this.articlesPerSecond = new LinkedHashSet<>();
    }

    @Override
    public void onUnsubscribed(Producer<Page> producer) {
        LongAdder adder = new LongAdder();
        int numArticlesPerSecond = articlesPerSecond.size();
        articlesPerSecond.parallelStream().forEach(adder::add);

        System.out.println("Average articles per second: " + (adder.intValue() / numArticlesPerSecond));
        System.out.println(this.consumedPages + " articles consumed in " + this.seconds + " seconds");
    }
}
