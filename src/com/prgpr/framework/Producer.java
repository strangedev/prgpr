package com.prgpr.framework;

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

    public void unsubscribe(Consumer<T> subscriber) {

        this.subscribers.remove(subscriber);

    }

    public void emit(T consumable) {

        this.subscribers.forEach((subscriber) -> {
            subscriber.consume(consumable);
        });

    }

    public void done() {

        new LinkedHashSet<>(this.subscribers).forEach((subscriber) -> {
            subscriber.unsubscribeFrom(this);
        });

    }

    public void parallelEmit(T consumable) {
        this.subscribers.parallelStream()
                        .forEach((subscriber) -> subscriber.consume(consumable));
    }

}
