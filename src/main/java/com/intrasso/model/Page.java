package com.intrasso.model;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Page extends AuditModel {
    private String name;
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String description) {
        this.content = description;
    }
}
