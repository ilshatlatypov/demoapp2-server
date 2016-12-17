package ru.jvdev.demoapp.server.dto;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class TaskDTO {

    private int id;
    @NotBlank
    private String title;
}
