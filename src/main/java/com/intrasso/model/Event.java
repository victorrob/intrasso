package com.intrasso.model;

import javax.persistence.OneToMany;
import java.util.List;

public class Event extends PageWithForm {
    @OneToMany
    private List<RegistredUser> users;

    public List<RegistredUser> getUsers() {
        return users;
    }

    public void setUsers(List<RegistredUser> users) {
        this.users = users;
    }
}
