package com.infostroy.borysov.springtask.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserChangePasswordDTO {

    @Size(min=8, max=100)
    @NotNull
    private String oldPassword;

    @Size(min=8, max=100)
    @NotNull
    private String newPassword;

    @Size(min=8, max=100)
    @NotNull
    private String retypeNewPassword;

}
