package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Client;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.services.ClientDetailsService;

@Component
public class ClientValidator implements Validator {

    private final ClientDetailsService clientDetailsService;

    @Autowired
    public ClientValidator(ClientDetailsService clientDetailsService) {
        this.clientDetailsService = clientDetailsService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Client.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Client client = (Client) o;

        try {
            clientDetailsService.loadUserByUsername(client.getClientName());
        } catch (UsernameNotFoundException ignored) {
            return; // все ок, пользователь не найден
        }

        errors.rejectValue("clientName", "", "Человек с таким именем пользователя уже существует");
    }
}