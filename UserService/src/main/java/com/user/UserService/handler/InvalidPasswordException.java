package com.user.UserService.handler;

public class InvalidPasswordException extends  Exception{
    public  InvalidPasswordException(String message){
        super(message);
    }
}
