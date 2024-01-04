package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.controllers;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.dto.CommentDTO;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.dto.TaskDTO;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Client;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Comment;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Status;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Task;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.repositories.ClientRepository;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.security.ClientDetails;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.services.CommentService;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.services.TaskService;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.util.TaskValidator;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.util.rsql.CustomRsqlVisitor;

import javax.validation.Valid;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final CommentService commentService;
    private final TaskValidator taskValidator;

    private final ModelMapper modelMapper;

    private final ClientRepository clientRepository;

    @Autowired
    public TaskController(TaskService taskService, CommentService commentService, TaskValidator taskValidator, ModelMapper modelMapper, ClientRepository clientRepository) {
        this.taskService = taskService;
        this.commentService = commentService;
        this.taskValidator = taskValidator;
        this.modelMapper = modelMapper;
        this.clientRepository = clientRepository;
    }

    @Operation(security = {@SecurityRequirement(name = "JWT")}, summary = "Показать все задачи", description = "Принимает JWT токен выводит список всех задач")

    @GetMapping("/getAllTasks")
    public ResponseEntity<Page<TaskDTO>> getAllTasks(@Parameter(description = "JSON объект со параметрами сортировки и пагинации, не обязателен к отправке") Pageable pageable, @RequestParam(value = "search", required = false) @Parameter(description = "RSQL запрос для фильтрации результатов, пример .../getAllTasks?search=title==fix*, не обязателен к отправке") String search) {
        Page<TaskDTO> tasks;
        if (search != null) {
            Node rootNode = new RSQLParser().parse(search);
            Specification<Task> spec = rootNode.accept(new CustomRsqlVisitor<Task>());


            tasks = taskService.findAllTasks(pageable, spec).map(this::convertToTaskDTO);

        } else tasks = taskService.findAllTasks(pageable).map(this::convertToTaskDTO);

        return ResponseEntity.ok(tasks);
    }

    @Operation(security = {@SecurityRequirement(name = "JWT")}, summary = "Создать задачу", description = "Принимает JSON с новой задачей и добавляет ее в базу")

    @PostMapping("/createTask")
    public Map<String, String> createTask(@Valid @RequestBody @Parameter(description = "JSON объект с новой задачей") TaskDTO taskDTO, BindingResult bindingResult) {


        Client currentClient = getCurentClient();

        if (currentClient == null) {
            return Map.of("message", " - для создания задачи, выполните авторизацию в системе");
        }

        Task task = convertToTask(taskDTO, currentClient);

        taskValidator.validate(task, bindingResult);
        if (bindingResult.hasErrors()) {
            return Map.of("message", "Задача с таким именем уже существует");
        }


        Task createdTask = taskService.createTask(task);
        return Map.of("message", "Задача успешно создана");
    }


    @GetMapping("/{title}")
    @Operation(security = {@SecurityRequirement(name = "JWT")}, summary = "Найти задачу по названию", description = "Возвращает задачу с указанным именем")

    public ResponseEntity<TaskDTO> getTaskByTitle(@PathVariable @Parameter(description = "Имя задачи для поиска") String title) {
        Task task = taskService.findTaskByTitle(title);
        if (task != null) {
            return ResponseEntity.ok(convertToTaskDTO(task));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(security = {@SecurityRequirement(name = "JWT")}, summary = "Найти задачи по автору", description = "Возвращает задачи указанного автора")
    @GetMapping("getTaskByAuthor/{authorName}")
    public ResponseEntity<Page<TaskDTO>> getTaskByAuthor(@PathVariable @Parameter(description = "Имя автора для поиска задач") String authorName, Pageable pageable) {
        Client author = clientRepository.findByClientName(authorName);

        if (author == null) {
            return ResponseEntity.notFound().build();
        }


        Page<TaskDTO> tasks;
        tasks = taskService.findTasksByAuthor(author, pageable).map(this::convertToTaskDTO);

        return ResponseEntity.ok(tasks);


    }


    @GetMapping("getTaskByExecutor/{executorName}")
    @Operation(security = {@SecurityRequirement(name = "JWT")}, summary = "Найти задачи по исполнителю", description = "Возвращает все задачи указанного исполнителя")

    public ResponseEntity<Page<TaskDTO>> getTaskByExecutor(@PathVariable @Parameter(description = "Имя исполнителя для поиска задач") String executorName, Pageable pageable) {


        Client executor = clientRepository.findByClientName(executorName);
        if (executor == null) {
            return ResponseEntity.notFound().build();
        }


        Page<TaskDTO> tasks;
        tasks = taskService.findTasksByExecutor(executor, pageable).map(this::convertToTaskDTO);

        return ResponseEntity.ok(tasks);
    }

    @Operation(security = {@SecurityRequirement(name = "JWT")}, summary = "Изменить статус задачи", description = "Исполнитель может менять статус своих задач")
    @PatchMapping("/{title}/updateStatus/{newStatus}")
    public Map<String, String> updateTaskStatus(@PathVariable @Parameter(description = "Название задачи для изменения статуса") String title, @PathVariable @Parameter(description = "Новый статус задачи: {IN_PROGRESS, DONE, PAUSED}") String newStatus) {
        Client currentClient = getCurentClient();

        if (currentClient == null) {
            return Map.of("message", " - для изменения статуса задачи, выполните авторизацию в системе");
        }


        Task taskToBeUpdatedStatus = taskService.findTaskByTitle(title);

        if (taskToBeUpdatedStatus == null) {
            return Map.of("message", title + " - Не найдено задачи с таким заголовком");
        }


        if (!taskToBeUpdatedStatus.getExecutor().getClientName().equals(currentClient.getClientName())) {
            return Map.of("message", title + " - У вас нет прав для изменения статуса этой задачи");

        }

        try {
            taskToBeUpdatedStatus.setStatus(Status.valueOf(newStatus.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Map.of("message", title + " - Введен не корректный статус задачи");
        }

        Task updatedTask = taskService.updateTask(taskToBeUpdatedStatus);
        if (updatedTask != null) {
            return Map.of("message", title + " - статус задачи успешно обновлен успешно");
        } else {
            return Map.of("message", title + " - обновление статуса не выполнено");
        }
    }


    @Operation(security = {@SecurityRequirement(name = "JWT")}, summary = "Редактировать задачу", description = "Принимает JSON с обновленной версией задачи, только автор задачи может редектировать ее")

    @PatchMapping("/{title}/update")
    public Map<String, String> updateTask(@PathVariable @Parameter(description = "Название задачи для изменения") String title, @Valid @RequestBody @Parameter(description = "JSON с обновленной версией задачи") TaskDTO taskDTO, BindingResult bindingResult) {
        Client currentClient = getCurentClient();
        if (currentClient == null) {
            return Map.of("message", " - для изменения статуса задачи, выполните авторизацию в системе");
        }


        Task taskToBeUpdated = taskService.findTaskByTitle(title);

        if (taskToBeUpdated == null) {
            return Map.of("message", title + " - Не найдено задачи с таким заголовком");
        }


        if (!taskToBeUpdated.getAuthor().getClientName().equals(currentClient.getClientName())) {
            return Map.of("message", title + " - У вас нет прав для редактирования этой задачи");

        }

        Task updateTask = convertToTask(taskDTO, taskToBeUpdated.getAuthor());

        updateTask.setId(taskToBeUpdated.getId());
        Task updatedTask = taskService.updateTask(updateTask);
        if (updatedTask != null) {
            return Map.of("message", title + " - задача успешно обновлена");
        } else {
            return Map.of("message", title + " - обновление не выполнено");
        }
    }

    @Operation(security = {@SecurityRequirement(name = "JWT")}, summary = "Добавить комментарий к задаче", description = "Принимает JSON с коментарием к указанной задаче")
    @PatchMapping("/{title}/addComment")
    public Map<String, String> addCommentToTask(@PathVariable @Parameter(description = "Название задачи для комментрования") String title, @Valid @RequestBody  @Parameter(description = "JSON с текстом комментария, автор определяется автоматически")CommentDTO commentDTO, BindingResult bindingResult) {


        Client currentClient = getCurentClient();
        if (currentClient == null) {
            return Map.of("message", " - для добавления комментариев, выполните авторизацию в системе");
        }

        Task taskToBeAddComment = taskService.findTaskByTitle(title);
        if (taskToBeAddComment == null) {
            return Map.of("message", title + " - Не найдено задачи с таким заголовком");
        }

        Comment comment = convertToComment(commentDTO, taskToBeAddComment, currentClient);


        Comment addedComment = commentService.AddComment(comment);
        if (addedComment != null) {
            return Map.of("message", title + " - коментарий добавлен успешно");
        } else {
            return Map.of("message", title + " -  комментарий не был добавлен");
        }
    }


    @DeleteMapping("/{title}/delete")
    @Operation(security = {@SecurityRequirement(name = "JWT")}, summary = "Удалить задачу", description = "Удаляет указанную задачу, только автор задачи может выполнить ее удаление")

    public Map<String, String> deleteTask(@PathVariable String title) {
        Client currentClient = getCurentClient();
        if (currentClient == null) {
            return Map.of("message", " - для удаления задачи, выполните авторизацию в системе");
        }


        Task taskToBeDeleted = taskService.findTaskByTitle(title);

        if (taskToBeDeleted == null) {
            return Map.of("message", title + " - Не найдено задачи с таким заголовком");
        }


        if (!taskToBeDeleted.getAuthor().getClientName().equals(currentClient.getClientName())) {
            return Map.of("message", title + " - У вас нет прав для удаления этой задачи");

        }
        taskService.deleteTaskByTitle(title);

        return Map.of("message", title + " - удаление выполнено успешно");

    }


    public Task convertToTask(TaskDTO taskDTO, Client currentClient) {
        Task task = this.modelMapper.map(taskDTO, Task.class);

        task.setAuthor(currentClient);

        task.setExecutor(clientRepository.findByClientName(taskDTO.getExecutor()));


        return task;
    }

    private Client getCurentClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        Client currentClient = clientRepository.findByClientName(clientDetails.getUsername());
        return currentClient;
    }

    private Comment convertToComment(CommentDTO commentDTO, Task task, Client client) {

        Comment comment = this.modelMapper.map(commentDTO, Comment.class);

        comment.setAuthor(client);
        comment.setTask(task);
        return comment;
    }

    public TaskDTO convertToTaskDTO(Task task) {

        TaskDTO taskDTO = this.modelMapper.map(task, TaskDTO.class);


        return taskDTO;


    }
}