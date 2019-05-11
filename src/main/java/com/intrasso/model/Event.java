package com.intrasso.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "event")
public class Event extends PageWithForm {
    @OneToMany
    private List<RegisteredUser> users;

    public List<RegisteredUser> getUsers() {
        return users;
    }

    public void setUsers(List<RegisteredUser> users) {
        this.users = users;
    }
}
