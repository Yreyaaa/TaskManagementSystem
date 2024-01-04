package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Client;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Task;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.repositories.TaskRepository;

@Service
@Transactional

public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }


    public Task findTaskByTitle(String title) {
        return taskRepository.findTaskByTitleIgnoreCase(title).orElse(null);
    }

    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }


    public void deleteTaskByTitle(String title) {
        taskRepository.deleteTaskByTitle(title);
    }

    public Page<Task> findAllTasks(Pageable pageable, Specification<Task> spec) {


        Page<Task> tasks = taskRepository.findAll(spec, pageable);

        return tasks;
    }

    public Page<Task> findAllTasks(Pageable pageable) {


        Page<Task> tasks = taskRepository.findAll(pageable);

        return tasks;
    }


    public Page<Task> findTasksByAuthor(Client client, Pageable pageable) {
        return taskRepository.findTasksByAuthor(client, pageable);
    }


    public Page<Task> findTasksByExecutor(Client client, Pageable pageable) {
        return taskRepository.findTasksByExecutor(client, pageable);
    }


}