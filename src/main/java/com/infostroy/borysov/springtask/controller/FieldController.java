package com.infostroy.borysov.springtask.controller;

import com.infostroy.borysov.springtask.entity.Field;
import com.infostroy.borysov.springtask.entity.TypeField;
import com.infostroy.borysov.springtask.service.FieldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class FieldController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FieldService fieldService;

    @GetMapping("/fields")
    public String getFields(@PageableDefault(size = 10, sort = {"id"}) Pageable pageable, Model model) {
        model.addAttribute("page", fieldService.findAllNotDeleted(pageable));
        return "fields";
    }

    @GetMapping(value = "/delete/{id}")
    public String getDeleteField(@PathVariable String id, Model model, @PageableDefault(size = 10, sort = {"id"}) Pageable pageable) {
        model.addAttribute("deleteField", fieldService.findFieldById(Long.valueOf(id)));
        model.addAttribute("page", fieldService.findAllNotDeleted(pageable));
        return "fields";
    }

    @PostMapping(value = "/delete")
    public String handleDeleteField(@PageableDefault(size = 10, sort = {"id"}) Pageable pageable, Model model, String id) {
        fieldService.deleteFieldById(id);
        model.addAttribute("page", fieldService.findAllNotDeleted(pageable));
        return "fields";
    }


    @GetMapping(value = "/fields/{id}")
    public String handleEditField(@PathVariable String id, @PageableDefault(size = 10, sort = {"id"}) Pageable pageable, Model model) {

        Optional<Field> field = fieldService.findAndPrepareField(id);

        model.addAttribute("page", fieldService.findAllNotDeleted(pageable));
        model.addAttribute("takenField", field.orElse(new Field()));
        model.addAttribute("types", TypeField.values());
        return "fields";
    }

    @PostMapping(value = "/saveField")
    public String saveField(@Valid Field modifiedField, BindingResult bindingResult) {
        fieldService.updateField(modifiedField);
        return "redirect:fields";
    }
}
