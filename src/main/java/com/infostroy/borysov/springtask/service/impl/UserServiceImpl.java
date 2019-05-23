package com.infostroy.borysov.springtask.service.impl;

import com.infostroy.borysov.springtask.dao.RoleRepository;
import com.infostroy.borysov.springtask.dao.UserRepository;
import com.infostroy.borysov.springtask.dto.UserChangePasswordDTO;
import com.infostroy.borysov.springtask.dto.UserForgotPasswordDTO;
import com.infostroy.borysov.springtask.entity.Role;
import com.infostroy.borysov.springtask.entity.User;
import com.infostroy.borysov.springtask.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MailingServiceImpl mailingService;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUser(User user) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            Role userRole = roleRepository.findByName("user");
            user.setRole(userRole);
            userRepository.save(user);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    @Override
    public boolean editUserData(User currentUser, User userEdit) {
        if (currentUser != null && userEdit != null) {
            currentUser.setFirstName(userEdit.getFirstName());
            currentUser.setLastName(userEdit.getLastName());
            currentUser.setPhone(userEdit.getPhone());

            userRepository.save(currentUser);
            return true;
        }
        return false;
    }

    @Override
    public boolean validateUserForChangePassword(@Valid UserChangePasswordDTO userChangePasswordDTO, User currentUser) {
        return !userChangePasswordDTO.getNewPassword().equals(userChangePasswordDTO.getRetypeNewPassword())
                || !bCryptPasswordEncoder.matches(userChangePasswordDTO.getOldPassword(), currentUser.getPassword());

    }

    @Override
    public void updatePassword(User currentUser, String newPassword) {
        try {
            currentUser.setPassword(newPassword);
            saveUser(currentUser);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    @Override
    public User findUserByEmailAndPhone(UserForgotPasswordDTO userForgotPasswordDTO) {
        return userRepository.findUserByEmailAndPhone(userForgotPasswordDTO.getEmail(), userForgotPasswordDTO.getPhone());
    }

    @Override
    public void saveNewRandomPasswordAndNotifyUser(User user) {
        try {
            String newPassword = RandomStringUtils.randomAlphanumeric(10);
            user.setPassword(newPassword);
            saveUser(user);
            mailingService.sentNewRandomPassword(user, newPassword);
        } catch (Exception e) {
            logger.error(e.toString());
        }
    }

    @Override
    public boolean userWithSameEmailExists(String email) {
        return findUserByEmail(email) != null;
    }
}
