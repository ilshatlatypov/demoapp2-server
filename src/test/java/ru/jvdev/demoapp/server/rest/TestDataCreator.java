package ru.jvdev.demoapp.server.rest;

import org.springframework.beans.factory.annotation.Autowired;
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

    int createEmployee(String firstname, String lastname, String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(username);
        user.setEnabled(true);
        user.setRole(Role.WORKER);

        Employee emp = new Employee();
        emp.setFirstname(firstname);
        emp.setLastname(lastname);
        emp.setUser(user);

        return employeeRepository.save(emp).getId();
    }
}
