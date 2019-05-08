package com.intrasso.model;

import javax.persistence.OneToMany;
import java.util.List;

public class JobVacancy extends PageWithForm {
    @OneToMany
    List<RegistredUser> candidates;
    String role;
}
