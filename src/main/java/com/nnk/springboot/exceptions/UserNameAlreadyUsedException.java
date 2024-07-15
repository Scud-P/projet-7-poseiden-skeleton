package com.nnk.springboot.exceptions;

public class UserNameAlreadyUsedException extends RuntimeException {

    public UserNameAlreadyUsedException(String message) {
        super(message);
    }
}
