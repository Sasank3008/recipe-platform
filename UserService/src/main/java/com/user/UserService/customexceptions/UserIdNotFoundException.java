package com.user.UserService.customexceptions;

public class UserIdNotFoundException extends Exception{
    public UserIdNotFoundException(String message){
        super(message);
    }
}
