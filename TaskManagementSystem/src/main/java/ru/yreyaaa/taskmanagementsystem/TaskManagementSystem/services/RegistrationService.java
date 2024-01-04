package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Client;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.repositories.ClientRepository;


@Service
@Transactional

public class RegistrationService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional

    public void deleteClientByClientName(String clientName) {
        clientRepository.deleteClientByClientName(clientName);
    }


    @Transactional
    public void register(Client client) {
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        clientRepository.save(client);
    }
}
