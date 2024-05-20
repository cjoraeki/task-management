package com.niyogroup.taskmanagement.repository;

import com.niyogroup.taskmanagement.enums.TaskStatus;
import com.niyogroup.taskmanagement.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findTasksByUserId(Long user_id);

    List<Task> findTasksByUserIdAndTaskStatus(Long user_id, TaskStatus taskStatus);
}
