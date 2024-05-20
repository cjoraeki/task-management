package com.niyogroup.taskmanagement.controller;

import com.niyogroup.taskmanagement.dto.request.TaskRequest;
import com.niyogroup.taskmanagement.dto.response.TaskResponse;
import com.niyogroup.taskmanagement.enums.TaskStatus;
import com.niyogroup.taskmanagement.service.impl.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/api/v1/tasks")
public class TaskController {

    private final TaskServiceImpl taskService;

    @PostMapping("/create")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest taskRequest) {
        TaskResponse taskResponse = taskService.createTask(taskRequest);
        return ResponseEntity.ok(taskResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> readTask(@PathVariable Long id) {
        TaskResponse response = taskService.readTask(id);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest taskRequest) {
        if (taskRequest.getTaskStatus() != null && !taskRequest.getTaskStatus().equals(TaskStatus.PENDING)) {
            return new ResponseEntity<>("Task update only allowed for PENDING tasks", HttpStatus.BAD_REQUEST);
        }
        TaskResponse response = taskService.updatePendingTask(id, taskRequest);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/done")
    public ResponseEntity<TaskResponse> markTaskDone(@PathVariable Long id) {
        TaskResponse response = taskService.updateTaskToDone(id);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<TaskResponse> cancelTask(@PathVariable Long id) {
        TaskResponse response = taskService.cancelTask(id);
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.deleteTask(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(){
        return new ResponseEntity<>(taskService.getAllUserTasks(), HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@RequestParam TaskStatus status) {
        List<TaskResponse> taskResponses = taskService.getTasksByStatus(status);
        if (taskResponses.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(taskResponses);
    }
}
