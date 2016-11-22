package ru.jvdev.demoapp.server.dto;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

@Data
public class EmployeeDTO {
    private int id;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    @Pattern(regexp = "[a-z]+", message = "{latin.lowercase.only}")
    private String username;
}
