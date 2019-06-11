package com.intrasso.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intrasso.Util;

import javax.persistence.*;

@Entity
@Table(name = "field")
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String type;
    private String label;
    private String value;
    private String inputId;

    public Field(){
        type = "";
        value = "";
        inputId = "";
        label = "";
    }

    public Field(String type, Object object, String inputId){
        this.type = type;
        label = "";
        ObjectMapper objectMapper = new ObjectMapper();
        value = Util.objectToString(object);
        this.inputId = inputId;
    }
    public Field(String type, Object object, String inputId, String label){
        this.type = type;
        this.label = label;
        ObjectMapper objectMapper = new ObjectMapper();
        value = Util.objectToString(object);
        this.inputId = inputId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return Util.stringToObject(value, getTypeReference());
    }

    public void setValue(Object value) {
        this.value = Util.objectToString(value);;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TypeReference getTypeReference(){
        switch (type){
            default:
                return new TypeReference<String>() {};
        }
    }

    public String getInputId() {
        return inputId;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }
}
