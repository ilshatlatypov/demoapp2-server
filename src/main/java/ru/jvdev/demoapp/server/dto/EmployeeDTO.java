package ru.jvdev.demoapp.server.dto;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import ru.jvdev.demoapp.server.model.Employee;

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

    public static EmployeeDTO fromEmployee(Employee emp) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(emp.getId());
        dto.setFirstname(emp.getFirstname());
        dto.setLastname(emp.getLastname());
        dto.setUsername(emp.getUser().getUsername());
        return dto;
    }
}
