package com.infostroy.borysov.springtask.service.impl;

import com.infostroy.borysov.springtask.entity.User;
import com.infostroy.borysov.springtask.service.MailingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ResourceBundle;

@Service
@Transactional
public class MailingServiceImpl implements MailingService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JavaMailSender sender;

    private void sendMail(String email, String text) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setTo(email);
            helper.setText(text);
            helper.setSubject("LOGOTYPE");
            sender.send(message);
        } catch (MessagingException | MailSendException | MailAuthenticationException e) {
            logger.error(e.toString());
        }
    }

    public void sendCongratulationMail(User user) {
        String text = ResourceBundle.getBundle("messages").getString("mailing.congratulationMail");
        sendMail(user.getEmail(), String.format(text, user.getFirstName()));
    }

    public void sendPasswordChangeMail(User user) {
        String text = ResourceBundle.getBundle("messages").getString("mailing.successfullyChanged");
        sendMail(user.getEmail(), text);
    }

    public void sentNewRandomPassword(User user, String newPassword) {
        String text = ResourceBundle.getBundle("messages").getString("mailing.newRandomPassword");
        sendMail(user.getEmail(), String.format(text, newPassword));
    }
}
