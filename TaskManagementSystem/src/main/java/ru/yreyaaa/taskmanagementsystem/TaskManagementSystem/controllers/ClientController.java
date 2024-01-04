package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.dto.ClientDTO;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Client;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.repositories.ClientRepository;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.security.ClientDetails;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.security.JWTUtil;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.services.RegistrationService;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.util.ClientValidator;

import javax.validation.Valid;
import java.util.Map;


@Validated
@RestController
public class ClientController {

    private final ClientValidator clientValidator;


    private final ClientRepository clientRepository;
    private final RegistrationService registrationService;

    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public ClientController(ClientValidator clientValidator, ClientRepository clientRepository, RegistrationService registrationService, PasswordEncoder passwordEncoder, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.clientValidator = clientValidator;
        this.clientRepository = clientRepository;
        this.registrationService = registrationService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;

        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Вход в систему", description = "Позволяет получить JWT токен для зарегистрированного пользователя, время действия токена - 60 минут")
    @PostMapping("/login")
    public Map<String, String> performLogin(@Valid @RequestBody @Parameter(description = "JSON с логином и паролем пользователя") ClientDTO clientDto, BindingResult bindingResult) {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(clientDto.getClientName(), clientDto.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Вееден неверный логин или пароль");
        }

        String token = jwtUtil.generateToken(clientDto.getClientName());
        return Map.of("jwt-token", token);
    }

    @Operation(summary = "Получений формы регистрации", description = "Возвращает формат отправки данных для регистрации")
    @GetMapping("/registration")
    public Map<String, String> registration() {

        return Map.of("clientName: \"<введите имя пользователя>\"", "password: \"<введите имя пароль>\"\"");
    }

    @Operation(summary = "Регестрация пользователеля", description = "Принимает логин, пароль и регестрирует нового пользователя")

    @PostMapping("/registration")
    public Map<String, String> registration(@Valid @RequestBody @Parameter(description = "JSON с логином и паролем пользователя") ClientDTO clientDto, BindingResult bindingResult) {


        Client client = convertToClient(clientDto);
        clientValidator.validate(client, bindingResult);
        if (bindingResult.hasErrors()) {
            return Map.of("message", "Пользователь с таким именем уже существует");
        }

        registrationService.register(client);

        String token = jwtUtil.generateToken(client.getClientName());
        return Map.of("jwt-token", token);


    }

    @Operation(security = {@SecurityRequirement(name = "JWT")}, summary = "Удаление пользователеля", description = "Принимает JWT токен и удаляет соответствубщего пользователя из базы")
    @DeleteMapping("/{clientName}/delete")
    public Map<String, String> deleteClient(@PathVariable @Parameter(description = "Имя пользователя которого нужно удалить") String clientName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ClientDetails clientDetails = (ClientDetails) authentication.getPrincipal();
        Client currentClient = clientRepository.findByClientName(clientDetails.getUsername());

        Client clientToDelete = clientRepository.findByClientName(clientName);
        if (clientToDelete == null) {
            return Map.of("message", clientName + " - Не найдено пользователя с таким именем");
        }
        if (currentClient.getClientName().equals(clientToDelete.getClientName())) {
            registrationService.deleteClientByClientName(clientName);
            return Map.of("message", clientName + " - Пользователь удален");
        } else {
            return Map.of("message", clientName + " - у вас нет прав ддя удаления даннного пользователя");
        }
    }

    public Client convertToClient(ClientDTO clientDto) {
        return this.modelMapper.map(clientDto, Client.class);
    }

}


