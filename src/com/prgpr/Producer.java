package com.prgpr;

import java.util.LinkedHashSet;

/**
 * Created by strange on 10/26/16.
 */
public abstract class Producer implements Runnable {

    private LinkedHashSet<Consumer> subscribers;

    public Producer() {

    }

    public void subscribe(Consumer subscriber) {

        this.subscribers.add(subscriber);

    }

    public void unscubscribe(Consumer subscriber) {

        this.subscribers.remove(subscriber);

    }

    public void emit(Consumable consumable) {

        this.subscribers.forEach((subscriber) -> {
            subscriber.consume(consumable);
        });

    }

    public void parallelEmit(Consumable consumable) {
        this.subscribers.parallelStream()
                        .forEach((subscriber) -> subscriber.consume(consumable));
    }

}
