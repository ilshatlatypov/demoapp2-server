package ru.jvdev.demoapp.server.dto;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import ru.jvdev.demoapp.server.model.Employee;

import lombok.Data;

@Data
public class EmployeeDTO {
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 20;

    private int id;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    @Length(min = MIN_USERNAME_LENGTH, max = MAX_USERNAME_LENGTH)
    @Pattern(regexp = "[a-z]+", message = "{latin.lowercase.only}")
    private String username;

    public static EmployeeDTO fromEmployee(Employee emp) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(emp.getId());
        dto.setFirstname(emp.getFirstname());
        dto.setLastname(emp.getLastname());
        dto.setUsername(emp.getUser().getUsername());
        return dto;
    }
}
