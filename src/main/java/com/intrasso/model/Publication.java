package com.intrasso.model;

import org.apache.tomcat.jni.User;

import javax.persistence.OneToOne;

public class Publication extends Page {
    @OneToOne
    private User writer;

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }
}
