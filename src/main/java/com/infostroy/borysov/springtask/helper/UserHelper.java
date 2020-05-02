package com.infostroy.borysov.springtask.helper;

import com.infostroy.borysov.springtask.entity.User;

import java.security.Principal;

public class UserHelper {
    public static String getCurrentUserEmail(User user, Principal principal) {
        if (user != null) {
            return user.getEmail();
        } else if (principal != null) {
            return principal.getName();
        } else {
            return null;
        }
    }
}
