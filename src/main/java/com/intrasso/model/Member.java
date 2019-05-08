package com.intrasso.model;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class Member extends AuditModel {
    @ManyToOne
    @JoinColumn
    private User student;
    @ManyToOne
    @JoinColumn
    private Association association;
    private String role;

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
