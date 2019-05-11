package com.intrasso.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "registeredUser")
public class RegisteredUser extends AuditModel {
    @OneToOne
    @JoinColumn
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
