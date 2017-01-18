package com.prgpr.exceptions;

/**
 * An Exception thrown when a task was not found.
 * @author Kyle Rinfreschi
 */
public class TaskNotFoundException extends RuntimeException {
    /**
     * A function to throw an exception.
     */
    public TaskNotFoundException(){}

    /**
     * A function to throw an exception with a message.
     * @param message the message to be displayed
     */
    public TaskNotFoundException(String message)
    {
        super(message);
    }
}
