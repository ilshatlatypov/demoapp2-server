package ru.jvdev.demoapp.server.dto;

import org.hibernate.validator.constraints.NotBlank;

import ru.jvdev.demoapp.server.model.Task;

import lombok.Data;

@Data
public class TaskDTO {

    private int id;
    @NotBlank
    private String title;
    private Integer assigneeId;

    public static TaskDTO fromTask(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        if (task.getAssignee() != null) {
            dto.setAssigneeId(task.getAssignee().getId());
        }
        return dto;
    }
}
