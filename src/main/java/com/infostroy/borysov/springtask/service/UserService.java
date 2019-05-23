package com.infostroy.borysov.springtask.service;

import com.infostroy.borysov.springtask.dto.UserChangePasswordDTO;
import com.infostroy.borysov.springtask.dto.UserForgotPasswordDTO;
import com.infostroy.borysov.springtask.entity.User;

import javax.validation.Valid;

public interface UserService {

    User findUserByEmail(String email);

    void saveUser(User user);

    boolean editUserData(User currentUser, User userEdit);

    boolean validateUserForChangePassword(@Valid UserChangePasswordDTO userChangePasswordDTO, User currentUser);

    void updatePassword(User currentUser, String newPassword);

    User findUserByEmailAndPhone(UserForgotPasswordDTO userForgotPasswordDTO);

    void saveNewRandomPasswordAndNotifyUser(User user);

    boolean userWithSameEmailExists(String email);
}
