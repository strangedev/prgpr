package com.prgpr.exceptions;

/**
 * @author Kyle Rinfreschi
 *
 * An Exception thrown when an unknown command was requested from CommandBroker
 */
public class CommandNotFound extends Exception
{
    /**
     * A function to trow an exception.
     */
    public CommandNotFound(){}

    /**
     * A function to trow an exception with a message.
     */
    public CommandNotFound(String message)
    {
        super(message);
    }
}