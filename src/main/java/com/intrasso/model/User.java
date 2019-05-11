package com.intrasso.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User extends AuditModel {
    private String firstName;
    private String lastName;
    private String mail;
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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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
    
    public void update(User user){
        this.setMail(user.getMail());
        this.setLastName(user.getLastName());
        this.setFirstName(user.getFirstName());
        this.setMembers(user.getMembers());
    }
}
