package com.prgpr.framework;

/**
 * Created by strange on 10/26/16.
 */
public interface Consumer<T> {

    void consume(T consumable);

    default void subscribeTo(Producer<T> producer) {
        producer.subscribe(this);
    }

    default void unsubscribeFrom(Producer<T> producer) {
        producer.unsubscribe(this);
    }

    default void onSubscribe(Producer<T> producer){};

    default void onUnsubscribed(Producer<T> producer){};

}
