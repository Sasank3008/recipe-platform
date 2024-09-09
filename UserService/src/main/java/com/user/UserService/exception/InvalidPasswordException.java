package com.user.UserService.exception;

public class InvalidPasswordException extends  Exception{
    public  InvalidPasswordException(String message){
        super(message);
    }
}
