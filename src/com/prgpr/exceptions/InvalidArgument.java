package com.prgpr.exceptions;

/**
 *
 * An Exception thrown when an invalid argument was passed to a command
 *
 * @author Kyle Rinfreschi
 */
public class InvalidArgument extends Exception
{
    /**
     * A function to trow an exception.
     */
    public InvalidArgument(){}

    /**
     * A function to trow an exception with a message.
     * @param message the message to be displayed
     */
    public InvalidArgument(String message)
    {
        super(message);
    }
}
