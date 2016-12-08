package com.prgpr.framework.database.transaction;

import com.prgpr.exceptions.NotInTransactionException;
import com.prgpr.framework.database.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by kito on 18.11.16.
 */
public class SimpleTransactionManager implements TransactionManager {
    private static final Logger log = LogManager.getFormatterLogger(SimpleTransactionManager.class);

    private final TransactionFactory tf;
    private final LinkedBlockingDeque<Transaction> queue = new LinkedBlockingDeque<>();
    private int depth = 0;
    private Callback<Transaction> transactionAction = null;

    public SimpleTransactionManager(TransactionFactory tf){
        this.tf = tf;
    }

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

        handleTransactionAction();

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

        handleTransactionAction();
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

    private void commit(Callback<Transaction> cb) throws NotInTransactionException {
        if(queue.isEmpty()){
            throw new NotInTransactionException();
        }

        if(depth > 0){
            transactionAction = cb;
            return;
        }

        transactionAction = null;

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
    public boolean inTransaction() {
        return !queue.isEmpty();
    }

    private void handleTransactionAction() {
        if(depth > 0 || transactionAction == null){
            return;
        }

        try {
            commit(transactionAction);
        } catch (NotInTransactionException e) {
            log.catching(e);
        }
    }
}
