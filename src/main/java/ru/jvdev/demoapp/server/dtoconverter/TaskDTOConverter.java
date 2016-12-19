package ru.jvdev.demoapp.server.dtoconverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import ru.jvdev.demoapp.server.dto.TaskDTO;
import ru.jvdev.demoapp.server.dto.TaskWithAssigneeName;
import ru.jvdev.demoapp.server.model.Employee;
import ru.jvdev.demoapp.server.model.Task;
import ru.jvdev.demoapp.server.repository.EmployeeRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 19.12.2016
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskDTOConverter {

    @NonNull
    private final EmployeeRepository employeeRepository;

    public TaskDTO toDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDueDate(task.getDueDate());
        if (task.getAssignee() != null) {
            dto.setAssigneeId(task.getAssignee().getId());
        }
        return dto;
    }

    public Task fromDTO(TaskDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDueDate(dto.getDueDate());
        Integer employeeId = dto.getAssigneeId();
        if (employeeId != null) {
            task.setAssignee(findEmployeeOr404(employeeId));
        } else {
            task.setAssignee(null);
        }
        return task;
    }

    public TaskWithAssigneeName toDTOWithAssigneeName(Task task) {
        TaskWithAssigneeName dto = new TaskWithAssigneeName();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDueDate(task.getDueDate());
        Employee assignee = task.getAssignee();
        if (assignee != null) {
            dto.setAssigneeName(assignee.getFullname());
        }
        return dto;
    }

    private Employee findEmployeeOr404(int employeeId) {
        Employee employee = employeeRepository.findOne(employeeId);
        if (employee == null) {
            throw new ResourceNotFoundException("Cannot find employee with id " + employeeId);
        }
        return employee;
    }
}
