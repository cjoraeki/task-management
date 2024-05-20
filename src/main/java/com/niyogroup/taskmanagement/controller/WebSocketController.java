package com.niyogroup.taskmanagement.controller;

import com.niyogroup.taskmanagement.dto.response.TaskResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/task")
    @SendTo("/topic/tasks")
    public TaskResponse sendTask(TaskResponse taskResponse) {
        System.out.println(taskResponse);
        return taskResponse;
    }
}

