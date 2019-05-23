package com.infostroy.borysov.springtask.entity;

public enum TypeField {
    TEXT(1L), TEXTAREA(2L), RADIOBUTTON(3L), CHECKBOX(4L), COMBOBOX(5L), DATE(6L);


    private Long id;

    public static TypeField getById(Long id) {
        for (TypeField e : values()) {
            if (e.getId().equals(id)) return e;
        }
        return null;
    }


    TypeField(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

