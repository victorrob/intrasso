package com.intrasso.model;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "pageWithForm")
public class PageWithForm extends Page {
    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn
    private Form form;
    private Date endDate;
    @ManyToOne
    @JoinColumn
    private Association association;
    @OneToMany(
            mappedBy = "pageWithForm",
            cascade = CascadeType.ALL
    )
    private List<Candidate> candidateList;

    private String role;
    private String type;
    private boolean published;

    public PageWithForm() {
        form = null;
        candidateList = new ArrayList<>();
        endDate = new Date();
        role = "";
        type = "";
        published = false;
    }

    public PageWithForm(HttpServletRequest request, String type) {
        update(request);
        this.type = type;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
        form.setPageWithForm(this);
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getEndDateString(String dateFormat) {
        SimpleDateFormat date = new SimpleDateFormat(dateFormat);
        return date.format(endDate);
    }

    @Override
    public Map<String, String> getAsMap() {
        Map<String, String> dataMap = super.getAsMap();
        if (endDate == null) {
            dataMap.put("endDate", "");
            return dataMap;
        }
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        dataMap.put("endDate", date.format(endDate));
        return dataMap;
    }

    @Override
    public void update(Object object) {
        if (!(object instanceof PageWithForm)) {
            return;
        }
        PageWithForm pageWithForm = (PageWithForm) object;
        super.update(object);
        this.setForm(pageWithForm.getForm());
        this.setEndDate(pageWithForm.getEndDate());
        this.published = pageWithForm.published;
        candidateList = pageWithForm.candidateList;

    }

    @Override
    public void update(HttpServletRequest request) {
        super.update(request);
        candidateList = new ArrayList<>();
        String dateString = request.getParameter("endDateString");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            endDate = df.parse(dateString);
        } catch (java.text.ParseException e) {
            System.out.println("erreur : " + e.getMessage());
            endDate = new Date();
        }
        System.out.println("is published : " + request.getParameter("published"));
        published = request.getParameter("published").equals("true");
        form = null;
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public List<Candidate> getCandidateList() {
        List<Candidate> candidates = new ArrayList<>();
        for (Candidate candidate : candidateList) {
            if (candidate.getStatus().equals("en attente")) {
                candidates.add(candidate);
            }
        }
        return candidates;
    }

    public void setCandidateList(List<Candidate> candidates) {
        this.candidateList = candidates;
    }

    public void addCandidate(Candidate candidate) {
        this.candidateList.add(candidate);
        candidate.setPageWithForm(this);
    }

    @PreRemove
    public void removeFromAssociation() {
        association.getPageWithFormList().remove(this);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean isPublished) {
        if (type.equals("publication") && !published && isPublished) {
            endDate = new Date();
        }
        published = isPublished;
    }
}
