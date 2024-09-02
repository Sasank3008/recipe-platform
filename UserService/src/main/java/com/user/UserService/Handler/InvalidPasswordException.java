package com.user.UserService.Handler;

public class InvalidPasswordException extends  Exception{
    public  InvalidPasswordException(String message){
        super(message);
    }
}
