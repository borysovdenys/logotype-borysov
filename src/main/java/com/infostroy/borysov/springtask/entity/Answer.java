package com.infostroy.borysov.springtask.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "answers")
@Data
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    @Lob
    private String value;

    public Answer(Field field) {
        this.field = field;
    }

    public Answer() {
    }
}
