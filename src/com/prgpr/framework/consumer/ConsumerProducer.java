package com.prgpr.framework.consumer;

/**
 * Created by kito on 10/27/16.
 *
 * An abstract class providing methods for interfacing between
 * objects. A ConsumerProducer is a Consumer which consumes
 * consumables from another producer and emits consumables to
 * any number of consumers.
 *
 * @author Kyle Rinfreschi
 */
public abstract class ConsumerProducer<TC,TP> extends Producer<TP> implements Consumer<TC> {

    /**
     * Default implementation of event handler.
     *
     * Because in many cases, consumables can only be emitted by
     * a ConsumerProducer as long as new consumables are consumed
     * by another producer, this implementation automatically
     * tears down the producer when unsubscribed from it's producer.
     *
     * his default method should be overridden if the implementation of
     * ConsumerProducer has to handle any kind of additional teardown
     * before unsubscribing it's subscribers.
     *
     * @param producer The producer from which this consumer was just unsubscribed from.
     */
    @Override
    public void onUnsubscribed(Producer<TC> producer) {
        this.done();
    }
}
