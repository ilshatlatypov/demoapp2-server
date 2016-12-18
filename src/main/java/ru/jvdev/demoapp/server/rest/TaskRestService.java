package ru.jvdev.demoapp.server.rest;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ru.jvdev.demoapp.server.dto.TaskDTO;
import ru.jvdev.demoapp.server.model.Task;
import ru.jvdev.demoapp.server.repository.TaskRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rest/tasks")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TaskRestService {

    @NonNull
    private final TaskRepository taskRepository;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@Valid @RequestBody TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());

        Task savedTask = taskRepository.save(task);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(savedTask.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(path = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@PathVariable int id, @Valid @RequestBody TaskDTO taskDTO) {
        Task existing = taskRepository.findOne(id);
        if (existing == null) {
            throw new ResourceNotFoundException();
        }
        existing.setTitle(taskDTO.getTitle());
        taskRepository.save(existing);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public ResponseEntity<TaskDTO> get(@PathVariable int id) {
        Task task = taskRepository.findOne(id);
        if (task == null) {
            throw new ResourceNotFoundException();
        }
        TaskDTO dto = TaskDTO.fromTask(task);
        return ResponseEntity.ok(dto);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (!taskRepository.exists(id)) {
            throw new ResourceNotFoundException();
        }
        taskRepository.delete(id);
        return ResponseEntity.noContent().build();
    }
}
