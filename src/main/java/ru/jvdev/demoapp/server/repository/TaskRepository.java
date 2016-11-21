package ru.jvdev.demoapp.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.jvdev.demoapp.server.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
