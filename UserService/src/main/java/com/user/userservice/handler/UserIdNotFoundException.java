package com.user.userservice.handler;

public class UserIdNotFoundException extends Exception{
    public UserIdNotFoundException(String message){
        super(message);
    }
}
