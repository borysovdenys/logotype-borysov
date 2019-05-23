package com.infostroy.borysov.springtask.controller;

import com.infostroy.borysov.springtask.entity.Field;
import com.infostroy.borysov.springtask.service.FieldService;
import com.infostroy.borysov.springtask.service.ResponseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ResponseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FieldService fieldService;

    @Autowired
    private ResponseService responseService;

    @GetMapping("/responses")
    public String getFields(@PageableDefault(size = 10, sort = {"id"}) Pageable pageable, Model model) {
        List<Field> allFields = fieldService.findAllOrderById();

        Page<List<String>> page = responseService.getPreparedPage(pageable, allFields);

        model.addAttribute("fields", allFields);
        model.addAttribute("page", page);
        return "responses";
    }

}
