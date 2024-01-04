package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models.Client;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByClientName(String clientName);

    List<Client> findAll();


    void deleteClientByClientName(String clientName);

}
