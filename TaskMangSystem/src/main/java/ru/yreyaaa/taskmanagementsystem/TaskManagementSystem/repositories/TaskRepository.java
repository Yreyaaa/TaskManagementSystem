package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Client;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Task;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {


    Page<Task> findTasksByAuthor(Client client, Pageable pageable);

    Page<Task> findTasksByAuthor(Client client, Pageable pageable, Specification spec);


    Page<Task> findTasksByExecutor(Client client, Pageable pageable);

    Page<Task> findAllByExecutor(Client client, Pageable pageable, Specification spec);

    Page<Task> findAll(Specification<Task> spec, Pageable pageable);


    Page<Task> findAll(Pageable pageable);


    void deleteTaskByTitle(String title);

    Optional<Task> findTaskByTitleIgnoreCase(String title);


}
