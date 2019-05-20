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
    private User user;

    public Form getFilledForm() {
        return filledForm;
    }

    public void setFilledForm(Form filledForm) {
        this.filledForm = filledForm;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
