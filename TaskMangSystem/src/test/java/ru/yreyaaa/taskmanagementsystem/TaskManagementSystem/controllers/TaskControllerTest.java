package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.dto.ClientDTO;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.dto.CommentDTO;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.dto.TaskDTO;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Client;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Task;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.repositories.ClientRepository;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.security.JWTUtil;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.services.ClientDetailsService;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.services.RegistrationService;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.services.TaskService;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.util.ClientValidator;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TaskControllerTest {
    @Autowired
    ClientValidator clientValidator;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ClientDetailsService clientDetailsService;
    @Autowired

    TaskService taskService;


    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired

    JWTUtil jwtUtil;
    @Mock
    BindingResult bindingResult;
    ClientController clientController;

    @Autowired
    TaskController taskController;

    TaskDTO taskDTO;
    ClientDTO clientDTO;
    ObjectWriter ow;
    @Autowired
    private MockMvc mockMvc;


    @AfterEach
    void tearDown() {
        registrationService.deleteClientByClientName(clientDTO.getClientName());
        taskService.deleteTaskByTitle(taskDTO.getTitle());

    }

    @BeforeEach
    void setUp() {


        clientController = new ClientController(clientValidator, clientRepository, registrationService, passwordEncoder, jwtUtil, modelMapper, authenticationManager);
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        clientDTO = new ClientDTO();
        clientDTO.setClientName("Yreya");
        clientDTO.setPassword("1213yreyaa");
        taskDTO = new TaskDTO();

        taskDTO.setTitle("Learn Java");
        taskDTO.setAuthor(clientDTO.getClientName());

        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testCreateTask() throws Exception {
        Map<String, String> result = clientController.registration(clientDTO, bindingResult);
        String json = ow.writeValueAsString(taskDTO);
        var request = MockMvcRequestBuilders.post("/tasks/createTask").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.content().json(ow.writeValueAsString(Map.of("message", "Задача успешно создана"))));
        registrationService.deleteClientByClientName(clientDTO.getClientName());

    }


    @Test
    void testGetAllTasks() throws Exception {
        Map<String, String> result = clientController.registration(clientDTO, bindingResult);
        String json = ow.writeValueAsString(taskDTO);
        var request = MockMvcRequestBuilders.post("/tasks/createTask").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request);
        request = MockMvcRequestBuilders.get("/tasks/getAllTasks").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
        registrationService.deleteClientByClientName(clientDTO.getClientName());

    }

    @Test
    void testGetTaskByTitle() throws Exception {
        Map<String, String> result = clientController.registration(clientDTO, bindingResult);
        String json = ow.writeValueAsString(taskDTO);
        var request = MockMvcRequestBuilders.post("/tasks/createTask").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request);
        request = MockMvcRequestBuilders.get("/tasks/Learn Java").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
        registrationService.deleteClientByClientName(clientDTO.getClientName());

    }


    @Test
    void testGetTaskByAuthor() throws Exception {
        Map<String, String> result = clientController.registration(clientDTO, bindingResult);
        String json = ow.writeValueAsString(taskDTO);
        var request = MockMvcRequestBuilders.post("/tasks/createTask").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request);
        request = MockMvcRequestBuilders.get("/tasks/getTaskByAuthor/Yreya").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
        registrationService.deleteClientByClientName(clientDTO.getClientName());

    }

    @Test
    void testGetTaskByExecutor() throws Exception {
        Map<String, String> result = clientController.registration(clientDTO, bindingResult);
        String json = ow.writeValueAsString(taskDTO);
        var request = MockMvcRequestBuilders.post("/tasks/createTask").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request);
        request = MockMvcRequestBuilders.get("/tasks/getTaskByExecutor/Yreya").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk());
        registrationService.deleteClientByClientName(clientDTO.getClientName());

    }


    @Test
    void testUpdateTask() throws Exception {
        Map<String, String> result = clientController.registration(clientDTO, bindingResult);
        String json = ow.writeValueAsString(taskDTO);
        var request = MockMvcRequestBuilders.post("/tasks/createTask").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request);

        taskDTO.setDescription("Simple java lessons");


        request = MockMvcRequestBuilders.patch("/tasks/Learn Java/update").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.content().json(ow.writeValueAsString(Map.of("message", taskDTO.getTitle() + " - задача успешно обновлена"))));

        registrationService.deleteClientByClientName(clientDTO.getClientName());

    }

    @Test
    void testUpdateTaskStatus() throws Exception {
        Map<String, String> result = clientController.registration(clientDTO, bindingResult);
        taskDTO.setExecutor(clientDTO.getClientName());
        String json = ow.writeValueAsString(taskDTO);
        var request = MockMvcRequestBuilders.post("/tasks/createTask").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request);
        request = MockMvcRequestBuilders.patch("/tasks/Learn Java/updateStatus/DONE").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(MockMvcResultMatchers.content().json(ow.writeValueAsString(Map.of("message", taskDTO.getTitle() + " - статус задачи успешно обновлен успешно"))));

        registrationService.deleteClientByClientName(clientDTO.getClientName());

    }

    @Test
    void testAddCommentToTask() throws Exception {
        Map<String, String> result = clientController.registration(clientDTO, bindingResult);
        taskDTO.setExecutor(clientDTO.getClientName());
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setTextComment("I don't do it");
        String json = ow.writeValueAsString(taskDTO);
        var request = MockMvcRequestBuilders.post("/tasks/createTask").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request);
        json = ow.writeValueAsString(commentDTO);
        request = MockMvcRequestBuilders.patch("/tasks/Learn Java/addComment").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(MockMvcResultMatchers.content().json(ow.writeValueAsString(Map.of("message", taskDTO.getTitle() + " - коментарий добавлен успешно"))));
        registrationService.deleteClientByClientName(clientDTO.getClientName());

    }

    @Test
    void testDeleteTask() throws Exception {
        Map<String, String> result = clientController.registration(clientDTO, bindingResult);

        String json = ow.writeValueAsString(taskDTO);
        var request = MockMvcRequestBuilders.post("/tasks/createTask").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request);

        request = MockMvcRequestBuilders.delete("/tasks/Learn Java/delete").content(json).accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(MockMvcResultMatchers.content().json(ow.writeValueAsString(Map.of("message", taskDTO.getTitle() + " - удаление выполнено успешно"))));
        registrationService.deleteClientByClientName(clientDTO.getClientName());

    }

    @Test
    void testConvertToTask() {
        Task result = taskController.convertToTask(taskDTO, new Client());
        Task task = new Task();
        task.setTitle("Learn Java");
        Assertions.assertEquals(task.getTitle(), result.getTitle());
    }

    @Test
    void testConvertToTaskDTO() {
        Task task = new Task();
        task.setTitle("Learn Java");

        TaskDTO result = taskController.convertToTaskDTO(task);

        Assertions.assertEquals(result.getTitle(), task.getTitle());
    }


}

