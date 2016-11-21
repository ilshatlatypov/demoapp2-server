package ru.jvdev.demoapp.server;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ru.jvdev.demoapp.server.model.Employee;
import ru.jvdev.demoapp.server.model.Role;
import ru.jvdev.demoapp.server.model.Task;
import ru.jvdev.demoapp.server.model.User;
import ru.jvdev.demoapp.server.repository.EmployeeRepository;
import ru.jvdev.demoapp.server.repository.TaskRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * @author <a href="mailto:ilatypov@wiley.com">Ilshat Latypov</a>
 * @since 18.11.2016
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataLoader implements CommandLineRunner {

    @NonNull private final EmployeeRepository employeeRepository;
    @NonNull private final TaskRepository taskRepository;
    @NonNull private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String username1 = "user1";
        String username2 = "user2";

        User user1 = new User();
        user1.setUsername(username1);
        user1.setPassword(passwordEncoder.encode(username1));
        user1.setEnabled(true);
        user1.setRole(Role.MANAGER);

        User user2 = new User();
        user2.setUsername(username2);
        user2.setPassword(passwordEncoder.encode(username2));
        user2.setEnabled(true);
        user2.setRole(Role.WORKER);

        Set<Employee> employees = new HashSet<>();
        Employee emp1 = new Employee();
        emp1.setFirstname("Peter");
        emp1.setLastname("Jackson");
        emp1.setUser(user1);
        Employee emp2 = new Employee();
        emp2.setFirstname("Neil");
        emp2.setLastname("Blomcamp");
        emp2.setUser(user2);
        employees.add(emp1);
        employees.add(emp2);

        employeeRepository.deleteAllInBatch();
        employeeRepository.save(employees);

        taskRepository.deleteAllInBatch();
        taskRepository.save(new HashSet<Task>() {{
                add(new Task("Deploy to PERF"));
                add(new Task("Upload data"));
                add(new Task("Write script"));
            }});
    }
}
