package com.user.UserService.handler;

public class UserIdNotFoundException extends Exception{
    public UserIdNotFoundException(String message){
        super(message);
    }
}
