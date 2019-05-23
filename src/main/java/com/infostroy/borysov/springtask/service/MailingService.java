package com.infostroy.borysov.springtask.service;

import com.infostroy.borysov.springtask.entity.User;

public interface MailingService {

    void sendCongratulationMail(User user);

    void sendPasswordChangeMail(User user);

    void sentNewRandomPassword(User user, String newPassword);
}
