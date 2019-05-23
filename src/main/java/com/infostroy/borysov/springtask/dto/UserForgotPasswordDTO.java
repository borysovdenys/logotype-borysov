package com.infostroy.borysov.springtask.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UserForgotPasswordDTO {

    @Email
    private String email;

    private String phone;
}
