package com.prgpr.exceptions;

/**
 * @author Kyle Rinfreschi
 *
 * An Exception thrown when an invalid number of arguments was passed to a Command
 */

public class InvalidNumberOfArguments extends Exception
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
