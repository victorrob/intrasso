package com.intrasso.model;

import javax.persistence.OneToMany;
import java.util.List;

public class Form extends AuditModel{
    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    @OneToMany
    private List<Field> fields;
}
