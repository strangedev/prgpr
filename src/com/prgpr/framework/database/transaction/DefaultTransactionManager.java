package com.prgpr.framework.database.transaction;

import com.prgpr.framework.database.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Callable;

/**
 * @author Kyle Rinfreschi
 */
public class DefaultTransactionManager implements TransactionManager {

    private static final Logger log = LogManager.getFormatterLogger(DefaultTransactionManager.class);

    private final TransactionFactory tf;
    private Transaction currentTx;
    private static final Callback<Transaction> txFailure = Transaction::failure;
    private Callback<Transaction> txAction;

    private int depth = 0;
    private int transactionCount = 0;
    private int batchSize = -1;

    public DefaultTransactionManager(TransactionFactory tf){
        this.tf = tf;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public <T> T execute(Callable<T> callable) {
        newTransaction();

        T retVal = null;
        try {
            depth++;
            retVal = callable.call();
        } catch (Exception e) {
            log.catching(e);
        } finally {
            depth--;
        }

        if(depth == 0){
            batchCommit();
        }

        return retVal;
    }

    @Override
    public void success() {
        if(this.txAction == txFailure)
            return;

        commit(Transaction::success);
    }

    @Override
    public void failure() {
        commit(Transaction::failure);
    }

    @Override
    public void closeOpenTransactions() {
        if(this.currentTx == null){
            return;
        }

        try {
            this.currentTx.close();
        } catch (Exception e) {
            log.catching(e);
        } finally {
            this.currentTx = null;
        }

    }

    private void newTransaction(){
        if(this.currentTx == null) {
            this.currentTx = this.tf.createTransaction();
        }

        this.transactionCount++;
    }

    private void batchCommit() {
        if(this.batchSize == -1)
            return;

        if(this.transactionCount >= this.batchSize){
            success();
            log.debug("committing");
        }
    }

    private void commit(Callback<Transaction> txAction) {
        this.txAction = txAction;

        if(depth > 0 || currentTx == null){
            return;
        }

        txAction.call(currentTx);

        try {
            currentTx.close();
        } catch (Exception e) {
            log.catching(e);
        }

        this.currentTx = null;
        this.txAction = null;
        this.transactionCount = 0;
    }
}
