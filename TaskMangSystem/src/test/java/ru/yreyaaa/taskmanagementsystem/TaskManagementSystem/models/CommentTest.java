package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class CommentTest {

    @InjectMocks
    Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToString() {
        Client client = new Client();
        client.setClientName("Bob");
        comment.setAuthor(client);
        comment.setTextComment("I won't do it.");
        String result = comment.toString();
        Assertions.assertEquals("Bob: I won't do it.", result);
    }
}

