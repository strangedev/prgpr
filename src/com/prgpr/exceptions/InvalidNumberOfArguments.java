package com.prgpr.exceptions;

/**
 *
 * An Exception thrown when an invalid number of arguments was passed.
 *
 * @author Kyle Rinfreschi
 */

public class InvalidNumberOfArguments extends RuntimeException
{
    /**
     * A function to throw an exception.
     */
    public InvalidNumberOfArguments(){}

    /**
     * A function to throw an exception with a message.
     * @param message the message to be displayed
     */
    public InvalidNumberOfArguments(String message)
    {
        super(message);
    }
}
