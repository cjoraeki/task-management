package com.niyogroup.taskmanagement.service.impl;

import com.niyogroup.taskmanagement.dto.request.TaskRequest;
import com.niyogroup.taskmanagement.dto.response.TaskResponse;
import com.niyogroup.taskmanagement.model.Task;
import com.niyogroup.taskmanagement.model.User;
import com.niyogroup.taskmanagement.repository.TaskRepository;
import com.niyogroup.taskmanagement.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    public void testGetCurrentUser_Success() {
        // Given
        String expectedEmail = "user@example.com";
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn(expectedEmail);
        Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(userDetails);

        // When
        User currentUser = taskService.getCurrentUser();

        // Then
        assertNotNull(currentUser);
        assertEquals(expectedEmail, currentUser.getEmail());
    }

    @Test
    public void testCreateTask() {
        // Given
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTitle("Test Task");
        taskRequest.setDescription("Test Description");

        User user = new User();
        user.setEmail("user@example.com");
        Mockito.mock((SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        Mockito.mock(userRepository.findByEmail(user.getEmail()));

        Task newTask = new Task();
        newTask.setTitle(taskRequest.getTitle());
        newTask.setDescription(taskRequest.getDescription());
        newTask.setUser(user);
        Mockito.when(taskRepository.save(newTask)).thenReturn(newTask);

        // When
        TaskResponse taskResponse = taskService.createTask(taskRequest);

        // Then
        assertEquals("Test Task", taskResponse.getTitle());
        assertEquals(newTask.getDescription(), taskResponse.getDescription());
        Mockito.verify(messagingTemplate).convertAndSend("/topic/tasks", taskResponse);
    }
}