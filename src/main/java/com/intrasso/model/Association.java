package com.intrasso.model;

import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "associations")
public class Association extends Page {
    @OneToMany(
            mappedBy = "association",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Member> members;
    @OneToMany
    private List<Form> savedForm;
    @OneToMany
    private List<Event> events;
    @OneToMany
    private List<Publication> publications;
    @OneToMany
    private List<JobVacancy> jobVacancies;

    public List<Form> getSavedForm() {
        return savedForm;
    }

    public void setSavedForm(List<Form> savedForm) {
        this.savedForm = savedForm;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event){
        this.events.add(event);
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Publication> getPublications() {
        return publications;
    }

    public void addPublication(Publication publication){
        this.publications.add(publication);
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    public List<JobVacancy> getJobVacancies() {
        return jobVacancies;
    }

    public void setJobVacancies(List<JobVacancy> jobVacancies) {
        this.jobVacancies = jobVacancies;
    }

    public void addJobVacancy(JobVacancy jobVacancy){
        this.jobVacancies.add(jobVacancy);
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public void addMember(Member member){
        this.members.add(member);
        member.setAssociation(this);
    }
}
