package com.intrasso.model;

import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

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

    @Override
    public Map<String, String> getAsMap(){
        Map<String, String> dataMap = super.getAsMap();
        if(endDate == null){
            dataMap.put("endDate", "");
            return dataMap;
        }
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        dataMap.put("endDate", date.format(endDate));
        return dataMap;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
