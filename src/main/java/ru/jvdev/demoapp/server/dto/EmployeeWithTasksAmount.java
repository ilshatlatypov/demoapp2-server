package ru.jvdev.demoapp.server.dto;

import ru.jvdev.demoapp.server.model.Employee;

import lombok.Data;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 19.12.2016
 */
@Data
public class EmployeeWithTasksAmount {
    private int id;
    private String firstname;
    private String lastname;
    private String username;
    private int tasksAmount;

    public static EmployeeWithTasksAmount fromEmployee(Employee emp) {
        EmployeeWithTasksAmount dto = new EmployeeWithTasksAmount();
        dto.setId(emp.getId());
        dto.setFirstname(emp.getFirstname());
        dto.setLastname(emp.getLastname());
        dto.setUsername(emp.getUser().getUsername());
        dto.setTasksAmount(emp.getTasks().size());
        return dto;
    }
}
