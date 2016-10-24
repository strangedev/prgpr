package com.prgpr;

import com.prgpr.collections.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
// TODO: 10/24/16 complete docstrings
// TODO: implement MAX_CHUNKS to cap memory usage.
/**
 * Created by strange on 10/22/16.
 * @author Noah Hummel
 *
 * A thread which uses WikiPageParser to read articles from a file of wikidata.
 * It reads in data line by line and makes data available in chunks of articles.
 * It also implements the Iterator interface to iterate through all of the chunks.
 * The iterator hides the asynchronous nature of this class - hasNext() will only return false
 * if all possible chunks have been processed by the consumer.
 */
public class ThreadedArticleCollector implements Runnable, Iterator<ArrayList<Tuple<Page, String>>>{

    private Thread t;
    private AtomicBoolean threadCanEnd = new AtomicBoolean(false); // used to signal hasNext() that all data is parsed
    private Semaphore chunksAvailable; // used to signal hasNext() to permit the consumer to use next()

    private WikiPageParser pageParser;
    private int chunkSize;
    private String infilePath;

    private ArrayList<Tuple<Page, String>> nextArticleChunk;  // a chunk is filled before adding it to articleChunks
    private BlockingQueue<ArrayList<Tuple<Page, String>>> articleChunks;  // holds all available chunks of articles

    private static final Logger log = LogManager.getFormatterLogger(ThreadedArticleCollector.class);

    /**
     * Constructor.
     *  @param infilePath The path to the file of wikidata.
     * @param chunkSize The amount of articles to be collected before making them available
     * @param maxChunks The amount of chunks that can be in memory at every given time.
     */
    ThreadedArticleCollector(String infilePath, int chunkSize, int maxChunks) {
        log.info("New thread created.");

        this.chunkSize = chunkSize;
        this.infilePath = infilePath;
        pageParser = new WikiPageParser();
        this.nextArticleChunk = new ArrayList<>(this.chunkSize);
        chunksAvailable = new Semaphore(1, true);  // Creates a fair semaphore with maximum value

        /*
         | Set the semaphore to 0 in the beginning. his is necessary, because javas semaphores are capped and start out
         | set to their maximum value. Yet another case of Java making things too easy.
         | TODO: 10/23/16 Find a better way to do this.
         */
        chunksAvailable.drainPermits();
        articleChunks = new ArrayBlockingQueue<>(maxChunks);

    }

    /**
     * Parses one line of the input by calling WikiPageParser.parseLine()
     * and aggregates parsed articles into chunks.
     *
     * @param line A line of input to be parsed
     */
    private void delegateParseLine(String line) {

        boolean articleComplete = pageParser.parseLine(line);

        if (articleComplete) {  // add to current chunk

            try {
                Tuple<Page, String> protoPage = pageParser.getProtoPage();
                nextArticleChunk.add(protoPage);

            } catch (IllegalAccessException exception) {
                log.error(exception.getMessage());
                throw new RuntimeException();

            }

            if (nextArticleChunk.size() >= chunkSize) {  // check if chunk has been filled - if so, add to ArrayList

                try {
                    articleChunks.put(new ArrayList<>(nextArticleChunk));

                } catch (InterruptedException exception) {
                    log.error("Thread interrupted while waiting for lock.");
                    return;

                }
                chunksAvailable.release();  // Signal that a chunk is available to another thread.

                nextArticleChunk.clear();  // clear next chunk, we'll be filling it again

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

        try (Stream<String> stream = Files.lines(Paths.get(infilePath))) {
            stream.forEachOrdered(this::delegateParseLine);

        }
        catch (IOException exception) {
            log.error("Couldn't get lines of file: " + infilePath);

        }


        if (nextArticleChunk.size() > 0) {  // No more lines to fill next chunk, take it as it is.
            try {
                articleChunks.put(new ArrayList<>(nextArticleChunk));

            } catch (InterruptedException exception) {
                log.error("Thread interrupted while waiting for lock.");
                return;

            }
            chunksAvailable.release();

        }

        threadCanEnd.set(true);
        log.info("All lines parsed.");

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

    // TODO figure out if JDoc uses docstring of interface
    @Override
    public boolean hasNext() {

        if(threadCanEnd.get()) return chunksAvailable.tryAcquire(); // Data might be left, but no more is coming in.

        try {
            chunksAvailable.acquire();
            return true;

        } catch (InterruptedException exception) {
            log.error("Interrupted while acquiring semaphore: " + exception.getMessage());
            return false;

        }

    }

    @Override
    public ArrayList<Tuple<Page, String>> next() {

        try {
            return articleChunks.take();

        } catch (InterruptedException exception) {
            log.error("Thread interrupted while waiting for lock.");
            return null;
        }

    }

}
