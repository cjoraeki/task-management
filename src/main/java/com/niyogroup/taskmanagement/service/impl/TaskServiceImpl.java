package com.niyogroup.taskmanagement.service.impl;

import com.niyogroup.taskmanagement.dto.request.TaskRequest;
import com.niyogroup.taskmanagement.dto.response.TaskResponse;
import com.niyogroup.taskmanagement.enums.TaskStatus;
import com.niyogroup.taskmanagement.exception.TaskNotFoundException;
import com.niyogroup.taskmanagement.model.Task;
import com.niyogroup.taskmanagement.model.User;
import com.niyogroup.taskmanagement.repository.TaskRepository;
import com.niyogroup.taskmanagement.repository.UserRepository;
import com.niyogroup.taskmanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;


    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal instanceof UserDetails ? ((UserDetails) principal).getUsername() : principal.toString();
        return userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public TaskResponse createTask(TaskRequest taskRequest) {
        User user = getCurrentUser();
        Task newTask = new Task();

        newTask.setTitle(taskRequest.getTitle());
        newTask.setDescription(taskRequest.getDescription());
        newTask.setUser(user);
        newTask.setCompletedAt(null);

        taskRepository.save(newTask);
        TaskResponse taskResponse = new TaskResponse();
        BeanUtils.copyProperties(newTask, taskResponse);

        messagingTemplate.convertAndSend("/topic/tasks", taskResponse);
        return taskResponse;
    }

    @Override
    public TaskResponse readTask(Long id) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        System.out.println(existingTask);
        TaskResponse taskResponse = new TaskResponse();
        BeanUtils.copyProperties(existingTask, taskResponse);

        messagingTemplate.convertAndSend("/topic/tasks", taskResponse);

        return taskResponse;
    }

    @Override
    public TaskResponse updatePendingTask(Long id, TaskRequest taskRequest) {
        Optional<Task> existingTask = taskRepository.findById(id);

        if (existingTask.isPresent()) {
            Task task = existingTask.get();
            task.setTitle(taskRequest.getTitle());
            task.setDescription(taskRequest.getDescription());
            task.setUpdatedAt(LocalDateTime.now());
            taskRepository.save(task);

            TaskResponse taskResponse = new TaskResponse();
            BeanUtils.copyProperties(existingTask, taskResponse);

            messagingTemplate.convertAndSend("/topic/tasks", taskResponse);

            return taskResponse;
        } else {
            return null;
        }
    }

    @Override
    public TaskResponse updateTaskToDone(Long id) {
        Optional<Task> existingTask = taskRepository.findById(id);

        if (existingTask.isPresent()) {
            Task task = existingTask.get();

            task.setTaskStatus(TaskStatus.DONE);
            task.setCompletedAt(LocalDateTime.now());

            taskRepository.save(task);

            TaskResponse taskResponse = new TaskResponse();
            BeanUtils.copyProperties(task, taskResponse);

            messagingTemplate.convertAndSend("/topic/tasks", taskResponse);

            return taskResponse;
        }
        return null;
    }

    @Override
    public TaskResponse cancelTask(Long id) {
        Optional<Task> existingTask = taskRepository.findById(id);

        if (existingTask.isPresent()) {
            Task task = existingTask.get();

            task.setTaskStatus(TaskStatus.CANCELED);
            task.setUpdatedAt(LocalDateTime.now());

            taskRepository.save(task);

            TaskResponse taskResponse = new TaskResponse();
            BeanUtils.copyProperties(task, taskResponse);

            messagingTemplate.convertAndSend("/topic/tasks", taskResponse);

            return taskResponse;
        }
        return null;
    }

    @Override
    public String deleteTask(Long id) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        taskRepository.delete(existingTask);
        messagingTemplate.convertAndSend("/topic/tasks", "Task deleted successfully");

        return "Task deleted successfully";
    }

    @Override
    public List<TaskResponse> getAllUserTasks() {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(getCurrentUser().getEmail())
                .orElseThrow(() -> new TaskNotFoundException("Tasks not found")));
        Long userId = user.get().getId();

        List<Task> existingTasks = taskRepository.findTasksByUserId(userId);

        List<TaskResponse> taskResponseList = new ArrayList<>();
        for (Task task : existingTasks){
            TaskResponse taskResponse = new TaskResponse();
            taskResponse.setTitle(task.getTitle());
            taskResponse.setDescription(task.getDescription());
            taskResponse.setTaskStatus(task.getTaskStatus());
            taskResponse.setCreatedAt(task.getCreatedAt());
            taskResponse.setCompletedAt(task.getCompletedAt());
            taskResponse.setUpdatedAt(task.getUpdatedAt());

            taskResponseList.add(taskResponse);
        }
        messagingTemplate.convertAndSend("/topic/tasks", taskResponseList);

        return taskResponseList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> getTasksByStatus(TaskStatus taskStatus) {
        User user = getCurrentUser();
        Long userId = user.getId();

        List<Task> tasks = taskRepository.findTasksByUserIdAndTaskStatus(userId, taskStatus);
        return tasks.stream().map(this::convertToTaskResponse).collect(Collectors.toList());
    }

    private TaskResponse convertToTaskResponse(Task task) {
        TaskResponse taskResponse = new TaskResponse();
        BeanUtils.copyProperties(task, taskResponse);

        messagingTemplate.convertAndSend("/topic/tasks", taskResponse);

        return taskResponse;
    }

}
