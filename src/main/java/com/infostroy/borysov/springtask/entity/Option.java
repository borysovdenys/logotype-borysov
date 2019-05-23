package com.infostroy.borysov.springtask.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "options")
@Data
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    public Option(String description) {
        this.description = description;
    }

    public Option() {
    }
}
