package com.intrasso.model;

import javax.persistence.OneToOne;

public class RegistredUser extends AuditModel {
    @OneToOne
    private Form filledForm;
    private User student;

    public Form getFilledForm() {
        return filledForm;
    }

    public void setFilledForm(Form filledForm) {
        this.filledForm = filledForm;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }
}
