package com.prgpr;

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
        this.unsubscribed(producer);
    }

    default void unsubscribed(Producer<T> producer){};

}
