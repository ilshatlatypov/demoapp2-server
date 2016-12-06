package ru.jvdev.demoapp.server.rest;

import java.net.URI;
import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import ru.jvdev.demoapp.server.dto.EmployeeDTO;
import ru.jvdev.demoapp.server.dto.PasswordDTO;
import ru.jvdev.demoapp.server.model.Employee;
import ru.jvdev.demoapp.server.model.Role;
import ru.jvdev.demoapp.server.model.User;
import ru.jvdev.demoapp.server.repository.EmployeeRepository;
import ru.jvdev.demoapp.server.repository.UserRepository;
import ru.jvdev.demoapp.server.rest.exception.UsernameConflictException;
import ru.jvdev.demoapp.server.rest.exception.UsernameModificationException;
import ru.jvdev.demoapp.server.service.CurrentUser;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rest/employees")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeRestService {

    @NonNull private final EmployeeRepository employeeRepository;
    @NonNull private final UserRepository userRepository;
    @NonNull private final PasswordEncoder passwordEncoder;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> create(@Valid @RequestBody EmployeeDTO employee) {
        String username = employee.getUsername();
        User userWithSameUsername = userRepository.getByUsername(username);
        if (userWithSameUsername != null) {
            throw new UsernameConflictException(username);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(username));
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

    @RequestMapping(path = "{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@PathVariable int id, @Valid @RequestBody EmployeeDTO employee) {
        Employee existing = employeeRepository.findOne(id);
        if (existing == null) {
            throw new ResourceNotFoundException();
        }

        String existingUsername = existing.getUser().getUsername();
        String newUsername = employee.getUsername();
        if (!existingUsername.equals(newUsername)) {
            throw new UsernameModificationException(newUsername);
        }

        existing.setFirstname(employee.getFirstname());
        existing.setLastname(employee.getLastname());
        employeeRepository.save(existing);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET)
    public ResponseEntity<EmployeeDTO> get(@PathVariable int id) {
        Employee employee = employeeRepository.findOne(id);
        if (employee == null) {
            throw new ResourceNotFoundException();
        }
        EmployeeDTO dto = EmployeeDTO.fromEmployee(employee);
        return ResponseEntity.ok(dto);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (!employeeRepository.exists(id)) {
            throw new ResourceNotFoundException();
        }
        employeeRepository.delete(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(path = "isUsernameUsed/{username}")
    public ResponseEntity<Void> isUsernameUsed(@PathVariable String username) {
        User user = userRepository.getByUsername(username);
        return user != null ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @RequestMapping(path = "changePassword", method = RequestMethod.POST)
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal Principal principal,
                                               @Valid @RequestBody PasswordDTO password) {
        CurrentUser activeUser = (CurrentUser) ((Authentication) principal).getPrincipal();
        User user = activeUser.getUser();

        String encodedPassword = passwordEncoder.encode(password.getValue());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
