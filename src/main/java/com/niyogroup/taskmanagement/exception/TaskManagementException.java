package com.niyogroup.taskmanagement.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class TaskManagementException extends RuntimeException{
    private HttpStatus status;
    private String message;

    public TaskManagementException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
