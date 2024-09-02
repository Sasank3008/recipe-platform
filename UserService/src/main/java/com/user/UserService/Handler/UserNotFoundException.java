package com.user.UserService.Handler;

import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.Super;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message) {
        super(message);
    }
}
