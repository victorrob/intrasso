package com.intrasso.model;

import com.intrasso.LDAP.LDAPObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User extends AuditModel {
    private String firstName;
    private String lastName;
    private String login;
    private String type;
    private String number;
    private String email;
    @OneToMany(
            mappedBy = "student",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Member> members;

    public User(){
        this.members = new ArrayList<>();
        this.firstName = "";
        this.lastName = "";
        this.login = "";
        this.type = "";
        this.number = "";

    }

    public User(LDAPObject ldapObject){
        this.firstName = ldapObject.getPrenom();
        this.lastName = ldapObject.getNomFamille();
        this.email = ldapObject.getEmail();
        this.type = ldapObject.getType();
        this.number = ldapObject.getNumber();
        this.login = ldapObject.getLogin();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public void addMember(Member member){
        this.members.add(member);
        member.setStudent(this);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void update(User user){
        this.setEmail(user.getEmail());
        this.setLastName(user.getLastName());
        this.setFirstName(user.getFirstName());
        this.setMembers(user.getMembers());
    }
}
