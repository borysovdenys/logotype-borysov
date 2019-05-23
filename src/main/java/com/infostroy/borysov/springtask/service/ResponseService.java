package com.infostroy.borysov.springtask.service;

import com.infostroy.borysov.springtask.entity.Field;
import com.infostroy.borysov.springtask.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ResponseService {

    void handleAndSaveResponses(Map<Field, String> fieldList, User currentUser);

    Page<List<String>> getPreparedPage(Pageable pageable, List<Field> allFields);
}
