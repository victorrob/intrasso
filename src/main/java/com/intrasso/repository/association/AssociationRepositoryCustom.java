package com.intrasso.repository.association;

import com.intrasso.model.Association;

public interface AssociationRepositoryCustom {
    void update(Association association);
    Association saveOrUpdate(Association association);
}
