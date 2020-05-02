package com.infostroy.borysov.springtask.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @Size(min=8, max=100)
    @NotNull
    private String password;

    @Transient
    private String retypePassword;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String phone;

    @ManyToOne
    private Role role;

    private String picture;

    private String locale;

    private Boolean authorizedWithGoogle = false;

    public User() {
    }

}
