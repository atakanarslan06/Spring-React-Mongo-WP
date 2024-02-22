package com.wp.chatapp.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String  message){
        super(message);
    }
}
