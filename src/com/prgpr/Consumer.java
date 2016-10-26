package com.prgpr;

/**
 * Created by strange on 10/26/16.
 */
public interface Consumer<T> {

    void consume(Consumable<T> consumable);

    default void subscribeTo(Producer<T> producer) {
        producer.subscribe(this);
    }

}
