package com.infostroy.borysov.springtask.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "fields")
@Data
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;

    private TypeField type;

    private Boolean required;

    private Boolean isActive;

    private Boolean isDeleted;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "field_id")
    private List<Option> options;

    @Transient
    private String optionsArea;

    public Field() {
    }

}
