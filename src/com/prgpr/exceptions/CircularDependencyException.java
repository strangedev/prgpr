package com.prgpr.exceptions;

/**
 *
 * An Exception thrown when a circular dependency is found.
 *
 * @author Kyle Rinfreschi
 */
public class CircularDependencyException extends RuntimeException
{
    /**
     * A function to throw an exception.
     */
    public CircularDependencyException(){}

    /**
     * A function to throw an exception with a message.
     * @param message the message to be displayed
     */
    public CircularDependencyException(String message)
    {
        super(message);
    }
}