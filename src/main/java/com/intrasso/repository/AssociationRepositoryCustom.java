package com.intrasso.repository;

import com.intrasso.model.Association;

public interface AssociationRepositoryCustom {
    void update(Association association);
    Association saveOrUpdate(Association association);
}
