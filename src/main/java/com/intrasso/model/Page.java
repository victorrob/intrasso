package com.intrasso.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.HashMap;
import java.util.Map;

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
        size = (size > content.length()) ? content.length() : size;
        return content.substring(0, size) + "...";
    }

    public Map<String, String> getAsMap(){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("name", this.name);
        dataMap.put("content", this.content);
        return dataMap;
    }

    public void setContent(String description) {
        this.content = description;
    }
}
