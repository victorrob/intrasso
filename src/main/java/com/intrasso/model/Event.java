package com.intrasso.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event")
public class Event extends PageWithForm {
    @OneToMany
    private List<RegisteredUser> users;
    @ManyToOne
    @JoinColumn
    private Association association;

    public List<RegisteredUser> getUsers() {
        return users;
    }

    public void setUsers(List<RegisteredUser> users) {
        this.users = users;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    @PreRemove
    public  void removeFromAssociation(){
        association.getEvents().remove(this);
    }

    public void update(Event event){
        this.setName(event.getName());
        this.setContent(event.getContent());
        this.setForm(event.getForm());
        this.setEndDate(event.getEndDate());
        this.setUsers(event.getUsers());
    }
}
