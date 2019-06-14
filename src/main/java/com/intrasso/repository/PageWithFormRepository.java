package com.intrasso.repository;

import com.intrasso.model.PageWithForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageWithFormRepository extends JpaRepository<PageWithForm, Long> {
}
