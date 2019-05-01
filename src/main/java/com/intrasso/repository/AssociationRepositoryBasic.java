package com.intrasso.repository;

import com.intrasso.model.Association;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociationRepositoryBasic extends JpaRepository<Association, Long> {
}