package com.user.UserService.handler;

public class EmailNotFoundException extends Exception{
    public EmailNotFoundException(String message){
        super(message);
    }
}
