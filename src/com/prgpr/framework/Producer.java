package com.prgpr.framework;

import java.util.LinkedHashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by strange on 10/26/16.
 */
public class Producer<T> implements ProducerInterface<T> {

    private LinkedHashSet<ConsumerInterface<T>> subscribers;

    public Producer() {
        subscribers = new LinkedHashSet<>();
    }

    @Override
    public void run() {}

    @Override
    public void subscribe(ConsumerInterface<T> subscriber) {
        this.subscribers.add(subscriber);
        subscriber.notify(Consumer.EVENT_SUBSCRIBED, this);
    }

    @Override
    public void unsubscribe(ConsumerInterface<T> subscriber) {
        this.subscribers.remove(subscriber);
        subscriber.notify(Consumer.EVENT_UNSUBSCRIBED, this);
    }

    @Override
    public void emit(T consumable) {
        this.subscribers.forEach((subscriber) -> {
            try {
                subscriber.getQueue().put(consumable);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void done() {
        new LinkedHashSet<>(this.subscribers).forEach(this::unsubscribe);
    }

}
