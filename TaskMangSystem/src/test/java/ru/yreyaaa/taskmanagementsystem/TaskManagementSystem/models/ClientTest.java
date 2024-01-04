package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientTest {
    @InjectMocks
    Client client;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToString() {
        client.setClientName("Hello");
        assertEquals("Hello", client.getClientName());
    }


}