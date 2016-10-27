package com.prgpr.framework;

/**
 * Created by kito on 10/27/16.
 */
public abstract class ConsumerProducer<T,T1> extends Producer<T> implements Consumer<T1> {
    @Override
    public void onUnsubscribed(Producer<T1> producer) {
        this.done();
    }
}
