package ru.jvdev.demoapp.server.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    private LocalDate dueDate;
    @ManyToOne
    private Employee assignee;

    public Task() {
    }

    public Task(String title) {
        this.title = title;
    }
}
