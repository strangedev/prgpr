package com.prgpr.exceptions;

/**
 * Created by kito on 08.12.16.
 *
 * An Exception thrown when a database action is performed when not inside of a transaction.
 *
 * @author Kyle Rinfreschi
 */
public class NotInTransactionException extends RuntimeException {
}
