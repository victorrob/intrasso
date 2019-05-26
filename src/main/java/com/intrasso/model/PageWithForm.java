package com.intrasso.model;

import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import java.text.SimpleDateFormat;
import java.util.Date;

@MappedSuperclass
public abstract class PageWithForm extends Page {
    @OneToOne
    @JoinColumn
    private Form form;
    private Date endDate;

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getEndDateString(String dateFormat){
        SimpleDateFormat date = new SimpleDateFormat(dateFormat);
        return date.format(endDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
