package com.prgpr.framework.database.transaction;

import com.prgpr.exceptions.NotInTransactionException;
import com.prgpr.framework.database.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by kito on 18.11.16.
 *
 * @author Kyle Rinfreschi
 * Docstrings are kept to a minimum, since all overridden methods are described in
 * detail in the superclass.
 */
public class SimpleTransactionManager implements TransactionManager {
    private static final Logger log = LogManager.getFormatterLogger(SimpleTransactionManager.class);

    private final TransactionFactory tf;
    private final LinkedBlockingDeque<Transaction> queue = new LinkedBlockingDeque<>();
    private int depth = 0;
    private Callback<Transaction> action = null;
    private static final Callback<Transaction> dominatingAction = Transaction::failure;

    public SimpleTransactionManager(TransactionFactory tf){
        this.tf = tf;
    }

    /**
     * Creates a new transaction and adds it to the queue
     */
    private void newTransaction(){
        Transaction tx = tf.createTransaction();
        queue.addFirst(tx);
    }

    @Override
    public <T> T execute(Callable<T> callable) {
        if(queue.isEmpty()){
            newTransaction();
        }

        T retVal = null;

        try {
            depth++;
            retVal = callable.call();
        } catch (Exception e){
            log.catching(e);
        } finally {
            depth--;
        }

        handleAction();

        return retVal;
    }

    @Override
    public void execute(Runnable runnable) {
        if(queue.isEmpty()){
            newTransaction();
        }

        try {
            depth++;
            runnable.run();
        } catch (Exception e){
            log.catching(e);
        } finally {
            depth--;
        }

        handleAction();
    }

    @Override
    public void success() {
        try {
            commit(Transaction::success);
        } catch (NotInTransactionException e) {
            log.catching(e);
        }
    }

    @Override
    public void failure()  {
        try {
            commit(Transaction::failure);
        } catch (NotInTransactionException e) {
            log.catching(e);
        }
    }

    /**
     * Commit the current transaction
     * @param cb the callback
     * @throws NotInTransactionException should not be called when not in a transaction
     */
    private void commit(Callback<Transaction> cb) throws NotInTransactionException {
        if(queue.isEmpty()){
            throw new NotInTransactionException();
        }

        if(depth > 0 && (action == null || action == dominatingAction)){
            action = cb;
            return;
        }

        action = null;

        try (Transaction tx = queue.pop()) {
            cb.call(tx);
        } catch (Exception e) {
            log.catching(e);
        }
    }

    @Override
    public void shutdown(){
        if(queue.isEmpty()){
            return;
        }

        queue.forEach((tx) -> {
            try {
                tx.close();
            } catch (Exception e){
                log.catching(e);
            }
        });

        queue.clear();
    }

    @Override
    public boolean isInTransaction() {
        return !queue.isEmpty();
    }

    /**
     * Only commit if we are at depth 0
     */
    private void handleAction() {
        if(depth > 0 || action == null){
            return;
        }

        try {
            commit(action);
        } catch (NotInTransactionException e) {
            log.catching(e);
        }
    }
}
