package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Comment;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Task;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Task> findCommentsByTask(Task task);


}
