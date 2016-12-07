package com.prgpr.exceptions;

/**
 * Created by strange on 10/21/16.
 * @author Noah Hummel
 *
 * Class of our own exeptions for Wikidatas
 */
public class MalformedWikidataException extends RuntimeException
{
    /**
     * A function to trow an exception.
     */
    public MalformedWikidataException(){}

    /**
     * A function to trow an exception with a message.
     */
    public MalformedWikidataException(String message)
    {
        super(message);
    }
}