package com.prgpr.exceptions;

/**
 *
 * An Exception thrown when an unknown command was requested from CommandBroker
 *
 * @author Kyle Rinfreschi
 */
public class CommandNotFound extends Exception
{
    /**
     * A function to trow an exception.
     */
    public CommandNotFound(){}

    /**
     * A function to trow an exception with a message.
     * @param message the message to be displayed
     */
    public CommandNotFound(String message)
    {
        super(message);
    }
}