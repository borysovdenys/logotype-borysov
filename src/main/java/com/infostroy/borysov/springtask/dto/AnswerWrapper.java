package com.infostroy.borysov.springtask.dto;

import com.infostroy.borysov.springtask.entity.Field;
import lombok.Data;

import java.util.Map;

@Data
public class AnswerWrapper {
    private Map<Field, String> answers;
}
