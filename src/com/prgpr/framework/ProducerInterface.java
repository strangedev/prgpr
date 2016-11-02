package com.prgpr.framework;

import java.util.concurrent.BlockingQueue;

/**
 * Created by kito on 10/28/16.
 */
public interface ProducerInterface<T> extends Runnable {
    @Override
    void run();

    void subscribe(ConsumerInterface<T> subscriber);

    void unsubscribe(ConsumerInterface<T> subscriber);

    void emit(T consumable);

    void done();
}
