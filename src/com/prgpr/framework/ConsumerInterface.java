package com.prgpr.framework;

import java.util.concurrent.BlockingQueue;

/**
 * Created by strange on 10/26/16.
 */
public interface ConsumerInterface<T> {

    void consume(T consumable);

    void subscribeTo(ProducerInterface<T> producer);

    void unsubscribeFrom(ProducerInterface<T> producer);

    void notify(int type, ProducerInterface<T> producer);

    BlockingQueue<T> getQueue();
}
