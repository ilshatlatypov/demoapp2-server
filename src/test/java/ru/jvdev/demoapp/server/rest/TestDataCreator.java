package ru.jvdev.demoapp.server.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ru.jvdev.demoapp.server.model.Employee;
import ru.jvdev.demoapp.server.model.Role;
import ru.jvdev.demoapp.server.model.User;
import ru.jvdev.demoapp.server.repository.EmployeeRepository;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 08.12.2016
 */
@Component
class TestDataCreator {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    int createEmployee(String firstname, String lastname, String username, String password) {
        Employee emp = buildEmployee(firstname, lastname, username, password);
        return employeeRepository.save(emp).getId();
    }

    int createEmployee(String firstname, String lastname, String username) {
        String anyPassword = "anyPassword";
        Employee emp = buildEmployee(firstname, lastname, username, anyPassword);
        return employeeRepository.save(emp).getId();
    }

    private Employee buildEmployee(String firstname, String lastname, String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setRole(Role.WORKER);

        Employee emp = new Employee();
        emp.setFirstname(firstname);
        emp.setLastname(lastname);
        emp.setUser(user);
        return emp;
    }
}
