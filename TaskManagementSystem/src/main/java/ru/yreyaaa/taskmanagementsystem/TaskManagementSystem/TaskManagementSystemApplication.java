package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



// http://localhost:8091/swagger-ui/index.html

@SpringBootApplication

public class TaskManagementSystemApplication  {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagementSystemApplication.class, args);


    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


}
