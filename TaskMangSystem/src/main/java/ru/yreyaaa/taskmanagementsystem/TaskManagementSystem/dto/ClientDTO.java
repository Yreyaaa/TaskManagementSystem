package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Schema(description = "Сущность пользователя")

public class ClientDTO {


    @NotEmpty(message = "Параметр clientName не может быть пустым")
    @Size(min = 1, max = 50, message = "Параметр clientName должен иметь длинну 1-50 символов")
    @Schema(description = "Имя пользователя, длинна 1-50 символов")

    private String clientName;

    @NotEmpty(message = "Параметр password не может быть пустым")
    @Size(min = 5, max = 50, message = "Параметр password должен иметь длинну 4-50 символов")
    @Schema(description = "Пароль, длинна  4-50 символов")

    private String password;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}