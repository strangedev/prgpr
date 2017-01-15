package com.prgpr.exceptions;

/**
 * Created by kito on 15/01/17.
 */
public class TaskNotFoundException extends RuntimeException {
    /**
     * A function to trow an exception.
     */
    public TaskNotFoundException(){}

    /**
     * A function to trow an exception with a message.
     * @param message the message to be displayed
     */
    public TaskNotFoundException(String message)
    {
        super(message);
    }
}
