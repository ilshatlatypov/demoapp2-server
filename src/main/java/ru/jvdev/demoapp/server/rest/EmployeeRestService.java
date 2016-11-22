package ru.jvdev.demoapp.server.rest;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ru.jvdev.demoapp.server.dto.EmployeeDTO;
import ru.jvdev.demoapp.server.model.Employee;
import ru.jvdev.demoapp.server.model.Role;
import ru.jvdev.demoapp.server.model.User;
import ru.jvdev.demoapp.server.repository.EmployeeRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rest/employees")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeRestService {

    @NonNull private final EmployeeRepository employeeRepository;
    @NonNull private final PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@Valid @RequestBody EmployeeDTO employee) {
        String username = employee.getUsername();
        String encodedPassword = passwordEncoder.encode(username);

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        user.setRole(Role.WORKER);

        Employee emp = new Employee();
        emp.setFirstname(employee.getFirstname());
        emp.setLastname(employee.getLastname());
        emp.setUser(user);

        Employee savedEmp = employeeRepository.save(emp);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(savedEmp.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@PathVariable int id, @Valid @RequestBody EmployeeDTO employee) {
        Employee existing = employeeRepository.findOne(id);
        if (existing == null) {
            throw new ResourceNotFoundException();
        }
        existing.setFirstname(employee.getFirstname());
        existing.setLastname(employee.getLastname());
        employeeRepository.save(existing);
        return ResponseEntity.ok().build();
    }
}
