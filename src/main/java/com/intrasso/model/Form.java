package com.intrasso.model;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "form")
public class Form extends AuditModel {
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Field> fields;
    @OneToOne
    private PageWithForm pageWithForm;

    public Form() {
        fields = new ArrayList<>();
    }

    public Form(HttpServletRequest request) {
        update(request);
    }

    public Form(HttpServletRequest request, Form form) {
        update(request, form);
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public PageWithForm getPageWithForm() {
        return pageWithForm;
    }

    public void setPageWithForm(PageWithForm pageWithForm) {
        this.pageWithForm = pageWithForm;
    }

    public void update(HttpServletRequest request) {
        int i = 0;
        fields = new ArrayList<>();
        Object value = request.getParameter("selectName-" + i);
        System.out.println(value);
        while (value != null) {
            String label = (String) request.getParameter("inputName-" + i);
            fields.add(new Field((String) value, null, "selectName-" + i++, label));
            value = request.getParameter("selectName-" + i);
        }
    }

    public void update(HttpServletRequest request, Form form){
        fields = new ArrayList<>();
        for (Field field : form.getFields()) {
            fields.add(new Field(
                    field.getType(),
                    request.getParameter(field.getInputId()),
                    field.getInputId()
            ));
        }
    }
}
