package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Comment;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.repositories.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment AddComment(Comment comment) {
        return commentRepository.save(comment);
    }


}