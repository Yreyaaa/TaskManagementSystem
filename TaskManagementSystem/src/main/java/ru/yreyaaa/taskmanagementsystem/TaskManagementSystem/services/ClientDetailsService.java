package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Client;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.repositories.ClientRepository;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.security.ClientDetails;

@Service
public class ClientDetailsService implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String clientName) throws UsernameNotFoundException {
        Client client = clientRepository.findByClientName(clientName);

        if (client == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new ClientDetails(client);

    }
}


