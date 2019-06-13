package com.intrasso.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

@Entity
@Table(name = "member")
public class Member extends AuditModel {
    @ManyToOne
    @JoinColumn
    private User user;
    @ManyToOne
    @JoinColumn
    private Association association;
    private String role;
    private boolean editPublication;
    private boolean editEvent;
    private boolean editJobVacancy;
    private boolean manageMembers;
    private boolean acceptMembers;
    private boolean editAssociation;

    public Member(){
        this.user = null;
        this.association = null;
        this.role = "";
        this.editPublication = false;
        this.editEvent = false;
        this.editJobVacancy = false;
        this.manageMembers = false;
        this.editAssociation = false;
    }

    public Member(User user, String role){
        this.user = user;
        this.role = role;
        this.association = null;
        this.editPublication = false;
        this.editEvent = false;
        this.editJobVacancy = false;
        //TODO remove acceptMembers
        this.manageMembers = false;
        this.editAssociation = false;
    }

    public Member(HttpServletRequest request){
        this.user = null;
        this.association = null;
        this.role = request.getParameter("role");
        this.update(request);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public boolean canEditPublication() {
        return editPublication;
    }

    public void setEditPublication(boolean editPublication) {
        this.editPublication = editPublication;
    }

    public boolean canEditEvent() {
        return editEvent;
    }

    public void setEditEvent(boolean editEvent) {
        this.editEvent = editEvent;
    }

    public boolean canEditJobVacancy() {
        return editJobVacancy;
    }

    public void setEditJobVacancy(boolean editJobVacancy) {
        this.editJobVacancy = editJobVacancy;
    }

    public boolean canManageMembers() {
        return manageMembers;
    }

    public void setManageMembers(boolean manageMembers) {
        this.manageMembers = manageMembers;
    }

    public boolean canAcceptMembers() {
        return acceptMembers;
    }

    public void setAcceptMembers(boolean acceptMembers) {
        this.acceptMembers = acceptMembers;
    }

    public boolean canEditAssociation() {
        return editAssociation;
    }

    public void setEditAssociation(boolean editAssociation) {
        this.editAssociation = editAssociation;
    }


    public void update(HttpServletRequest request) {
        this.editPublication = request.getParameter("editPublication") != null;
        this.editEvent = request.getParameter("editEvent") != null;
        this.editJobVacancy = request.getParameter("editJobVacancy") != null;
        this.acceptMembers = request.getParameter("acceptMembers") != null;
        this.editAssociation = request.getParameter("editAssociation") != null;
    }
    
    public void giveAllRights(){
        this.editPublication = true;
        this.editEvent = true;
        this.editJobVacancy = true;
        this.acceptMembers = true;
        this.manageMembers = true;
        this.editAssociation = true;
    }
}
