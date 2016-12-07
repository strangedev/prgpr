package com.prgpr.exceptions;

/**
 * @author Kyle Rinfreschi
 *
 * A Class to catch exceptions where the command was not found
 */

public class InvalidNumberOfArguments extends Exception
{
    /**
     * A function to trow an exception.
     */
    public InvalidNumberOfArguments(){}

    /**
     * A function to trow an exception with a message.
     */
    public InvalidNumberOfArguments(String message)
    {
        super(message);
    }
}
