package com.user.UserService.customexceptions;

public class EmailNotFoundException extends Exception{
    public EmailNotFoundException(String message){
        super(message);
    }
}
