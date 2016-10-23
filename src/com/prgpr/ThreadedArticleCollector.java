package com.prgpr;

import com.prgpr.collections.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Stack;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

import com.prgpr.WikiPageParser;
import com.prgpr.collections.Tuple;
import com.prgpr.Page;

/**
 * Created by strange on 10/22/16.
 *
 */
public class ThreadedArticleCollector implements Runnable, Iterator<ArrayList<Tuple<Page, String>>>{

    private Thread t;

    private final ReentrantLock lock = new ReentrantLock();

    private int chunkSize;
    private boolean allLinesParsed = true;
    private String infilePath;
    private WikiPageParser pageParser;
    private ArrayList<Tuple<Page, String>> nextArticleChunk;
    private Stack<ArrayList<Tuple<Page, String>>> articleChunks;
    private static final Logger log = LogManager.getFormatterLogger(ThreadedArticleCollector.class);

    /**
     * Constructor.
     *
     * @param chunkSize The amount of articles to be collected before making them available
     *                  For streaming to another thread.
     */
    ThreadedArticleCollector(String infilePath, int chunkSize) {
        log.info("New thread created.");

        this.chunkSize = chunkSize;
        this.infilePath = infilePath;
        pageParser = new WikiPageParser();
        this.nextArticleChunk = new ArrayList<>(this.chunkSize);

        lock.lock();
        articleChunks = new Stack<>();
        lock.unlock();

    }

    /**
     * Parses one line of the input by calling WikiPageParser.parseLine()
     * and aggregates parsed articles into chunks.
     *
     * @param line A line of input to be parsed
     */
    private void delegateParseLine(String line) {

        boolean articleComplete = pageParser.parseLine(line);

        if (articleComplete) { // add to current chunk

            try {

                Tuple<Page, String> protoPage = pageParser.getProtoPage();
                nextArticleChunk.add(protoPage);

            } catch (IllegalAccessException exception) {

                log.error(exception.getMessage());
                // @todo how to handle this properly?

            }

            if (nextArticleChunk.size() >= chunkSize) { // check if chunk has been filled - if so, add to ArrayList

                lock.lock();
                articleChunks.push(nextArticleChunk);
                lock.unlock();

                nextArticleChunk.clear(); // clear next chunk, we'll be filling it again

            }

        }

    }

    /**
     * Executes the thread.
     *
     */
    public void run() {
        log.info("Thread running.");

        // Do the deed.
        allLinesParsed = false;

        try (Stream<String> stream = Files.lines(Paths.get(infilePath))) {
            stream.forEachOrdered(this::delegateParseLine);

        }
        catch (IOException exception) {
            log.error("Couldn't get lines of file: " + infilePath);

        }

        // No more lines to fill next chunk, take it as it is.
        if (nextArticleChunk.size() > 0) {
            lock.lock();
            articleChunks.push(nextArticleChunk);
            lock.unlock();
        }
        allLinesParsed = true;

        log.info("Thread stopped.");

    }

    /**
     * Starts the thread.
     *
     */
    public void start () {
        log.info("Thread started.");

        if (t == null) { // There can only be one.
            t = new Thread (this, this.getClass().getName());
            t.start ();

        }

    }

    /**
     * Getter method for chunkSize
     *
     * @return The chunkSize with which the thread was set up.
     */
    public int getChunkSize() {
        return chunkSize;
    }

    public boolean hasNextPromise() {
        return !allLinesParsed;
    }

    @Override
    public boolean hasNext() {

        lock.lock();
        boolean ret = articleChunks.size() > 0; // @todo is .size() really O(1)?
        lock.unlock();

        return ret;
    }

    @Override
    public ArrayList<Tuple<Page, String>> next() {

        lock.lock();
        ArrayList<Tuple<Page, String>> ret = articleChunks.pop();
        lock.unlock();

        return ret;
    }

}
