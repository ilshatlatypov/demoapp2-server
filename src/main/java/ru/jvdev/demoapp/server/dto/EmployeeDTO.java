package ru.jvdev.demoapp.server.dto;

import org.hibernate.validator.constraints.NotBlank;

import lombok.Data;

/**
 * Created by ilshat on 20.11.16.
 */
@Data
public class EmployeeDTO {
    private int id;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    private String username;
}
