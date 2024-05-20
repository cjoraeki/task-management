package com.niyogroup.taskmanagement.exception;

public class InvalidEmailOrPasswordException extends RuntimeException{
    public InvalidEmailOrPasswordException() {
        super("Invalid email or password");
    }

    public InvalidEmailOrPasswordException(String message){
        super(message);
    }
}
