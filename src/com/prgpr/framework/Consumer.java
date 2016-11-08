package com.prgpr.framework;

/**
 * Created by strange on 10/26/16.
 * @author Noah Hummel
 *
 * An interface providing methods for interfacing between
 * objects. A consumer is capable of consuming objects
 * called "consumables" which will are passed to it by
 * a Producer of the same type.
 *
 * Consumers are able to subscribe to any number of Producers.
 *
 * Consumer can only be subscribed to producers od the same
 * type as the consumer.
 */
public interface Consumer<T> {

    /**
     * Consume a consumable which is passed to the Consumer
     * by any of the Producers it's subscribed to.
     *
     * @param consumable The consumable to be consumed of the same type as the consumer.
     */
    void consume(T consumable);

    /**
     * Subscribes this consumer to a given producer.
     *
     * This default method shouldn't need to be overridden
     * by most implementations of consumer.
     *
     * @param producer The producer to subscribe to of the same type as the consumer.
     */
    default void subscribeTo(Producer<T> producer) {
        producer.subscribe(this);
    }

    /**
     * Unsubscribes this consumer from a given producer.
     *
     * This default method shouldn't need to be overridden
     * by most implementations of consumer.
     *
     * @param producer The producer to unsubscribe from of the same type as the consumer.
     */
    default void unsubscribeFrom(Producer<T> producer) {
        producer.unsubscribe(this);
    }

    /**
     * Event handler.
     *
     * This event handler is called when this consumer has been successfully subscribed
     * to a producer. It should never be called by another object except the producer.
     *
     * This default method should be overridden if the implementation of consumer has
     * to handle any kind of setup before being able to consume consumables from
     * a new producer.
     *
     * This method can also be used to ensure a consumer is only subscribed to
     * a maximum number of producers.
     *
     * @param producer The producer to which this consumer was just subscribed to.
     */
    default void onSubscribe(Producer<T> producer){};

    /**
     * Event handler.
     *
     * This event handler is called when this consumer was unsubscribed from a producer.
     * It should never be called by another object except the producer.
     *
     * This default method should be overridden if the implementation of consumer has
     * to handle any kind of teardown of finalization after receiving no more data
     * from the producer.
     *
     * @param producer The producer from which this consumer was just unsubscribed from.
     */
    default void onUnsubscribed(Producer<T> producer){};

}
