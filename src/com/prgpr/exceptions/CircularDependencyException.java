package com.prgpr.exceptions;

/**
 *
 * An Exception thrown when an unknown command was requested from CommandBroker
 *
 * @author Kyle Rinfreschi
 */
public class CircularDependencyException extends RuntimeException
{
    /**
     * A function to trow an exception.
     */
    public CircularDependencyException(){}

    /**
     * A function to trow an exception with a message.
     * @param message the message to be displayed
     */
    public CircularDependencyException(String message)
    {
        super(message);
    }
}