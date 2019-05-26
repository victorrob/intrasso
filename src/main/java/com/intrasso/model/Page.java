package com.intrasso.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Page extends AuditModel {
    private String name;
    @Column(length = 5000)
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

    public String getContent(int size){
        System.out.println("size1 : " + size);
        size = (size > content.length()) ? content.length() : size;
        System.out.println("lenght : " + content.length());
        System.out.println("size : " + size);
        return content.substring(0, size) + "...";
    }

    public void setContent(String description) {
        this.content = description;
    }
}
