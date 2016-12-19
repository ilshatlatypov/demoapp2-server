package ru.jvdev.demoapp.server.rest;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

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

import ru.jvdev.demoapp.server.dtoconverter.TaskDTOConverter;
import ru.jvdev.demoapp.server.dto.TaskDTO;
import ru.jvdev.demoapp.server.dto.TaskWithAssigneeName;
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
    @NonNull
    private final TaskDTOConverter taskDTOConverter;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@Valid @RequestBody TaskDTO taskDTO) {
        Task task = taskDTOConverter.fromDTO(taskDTO);
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
        Task updated = taskDTOConverter.fromDTO(taskDTO);
        updated.setId(existing.getId());
        taskRepository.save(updated);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public ResponseEntity<TaskDTO> get(@PathVariable int id) {
        Task task = taskRepository.findOne(id);
        if (task == null) {
            throw new ResourceNotFoundException();
        }
        TaskDTO dto = taskDTOConverter.toDTO(task);
        return ResponseEntity.ok(dto);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<TaskWithAssigneeName>> list() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskWithAssigneeName> taskDTOs = tasks.stream()
            .map(taskDTOConverter::toDTOWithAssigneeName)
            .collect(Collectors.toList());
        return ResponseEntity.ok(taskDTOs);
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
