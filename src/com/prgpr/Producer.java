package com.prgpr;

import java.util.LinkedHashSet;

/**
 * Created by strange on 10/26/16.
 */
public abstract class Producer<T> implements Runnable {

    private LinkedHashSet<Consumer<T>> subscribers;

    public Producer() {
        subscribers = new LinkedHashSet<>();
    }

    @Override
    public void run() {}

    public void subscribe(Consumer<T> subscriber) {

        this.subscribers.add(subscriber);

    }

    public void unscubscribe(Consumer<T> subscriber) {

        this.subscribers.remove(subscriber);

    }

    public void emit(Consumable<T> consumable) {

        this.subscribers.forEach((subscriber) -> {
            subscriber.consume(consumable);
        });

    }

    public void parallelEmit(Consumable<T> consumable) {
        this.subscribers.parallelStream()
                        .forEach((subscriber) -> subscriber.consume(consumable));
    }

}
