package com.prgpr.helpers;

import com.prgpr.PageFactory;
import com.prgpr.data.Page;
import com.prgpr.framework.consumer.Consumer;
import com.prgpr.framework.consumer.Producer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by strange on 11/4/16.
 *
 * A Consumer class which generates statistics for the producer it is
 * subscribed to. Uses log4j to log the satistics in real-time.
 *
 * @author Noah Hummel
 */
public class ProducerLogger<T> implements Consumer<T> {

    private static final Logger log = LogManager.getFormatterLogger(ProducerLogger.class);

    private boolean logAll = false;
    private long consumed = 0;
    private long lastTime = 0;
    private long seconds = 0;
    private Set<Long> consumablesPerSecond;
    private String producerName;

    /**
     * Default constructor.
     *
     * The logger will only log statistics once the producer has finished
     * producing consumables.
     */
    public ProducerLogger(){

    }

    /**
     * Alternate constructor.
     *
     * Lets the user decide whether to log each produced consumable individually.
     * Warning: Logging each consumable costs CPU time.
     * @param logAll Whether to log each consumable individually.
     */
    public ProducerLogger(boolean logAll){
        this.logAll = logAll;
    }

    /**
     * Consumer method which counts the amount of consumables emitted and
     * generates timing statistics. Logs each consumable consumed if logAll
     * was set during instantiation.
     *
     * @param consumable A consumable to be logged and counted.
     */
    @Override
    public void consume(T consumable) {
        this.consumed++;  // count all the consumables.

        long timeNow = System.currentTimeMillis();
        long timeSince = timeNow - this.lastTime;

        if(timeSince > 1000){
            this.seconds += timeSince / 1000;
            consumablesPerSecond.add(this.consumed / this.seconds);  // add new data point to timing statistics
            this.lastTime = timeNow;
        }

        if (this.logAll) {
            log.info(this.producerName + " emitted " + this.consumed + " consumables.");
        }

    }

    /**
     * onSubscribe event handler.
     * Sets up internal members to start producing statistics.
     *
     * @param producer The producer this is subscribed to.
     */
    @Override
    public void onSubscribe(Producer<T> producer) {
        this.lastTime = System.currentTimeMillis();
        this.consumablesPerSecond = new LinkedHashSet<>();
        this.producerName = producer.getClass().getName();
    }

    /**
     * onUnsubscribe event handler.
     * Generates the final report for the producer and logs it.
     *
     * @param producer The producer this was subscribed to.
     */
    @Override
    public void onUnsubscribed(Producer<T> producer) {

        LongAdder adder = new LongAdder();
        int numConsumablesPerSecond = consumablesPerSecond.size();
        consumablesPerSecond.parallelStream().forEach(adder::add);  // adds all data points for averaging

        if (numConsumablesPerSecond < 1) return;

        log.info(
                "Final performance record for " +
                        this.producerName +
                        " | Average throughput: " +
                        (adder.intValue() / numConsumablesPerSecond) +
                        " consumables per second | " +
                        this.consumed + " consumables produced in " +
                        this.seconds + " seconds"
        );
    }

}
