package com.infostroy.borysov.springtask.service.impl;

import com.infostroy.borysov.springtask.dao.ResponseRepository;
import com.infostroy.borysov.springtask.entity.*;
import com.infostroy.borysov.springtask.service.ResponseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Service
@Transactional
public class ResponseServiceImpl implements ResponseService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ResponseRepository responseRepository;

    @Override
    public void handleAndSaveResponses(Map<Field, String> fieldList, User currentUser) {
        Response response = new Response();
        response.setUser(currentUser);
        response.setAnswers(new ArrayList<>());
        for (Map.Entry<Field, String> entry : fieldList.entrySet()) {
            Answer answer = new Answer(entry.getKey());

            String managedAnswerValue = manageDefaultOfReply(entry.getKey().getType(), entry.getValue());
            answer.setValue(managedAnswerValue);

            response.getAnswers().add(answer);
        }
        responseRepository.save(response);
    }

    private String manageDefaultOfReply(TypeField type, String value) {
        if(TypeField.CHECKBOX.equals(type) && value == null) {
            return "false";
        }
        if (value == null || "".equals(value.trim())) {
            return ResourceBundle.getBundle("messages").getString("DEFAULT_VALUE");
        }
        return value;
    }

    public Page<List<String>> getPreparedPage(Pageable pageable, List<Field> allFields) {
        List<List<String>> values = new ArrayList<>();
        for (Response response : responseRepository.findAllByOrderById()) {
            List<String> line = new ArrayList<>();
            for (Field field : allFields) {
                String value = response.getAnswers().stream()
                        .filter(answer -> field.equals(answer.getField()))
                        .findAny().map(Answer::getValue)
                        .orElse(ResourceBundle.getBundle("messages").getString("DEFAULT_VALUE"));
                line.add(value);
            }
            values.add(line);
        }
        Long start = pageable.getOffset();
        Long end = (start + pageable.getPageSize()) > values.size() ? values.size() : (start + pageable.getPageSize());
        return new PageImpl<>(values.subList(start.intValue(), end.intValue()), pageable, values.size());
    }
}
