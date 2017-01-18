package com.prgpr.exceptions;

/**
 *
 * An Exception thrown when a missing dependency was discovered.
 *
 * @author Kyle Rinfreschi
 */
public class MissingDependencyException extends RuntimeException
{
    /**
     * A function to throw an exception.
     */
    public MissingDependencyException(){}

    /**
     * A function to throw an exception with a message.
     * @param message the message to be displayed
     */
    public MissingDependencyException(String message)
    {
        super(message);
    }
}