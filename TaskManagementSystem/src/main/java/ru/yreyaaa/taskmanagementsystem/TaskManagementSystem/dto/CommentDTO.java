package ru.yreyaaa.taskmanagementsystem.TaskManagementSystem.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class CommentDTO {


    @NotEmpty(message = "Параметр textComment не может быть пустым")
    @Size(max = 200, message = "Параметр textComment не должен превышать 200")
    @Schema(description = "Комментарии к задаче, не более 200 символов")
    private String textComment;


    public String getTextComment() {
        return textComment;
    }

    public void setTextComment(String textComment) {
        this.textComment = textComment;
    }


}

