package com.prgpr.exceptions;

/**
 *
 * An Exception thrown when an invalid number of arguments was passed to a Command
 *
 * @author Kyle Rinfreschi
 */

public class InvalidNumberOfArguments extends RuntimeException
{
    /**
     * A function to trow an exception.
     */
    public InvalidNumberOfArguments(){}

    /**
     * A function to trow an exception with a message.
     * @param message the message to be displayed
     */
    public InvalidNumberOfArguments(String message)
    {
        super(message);
    }
}
