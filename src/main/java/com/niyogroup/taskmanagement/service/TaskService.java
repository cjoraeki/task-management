package com.niyogroup.taskmanagement.service;

import com.niyogroup.taskmanagement.dto.request.TaskRequest;
import com.niyogroup.taskmanagement.dto.response.TaskResponse;
import com.niyogroup.taskmanagement.enums.TaskStatus;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(TaskRequest taskRequest);
    TaskResponse readTask(Long id);
    TaskResponse updatePendingTask(Long id, TaskRequest taskRequest);
    TaskResponse updateTaskToDone(Long id);
    TaskResponse cancelTask(Long id);
    String deleteTask(Long id);
    List<TaskResponse> getAllUserTasks();
    List<TaskResponse> getTasksByStatus(TaskStatus taskStatus);

}
