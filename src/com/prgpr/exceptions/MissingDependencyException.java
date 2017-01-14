package com.prgpr.exceptions;

/**
 *
 * An Exception thrown when an unknown command was requested from CommandBroker
 *
 * @author Kyle Rinfreschi
 */
public class MissingDependencyException extends RuntimeException
{
    /**
     * A function to trow an exception.
     */
    public MissingDependencyException(){}

    /**
     * A function to trow an exception with a message.
     * @param message the message to be displayed
     */
    public MissingDependencyException(String message)
    {
        super(message);
    }
}