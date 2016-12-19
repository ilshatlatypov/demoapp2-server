package ru.jvdev.demoapp.server.dto;

import ru.jvdev.demoapp.server.model.Task;

import lombok.Data;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 19.12.2016
 */
@Data
public class TaskWithAssigneeName {
    private int id;
    private String title;
    private String assigneeName;

    public static TaskWithAssigneeName fromTask(Task task) {
        TaskWithAssigneeName dto = new TaskWithAssigneeName();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        if (task.getAssignee() != null) {
            dto.setAssigneeName(task.getAssignee().getFullname());
        }
        return dto;
    }
}
