package com.intrasso.model;

import javax.persistence.*;

@Entity
@Table(name = "candidate")
public class Candidate extends AuditModel {
    @ManyToOne
    @JoinColumn
    private User user;
    @ManyToOne
    @JoinColumn
    private PageWithForm pageWithForm;
    @OneToOne
    private Form form;

    private String status;

    public Candidate() {
        user = null;
        pageWithForm = null;
        status = "en attente";
    }

    public Candidate(Form form, User user) {
        this.user = user;
        this.form = form;
        form.setCandidate(this);
        this.pageWithForm = null;
        status = "en attente";

    }

    public void decline() {
        status = "refusé";
    }

    public void waiting() {
        status = "en attente";
    }

    public void accept() {
        status = "accepté";
    }

    public String getStatus() {
        return this.status;
    }

    public PageWithForm getPageWithForm() {
        return pageWithForm;
    }

    public void setPageWithForm(PageWithForm pageWithForm) {
        this.pageWithForm = pageWithForm;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    @PreRemove
    public void deleteCandidate() {
        for(Candidate candidate : user.getCandidateList()){
            if(candidate.getId().equals(this.getId())){
                user.getCandidateList().remove(candidate);
                break;
            }
        }
        user = null;
        for(Candidate candidate : pageWithForm.getCandidateList()){
            if(candidate.getId().equals(this.getId())){
                pageWithForm.getCandidateList().remove(candidate);
                break;
            }
        }
        pageWithForm = null;
        form.setCandidate(null);
        form = null;
    }
}
