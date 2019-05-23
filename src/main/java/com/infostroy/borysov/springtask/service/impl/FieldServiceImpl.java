package com.infostroy.borysov.springtask.service.impl;

import com.infostroy.borysov.springtask.dao.FieldRepository;
import com.infostroy.borysov.springtask.dao.OptionRepository;
import com.infostroy.borysov.springtask.entity.Field;
import com.infostroy.borysov.springtask.entity.Option;
import com.infostroy.borysov.springtask.entity.TypeField;
import com.infostroy.borysov.springtask.service.FieldService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class FieldServiceImpl implements FieldService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FieldRepository fieldRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Override
    public List<Field> getAllActiveFields() {
        return fieldRepository.findAllDisplayed();
    }

    @Override
    public void deleteFieldById(String id) {
        if (StringUtils.isNumeric(id)) {
            Optional<Field> fieldToDelete = fieldRepository.findById(Long.valueOf(id));
            fieldToDelete.ifPresent(field -> {
                field.setIsDeleted(true);
                fieldRepository.save(field);
            });

        }
    }

    @Override
    public Optional<Field> findAndPrepareField(String id) {
        if (StringUtils.isNumeric(id)) {
            Optional<Field> field = fieldRepository.findByIdAndIsDeletedFalseOrIsDeletedIsNull(Long.valueOf(id));
            if (field.isPresent()) {
                String area = field.get().getOptions().stream().map(Option::getDescription).collect(Collectors.joining("\r\n"));
                field.get().setOptionsArea(area);
            }
            return field;
        }
        return Optional.empty();
    }

    @Override
    public void updateField(Field modifiedField) {
        deleteOldOptions(modifiedField);

        manageOptionArea(modifiedField);

        if (modifiedField.getRequired() == null)
            modifiedField.setRequired(false);
        if (modifiedField.getIsActive() == null)
            modifiedField.setIsActive(false);

        fieldRepository.save(modifiedField);
    }

    @Override
    public Page<Field> findAllNotDeleted(Pageable pageable) {
        return fieldRepository.findAllByIsDeletedFalseOrIsDeletedIsNull(pageable);
    }

    @Override
    public List<Field> findAllOrderById() {
       return fieldRepository.findAllByOrderById();
    }

    @Override
    public Field findFieldById(Long id) {
        return fieldRepository.findByIdAndIsDeletedFalseOrIsDeletedIsNull(id).orElse(null);
    }

    private void manageOptionArea(Field modifiedField) {
        if (modifiedField.getOptionsArea() != null && isFieldWithOptions(modifiedField.getType())) {
            List<String> options = Arrays.stream(modifiedField.getOptionsArea().split("\\r\\n"))
                    .map(String::trim)
                    .collect(Collectors.toList());
            modifiedField.setOptions(new ArrayList<>());
            for (String o : options) {
                Option option = new Option(o);
                optionRepository.save(option);
                modifiedField.getOptions().add(option);
            }
        }
    }

    private void deleteOldOptions(Field modifiedField) {
        if (modifiedField.getId() != null) {
            Optional<Field> fieldById = fieldRepository.findById(modifiedField.getId());
            fieldById.ifPresent(field -> optionRepository.deleteAll(field.getOptions()));
        }
    }

    private boolean isFieldWithOptions(TypeField typeField) {
        return TypeField.RADIOBUTTON.equals(typeField) || TypeField.COMBOBOX.equals(typeField);
    }


}
