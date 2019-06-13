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
    private boolean hasForm;

    public PageWithForm() {
        form = null;
        candidateList = new ArrayList<>();
        endDate = new Date();
        role = "";
        type = "";
        published = false;
        hasForm = false;
    }

    public PageWithForm(String type){
        form = null;
        candidateList = new ArrayList<>();
        endDate = new Date();
        role = "";
        this.type = type;
        published = false;
        hasForm = type.equals("JobVacancy");
    }

    public PageWithForm(HttpServletRequest request, String type) {
        update(request);
        this.type = type;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        if (!hasForm) {
            this.form = form;
            form.setPageWithForm(this);
        }
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
        dataMap.put("published", ((published) ? "1" : "0"));
        dataMap.put("hasForm", ((hasForm) ? "1" : "0"));

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
        this.hasForm = pageWithForm.hasForm;
        candidateList = pageWithForm.candidateList;

    }

    @Override
    public void update(HttpServletRequest request) {
        super.update(request);
        candidateList = new ArrayList<>();

        String dateString = request.getParameter("endDateString");
        if(dateString != null) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            try {
                endDate = df.parse(dateString);
            } catch (java.text.ParseException e) {
                System.out.println("erreur : " + e.getMessage());
                endDate = new Date();
            }
        }
        else {
            endDate = new Date();
        }
        System.out.println(request.getParameter("published"));
        published = request.getParameter("published") != null;
        System.out.println(published);
        hasForm = request.getParameter("hasForm") != null;
        if(!hasForm){
            form = null;
        }
        else if(form != null) {
            form.update(request);
        }
        else {
            form = new Form(request);
        }
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

    public boolean hasForm() {
        return hasForm;
    }

    public void setHasForm(boolean hasForm) {
        this.hasForm = hasForm;
    }
}
