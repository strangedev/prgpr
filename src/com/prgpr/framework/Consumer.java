package com.prgpr.framework;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by kito on 10/28/16.
 */
public class Consumer<T> implements ConsumerInterface<T>, Runnable {
    public static final int EVENT_SUBSCRIBED = 0;
    public static final int EVENT_UNSUBSCRIBED = 1;

    private boolean interuppted = false;
    public BlockingQueue<T> queue;
    private boolean isUnsubscribed = true;

    protected Consumer() {
        queue = new LinkedBlockingQueue<>();
    }

    public synchronized void subscribeTo(ProducerInterface<T> producer) {
        producer.subscribe(this);
    }

    public synchronized void unsubscribeFrom(ProducerInterface<T> producer) {
        producer.unsubscribe(this);
    }

    @Override
    public synchronized void notify(int type, ProducerInterface<T> producer){
        switch (type){
            case EVENT_SUBSCRIBED:
                isUnsubscribed = false;
                this.onUnsubscribed(producer);
                break;
            case EVENT_UNSUBSCRIBED:
                isUnsubscribed = true;
                this.onUnsubscribed(producer);
                break;
        }
    }

    @Override
    public BlockingQueue<T> getQueue() {
        return queue;
    }

    public void onSubscribe(ProducerInterface<T> producer){}

    public void onUnsubscribed(ProducerInterface<T> producer){}

    public void onStart() {}

    public void onComplete() {}

    @Override
    public void consume(T consumable) {}

    @Override
    public void run() {
        this.onStart();
        while (!this.interuppted && (this.queue.size() > 0 || !isUnsubscribed)) {
            try {
                T data = this.queue.poll(10, TimeUnit.MILLISECONDS);
                if(data != null) {
                    this.consume(data);
                }
            } catch (InterruptedException e) {
                this.interuppted = true;
            }
        }
        this.onComplete();
    }
}
