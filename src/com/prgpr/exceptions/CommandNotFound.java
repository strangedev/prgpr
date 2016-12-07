package com.prgpr.exceptions;

/**
 * @author Kyle Rinfreschi
 *
 * A Class to catch exceptions where the command was not found
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
