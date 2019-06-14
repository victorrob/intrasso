package com.intrasso.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "form")
public class Form extends AuditModel {
    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Field> fields;
    @OneToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private PageWithForm pageWithForm;
    @OneToOne
    private Candidate candidate;

    public Form() {
        fields = new ArrayList<>();
    }

    public Form(HttpServletRequest request) {
        update(request);
    }

    public Form(HttpServletRequest request, Form form) {
        fields = new ArrayList<>();
        for (Field field : form.getFields()) {
            fields.add(new Field(
                    field.getType(),
                    request.getParameter(field.getInputId()),
                    field.getInputId(),
                    field.getLabel()
            ));
        }
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
            String label = request.getParameter("inputName-" + i);
            fields.add(new Field((String) value, null, "selectName-" + i++, label));
            value = request.getParameter("selectName-" + i);
        }
    }

    public void update(HttpServletRequest request, Form form){
        for (Field field : form.getFields()) {
            field.setValue((Object) request.getParameter(field.getInputId()));
        }
    }

    public String getAsMap(){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Map<String, String>> stringMapMap = new HashMap<>();
        for(Field field : fields){
            Map<String, String> stringMap = new HashMap<>();
            stringMap.put("inputValue", field.getLabel());
            stringMap.put("optionValue", field.getType());
            stringMapMap.put(field.getInputId(), stringMap);


        }
        try {
            return objectMapper.writeValueAsString(stringMapMap);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return "";
        }
    }
    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }
}
