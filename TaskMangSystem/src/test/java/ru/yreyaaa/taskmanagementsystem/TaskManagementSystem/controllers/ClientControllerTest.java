package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Client;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.repositories.ClientRepository;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.security.JWTUtil;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.services.ClientDetailsService;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.services.RegistrationService;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.util.ClientValidator;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class ClientControllerTest {
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
    ModelMapper modelMapper;
    @Autowired
    AuthenticationManager authenticationManager;

    ObjectWriter ow;

    @Autowired
    JWTUtil jwtUtil;
    @Mock
    BindingResult bindingResult;
    ClientController clientController;
    ClientDTO clientDTO;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {


        clientController = new ClientController(clientValidator, clientRepository, registrationService, passwordEncoder, jwtUtil, modelMapper, authenticationManager);
        ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        clientDTO = new ClientDTO();
        clientDTO.setClientName("Yreya");
        clientDTO.setPassword("1213yreyaa");
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void testPerformLogin() {
        clientController.registration(clientDTO, bindingResult);

        Map<String, String> result = clientController.performLogin(clientDTO, bindingResult);
        Assertions.assertEquals(Map.of("jwt-token", result.get("jwt-token")), result);
        registrationService.deleteClientByClientName(clientDTO.getClientName());

    }

    @Test
    void testRegistration() {
        Map<String, String> result = clientController.registration(clientDTO, bindingResult);
        Assertions.assertEquals(Map.of("jwt-token", result.get("jwt-token")), result);
        registrationService.deleteClientByClientName(clientDTO.getClientName());
    }

    @Test
    void testGetRegistration() throws Exception {
        var request = MockMvcRequestBuilders.get("/registration");


        mockMvc.perform(request).andExpect(MockMvcResultMatchers.content().json(ow.writeValueAsString(Map.of("clientName: \"<введите имя пользователя>\"", "password: \"<введите имя пароль>\"\""))));

    }

    @Test
    void testConvertToClient() {
        Client result = clientController.convertToClient(clientDTO);
        Client client = new Client();
        client.setClientName("Yreya");
        client.setPassword("1213yreyaa");
        Assertions.assertEquals(client.getClientName(), result.getClientName());
    }

    @Test
    void testDeleteClient() throws Exception {
        Map<String, String> result = clientController.registration(clientDTO, bindingResult);


        var request = MockMvcRequestBuilders.delete("/Yreya/delete").accept(MediaType.APPLICATION_JSON).header(HttpHeaders.AUTHORIZATION, "Bearer " + result.get("jwt-token")).contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(request).andExpect(MockMvcResultMatchers.content().json(ow.writeValueAsString(Map.of("message", clientDTO.getClientName() + " - Пользователь удален"))));


    }


}

