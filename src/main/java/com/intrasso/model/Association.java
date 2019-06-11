package com.intrasso.model;

import com.intrasso.repository.PageWithFormRepository;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "association")
public class Association extends Page {
    @OneToMany(
            mappedBy = "association",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Member> members;
    @OneToMany
    private List<Form> savedForm;
    @OneToMany(
            mappedBy = "association",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<PageWithForm> pageWithFormList;

    public Association() {
        this.members = new ArrayList<>();
        this.savedForm = new ArrayList<>();
        this.pageWithFormList = new ArrayList<>();
    }

    @SuppressWarnings(value = "all")
    public Association(Association association) {
        association.update(this);
    }

    public List<Form> getSavedForm() {
        return savedForm;
    }

    public void setSavedForm(List<Form> savedForm) {
        this.savedForm = savedForm;
    }

    public List<PageWithForm> getPageWithFormList() {
        return pageWithFormList;
    }

    public void setPageWithFormList(List<PageWithForm> pageWithFormList) {
        this.pageWithFormList = pageWithFormList;
    }

    public List<PageWithForm> getByType(String type){
        List<PageWithForm> pageWithForms = new ArrayList<>();
        for(PageWithForm pageWithForm : pageWithFormList){
            if(pageWithForm.getType().equals(type)){
                pageWithForms.add(pageWithForm);
            }
        }
        return pageWithForms;
    }

    public void addPageWithForm(PageWithForm pageWithForm) {
        this.pageWithFormList.add(pageWithForm);
        pageWithForm.setAssociation(this);
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public void addMember(Member member) {
        this.members.add(member);
        member.setAssociation(this);
    }

    public void addMember(User user, String role){
        for(Member member : members){
            if(member.getUser().getId().equals(user.getId())){
                member.setRole(role);
                return;
            }
        }
        Member member = new Member(user, role);
        this.members.add(member);
        member.setAssociation(this);
    }

    public void update(Object object) {
        if (object instanceof Association) {
            Association association = (Association) object;
            association.setName(this.getName());
            association.setContent(this.getContent());
            association.pageWithFormList = this.pageWithFormList;
            association.setMembers(this.getMembers());
        }
    }
}
