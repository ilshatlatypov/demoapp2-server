package ru.jvdev.demoapp.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.jvdev.demoapp.server.model.Employee;
import ru.jvdev.demoapp.server.model.User;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee getByUser(User user);
}
