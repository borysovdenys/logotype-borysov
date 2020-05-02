package com.infostroy.borysov.springtask.controller;

import com.infostroy.borysov.springtask.dto.AnswerWrapper;
import com.infostroy.borysov.springtask.entity.Field;
import com.infostroy.borysov.springtask.entity.User;
import com.infostroy.borysov.springtask.helper.UserHelper;
import com.infostroy.borysov.springtask.service.FieldService;
import com.infostroy.borysov.springtask.service.ResponseService;
import com.infostroy.borysov.springtask.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.LinkedHashMap;

@Controller
public class QuestionnaireController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseService responseService;

    @Autowired
    private FieldService fieldService;

    @GetMapping("/questionnaire")
    public String getQuestions(Model model) {

        model.addAttribute("questionnaire", fieldService.getAllActiveFields());

        AnswerWrapper answerWrapper = new AnswerWrapper();
        answerWrapper.setAnswers(new LinkedHashMap<>());
        for (Field field : fieldService.getAllActiveFields()) {
            answerWrapper.getAnswers().put(field, "");
        }
        model.addAttribute("answerWrapper", answerWrapper);
        return "questionnaire";
    }

    @PostMapping(value = "submitData")
    public String processSave(@ModelAttribute AnswerWrapper answerWrapper, BindingResult bindingResult, Model model,
                              @AuthenticationPrincipal User user,  @AuthenticationPrincipal Principal principal) {

        if (bindingResult.hasErrors()) {
            return "questionnaire";
        }

        if (answerWrapper.getAnswers() != null) {
            String email = UserHelper.getCurrentUserEmail(user, principal);
            User currentUser = userService.findUserByEmail(email);
            responseService.handleAndSaveResponses(answerWrapper.getAnswers(), currentUser);
        }

        return "thankfulPage";
    }

    @GetMapping(value = "submitData")
    public String getThankfulPage() {
        return "thankfulPage";
    }
}
