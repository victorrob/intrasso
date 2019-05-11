package com.intrasso.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "jobVacancy")
public class JobVacancy extends PageWithForm {
    @OneToMany
    List<RegisteredUser> candidates;
    String role;
}
