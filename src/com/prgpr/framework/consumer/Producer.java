package com.prgpr.framework.consumer;

import java.util.LinkedHashSet;

/**
 * Created by strange on 10/26/16.
 * @author Noah Hummel
 *
 * An abstract class providing methods for interfacing between
 * objects. A producer is capable of emiting objects
 * called "consumables" which will be passed to a set of
 * Consumer objects by using their consume method.
 *
 * All consumers who are subscribed to a producer will
 * receive an emitted consumable. A Producer can have any number
 * of Consumers subscribed to it.
 *
 * Producers can only have subscribers of the same type as
 * the producer itself.
 */
public abstract class Producer<T> implements Runnable {

    private LinkedHashSet<Consumer<T>> subscribers;

    /**
     * Constructor.
     */
    protected Producer() {
        subscribers = new LinkedHashSet<>();
    }

    /**
     * Start producing data to be emitted.
     *
     * A Producer can be run, which is useful for doing the
     * setup of a producer-consumer chain first, and then starting
     * the execution chain by calling run() on the first producer.
     *
     * Runnable provides a neat interface for this which is supported
     * by many other classes. #makeJavaConvenientAgain
     */
    @Override
    public void run() {}

    /**
     * Adds a subscriber to this producer.
     * Notifies the subscriber by calling onSubscribe() on it.
     *
     * @param subscriber A consumer of the same type as this producer.
     */
    public void subscribe(Consumer<T> subscriber) {

        this.subscribers.add(subscriber);
        subscriber.onSubscribe(this);

    }

    /**
     * Removes a subscriber from this producer.
     * This means there is no more data to be processed by the consumer.
     * Notifies the subscriber by calling onUnsubscribe() on it.
     *
     * @param subscriber A consumer of the same type as this producer.
     */
    public void unsubscribe(Consumer<T> subscriber) {

        this.subscribers.remove(subscriber);
        subscriber.onUnsubscribed(this);

    }

    /**
     * Passes a consumable to all of the subscribers sequentially.
     * Notifies the subscriber by calling consume() on it.
     *
     * @param consumable The consumable to be emitted of the same type as this producer.
     */
    public void emit(T consumable) {

        this.subscribers.forEach((subscriber) -> subscriber.consume(consumable));

    }

    /**
     * Notifies all subscribers that there is no more data by unsubscribing them.
     */
    public void done() {

        new LinkedHashSet<>(this.subscribers).forEach(this::unsubscribe);

    }

    /**
     * Passes a consumable to all of the subscribers in parallel.
     * Notifies the subscriber by calling consume() on it.
     *
     * @param consumable The consumable to be emitted of the same type as this producer.
     */
    public void parallelEmit(T consumable) {
        this.subscribers.parallelStream()
                        .forEach((subscriber) -> subscriber.consume(consumable));
    }

}
