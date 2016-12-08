package com.prgpr.framework.database.transaction;

import com.prgpr.exceptions.NotInTransactionException;
import com.prgpr.framework.database.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Stack;
import java.util.concurrent.Callable;

/**
 * Created by kito on 18.11.16.
 */
public class TransactionManager {
    private static final Logger log = LogManager.getFormatterLogger(TransactionManager.class);

    private static final Stack<Transaction> transactions = new Stack<>();
    private final TransactionFactory tf;

    public TransactionManager(TransactionFactory tf){
        this.tf = tf;
    }

    private void newTransaction(){
        transactions.push(tf.createTransaction());
    }

    private void currentTransaction(){
        if(transactions.empty()){
            newTransaction();
        }
    }

    public <T> T execute(Callable<T> callable) {
        try{
            currentTransaction();
            return callable.call();
        } catch (Exception e){
            e.printStackTrace();
            log.error(e);
        }
        return null;
    }

    public void execute(Runnable runnable) {
        try{
            currentTransaction();
            runnable.run();
        } catch (Exception e){
            e.printStackTrace();
            log.error(e);
        }
    }

    public void success() throws NotInTransactionException {
        commit(Transaction::success);
    }

    public void failure() throws NotInTransactionException {
        commit(Transaction::failure);
    }

    private void commit(Callback<Transaction> cb) throws NotInTransactionException {
        if(transactions.empty()){
            throw new NotInTransactionException();
        }

        try (Transaction tx = transactions.pop()) {
            cb.call(tx);
        } catch (Exception e) {
            log.error(e);
        }
    }

    public void shutdown(){
        if(transactions.empty()){
            return;
        }

        transactions.forEach((tx) -> {
            try {
                tx.close();
            } catch (Exception e){
                log.catching(e);
            }
        });

        transactions.clear();
    }
}
