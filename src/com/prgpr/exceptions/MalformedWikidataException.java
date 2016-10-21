package com.prgpr.exceptions;

/**
 * Created by strange on 10/21/16.
 *
 */
public class MalformedWikidataException extends Exception
{
    public MalformedWikidataException(){}

    public MalformedWikidataException(String message)
    {
        super(message);
    }
}