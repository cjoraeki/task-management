package com.niyogroup.taskmanagement.exception;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException() {
        super("Invalid email or password");
    }

    public TaskNotFoundException(String message){
        super(message);
    }
}
