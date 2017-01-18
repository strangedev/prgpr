package com.prgpr.exceptions;

/**
 *
 * An Exception thrown when an unknown command was requested.
 *
 * @author Kyle Rinfreschi
 */
public class CommandNotFound extends RuntimeException
{
    /**
     * A function to throw an exception.
     */
    public CommandNotFound(){}

    /**
     * A function to throw an exception with a message.
     * @param message the message to be displayed
     */
    public CommandNotFound(String message)
    {
        super(message);
    }
}