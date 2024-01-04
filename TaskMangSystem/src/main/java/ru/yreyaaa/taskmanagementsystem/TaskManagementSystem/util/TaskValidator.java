package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Task;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.services.TaskService;

@Component
public class TaskValidator implements Validator {

    private final TaskService taskService;


    @Autowired
    public TaskValidator(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Task.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Task task = (Task) o;

        if (taskService.findTaskByTitle(task.getTitle()) == null) {
            return;
        }
        errors.rejectValue("title", "", "Задача с таким именем уже существует");
    }
}