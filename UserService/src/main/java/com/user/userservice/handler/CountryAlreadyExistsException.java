package com.user.userservice.handler;

public class CountryAlreadyExistsException extends Exception
{
    public CountryAlreadyExistsException(String message)
    {
        super(message);
    }
}
