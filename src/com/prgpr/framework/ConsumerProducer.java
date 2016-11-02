package com.prgpr.framework;

import java.util.concurrent.BlockingQueue;

/**
 * Created by kito on 10/27/16.
 */
public abstract class ConsumerProducer<T,T1> extends Consumer<T1> implements ProducerInterface<T> {
    protected Producer<T> producer;

    public ConsumerProducer(){
        super();
        producer = new Producer<>();
    }

    @Override
    public void subscribe(ConsumerInterface<T> subscriber) {
        producer.subscribe(subscriber);
    }

    @Override
    public void unsubscribe(ConsumerInterface<T> subscriber) {
        producer.unsubscribe(subscriber);
    }

    @Override
    public void emit(T consumable) {
        producer.emit(consumable);
    }

    @Override
    public void done() {
        producer.done();
    }

    @Override
    public void onComplete() {
        this.done();
    }
}
