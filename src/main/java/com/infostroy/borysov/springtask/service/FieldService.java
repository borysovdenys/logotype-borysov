package com.infostroy.borysov.springtask.service;

import com.infostroy.borysov.springtask.entity.Field;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FieldService {

    List<Field> getAllActiveFields();

    void deleteFieldById(String id);

    Optional<Field> findAndPrepareField(String id);

    void updateField(Field modifiedField);

    Page<Field> findAllNotDeleted(Pageable pageable);

    List<Field> findAllOrderById();

    Field findFieldById(Long id);

}
