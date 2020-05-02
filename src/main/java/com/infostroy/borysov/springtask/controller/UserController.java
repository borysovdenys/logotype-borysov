package com.infostroy.borysov.springtask.controller;

import com.infostroy.borysov.springtask.dto.UserChangePasswordDTO;
import com.infostroy.borysov.springtask.dto.UserForgotPasswordDTO;
import com.infostroy.borysov.springtask.entity.User;
import com.infostroy.borysov.springtask.helper.UserHelper;
import com.infostroy.borysov.springtask.service.MailingService;
import com.infostroy.borysov.springtask.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private MailingService mailingService;

    @GetMapping(value = "register")
    public ModelAndView getRegistrationPage(Principal principal, ModelAndView modelAndView) {
        if (modelAndView.isEmpty()) {
            modelAndView.addObject("user", new User());
        }
        if (principal != null) {
            modelAndView.setViewName("home");
        }
        return modelAndView;
    }

    @PostMapping(value = "register")
    public ModelAndView doRegistration(@Valid User user, BindingResult result) {
        ModelAndView modelAndView = new ModelAndView();

        if (result.hasErrors()) {
            modelAndView.addObject("validationError", true);
            modelAndView.setViewName("register");
            return modelAndView;
        }

        validateUserForRegister(modelAndView, user);
        if (!modelAndView.isEmpty()) {
            modelAndView.setViewName("register");
            return modelAndView;

        }
        userService.saveUser(user);
        mailingService.sendCongratulationMail(user);
        modelAndView.addObject("successfullyRegistered", true);
        modelAndView.setViewName("index");

        return modelAndView;
    }

    private void validateUserForRegister(ModelAndView modelAndView, @Valid User user) {
        boolean userExists = userService.userWithSameEmailExists(user.getEmail());
        if (userExists) {
            modelAndView.addObject("emailUsedError", true);
        }

        if (isValidPasswordAndRetypePassword(user)) {
            modelAndView.addObject("passwordError", true);
            user.setPassword(null);
            user.setRetypePassword(null);
            modelAndView.addObject("user", user);
        }
    }

    private boolean isValidPasswordAndRetypePassword(@Valid User user) {
        return (user.getPassword() != null && user.getRetypePassword() != null)
                && !user.getPassword().equals(user.getRetypePassword());
    }

    @GetMapping(value = "index")
    public String getLoginPage(Model model, @RequestParam(required = false) String error, Principal principal) {
        if (error != null) {
            model.addAttribute("loginError", true);
        }

        if (principal != null) {
            return "redirect:home";
        }

        return "index";
    }

    @GetMapping(value = "/home")
    public String getHomePage(@AuthenticationPrincipal User user, @AuthenticationPrincipal Principal principal, HttpServletRequest request) {
        String email = UserHelper.getCurrentUserEmail(user, principal);
        User currentUser =  userService.findUserByEmail(email);

        if (currentUser != null) {
            request.getSession().setAttribute("userName", currentUser.getFirstName() + " " + currentUser.getLastName());
        }
        return "home";
    }

    @PostMapping(value = "/home")
    public String getHomePagePost() {
        return "redirect:home";
    }

    @GetMapping(value = "/editProfile")
    public ModelAndView getEditProfilePage(@AuthenticationPrincipal User user, @AuthenticationPrincipal Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("editProfile");

        String email = UserHelper.getCurrentUserEmail(user, principal);
        User currentUser = userService.findUserByEmail(email);
        modelAndView.addObject("currentUser", currentUser);
        return modelAndView;
    }

    @PostMapping(value = "/editProfile")
    public ModelAndView editProfile(User userEdit, @AuthenticationPrincipal User user,
                                    @AuthenticationPrincipal Principal principal,  HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("editProfile");

        String email = UserHelper.getCurrentUserEmail(user, principal);
        User currentUser = userService.findUserByEmail(email);

        if (currentUser == null) {
            modelAndView.addObject("currentUser", new User());
            return modelAndView;
        }

        boolean isUpdated = userService.editUserData(currentUser, userEdit);

        if (!isUpdated) {
            modelAndView.addObject("failedToUpdate", true);
        }

        modelAndView.addObject("successfullyChanged", true);
        modelAndView.addObject("currentUser", currentUser);
        request.getSession().setAttribute("userName", currentUser.getFirstName() + " " + currentUser.getLastName());
        return modelAndView;
    }

    @GetMapping(value = "/changePassword")
    public ModelAndView getChangePasswordPage(@AuthenticationPrincipal User user, @AuthenticationPrincipal Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("changePassword");

        String email = UserHelper.getCurrentUserEmail(user, principal);
        User currentUser = userService.findUserByEmail(email);

        modelAndView.addObject("currentUser", currentUser);
        return modelAndView;
    }

    @PostMapping(value = "/changePassword")
    public ModelAndView changePassword(@Valid UserChangePasswordDTO userChangePasswordDTO, BindingResult result,
                                       @AuthenticationPrincipal User user, @AuthenticationPrincipal Principal principal) {
        ModelAndView modelAndView = new ModelAndView();

        String email = UserHelper.getCurrentUserEmail(user, principal);
        User currentUser = userService.findUserByEmail(email);

        if (result.hasErrors() || userService.validateUserForChangePassword(userChangePasswordDTO, currentUser)) {
            modelAndView.addObject("validationError", true);
            modelAndView.setViewName("changePassword");
            return modelAndView;
        }

        userService.updatePassword(currentUser, userChangePasswordDTO.getNewPassword());

        modelAndView.addObject("successfullyChanged", true);
        modelAndView.setViewName("changePassword");
        mailingService.sendPasswordChangeMail(currentUser);

        return modelAndView;
    }

    @GetMapping(value = "/forgotPassword")
    public String getForgotPasswordPage(Principal principal) {
        if (principal != null) {
            return "redirect:home";
        }
        return "forgotPassword";
    }

    @PostMapping(value = "/forgotPassword")
    public ModelAndView forgotPassword(@Valid UserForgotPasswordDTO userForgotPasswordDTO, BindingResult result, Principal principal) {
        ModelAndView modelAndView = new ModelAndView();

        User user = userService.findUserByEmailAndPhone(userForgotPasswordDTO);

        if (result.hasErrors()) {
            modelAndView.addObject("validationError", true);
            modelAndView.setViewName("forgotPassword");
            return modelAndView;
        } else if (user == null) {
            modelAndView.addObject("userIsAbsent", true);
            modelAndView.setViewName("forgotPassword");
            return modelAndView;
        }

        userService.saveNewRandomPasswordAndNotifyUser(user);
        modelAndView.addObject("successfullySent", true);
        modelAndView.setViewName("index");

        return modelAndView;
    }
}
