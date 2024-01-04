package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Priority;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Status;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class TaskDTO {


    @NotEmpty(message = "Параметр title не может быть пустым")
    @Size(min = 1, max = 50, message = "Параметр title должен быть в пределах 1-50 символов")
    @Schema(description = "Название задачи, длинна 1-50 символов")

    private String title;

    @Size(max = 200, message = "Параметр description не должен превышать 200")
    @Schema(description = "Описание задачи, не более 200 символов")

    private String description;

    @Schema(description = "Статус задачи:  {IN_PROGRESS, DONE, PAUSED}")

    private Status status;

    @Schema(description = "Приоритет задачи:  {LOW, MEDIUM, HIGH}")

    private Priority priority;
    @Schema(description = "Имя автора цадачи, выставляется автоматически по владельцу текущего JWT токена", accessMode = Schema.AccessMode.READ_ONLY)

    private String author;

    @Schema(description = "Имя исполнителя задачи")

    private String executor;

    @Schema(description = "Комментарии к задаче", accessMode = Schema.AccessMode.READ_ONLY)

    private List<String> comments;

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }
}
