package com.niyogroup.taskmanagement.exception;

import com.niyogroup.taskmanagement.dto.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException{
    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiResponse<String> handleUserAlreadyExistsException(UserAlreadyExistsException userAlreadyExistsException){
        logger.info(userAlreadyExistsException.getMessage());
        return new ApiResponse<>(userAlreadyExistsException.getMessage(), 409, null);
    }

    @ExceptionHandler(InvalidEmailOrPasswordException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiResponse<String> handleUserAlreadyExistsException(InvalidEmailOrPasswordException invalidEmailOrPasswordException){
        logger.info(invalidEmailOrPasswordException.getMessage());
        return new ApiResponse<>(invalidEmailOrPasswordException.getMessage(), 409, null);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiResponse<String> handleUserAlreadyExistsException(TaskNotFoundException taskNotFoundException){
        logger.info(taskNotFoundException.getMessage());
        return new ApiResponse<>(taskNotFoundException.getMessage(), 409, null);
    }
}
