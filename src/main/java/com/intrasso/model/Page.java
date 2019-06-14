package com.intrasso.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@MappedSuperclass
public abstract class Page extends AuditModel {
    private String name;
    @Column(length = 5000)
    private String content;

    public Page(){
        this.name = "";
        this.content = "";
    }

    public Page(HttpServletRequest request){
        update(request);
    }

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
        return content.substring(0, size) + ((size != content.length()) ? "..." : "");
    }

    public String getContentAsHtml(){
        System.out.println("content : " + content.replace("\\n", "<br/>\\n"));
        System.out.println("content : " + content.replace("\n", "<br/>"));
        System.out.println("line separator : " + System.lineSeparator());
        return content.replace("\\n", "<br/>\\n");
    }

    public Map<String, String> getAsMap(){
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("name", this.name);
        dataMap.put("content", this.content);
        return dataMap;
    }

    public void update(Object object){
        if(!(object instanceof Page)){
            return;
        }
        Page page = (Page) object;
        this.name = page.name;
        this.content = page.content;
    }

    public void update(HttpServletRequest request){
        this.name = request.getParameter("name");
        this.content = request.getParameter("content");
    }

    public void setContent(String description) {
        this.content = description;
    }
}
