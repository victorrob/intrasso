package com.intrasso.repository.association;

import com.intrasso.model.Association;

import javax.transaction.Transactional;
import java.util.Optional;

public class AssociationRepositoryCustomImpl implements AssociationRepositoryCustom {
    private final AssociationRepositoryBasic associationRepositoryBasic;

    public AssociationRepositoryCustomImpl(AssociationRepositoryBasic associationRepositoryBasic){
        this.associationRepositoryBasic = associationRepositoryBasic;
    }
    @Transactional
    public void update(Association association){
        Optional<Association> opt = associationRepositoryBasic.findById(association.getId());
        if(opt.isPresent()) {
            Association source = opt.get();
            source.setName(association.getName());
            source.setDescription(association.getDescription());
            source.setEssais(association.getEssais());
        }

    }
    @Transactional
    public Association saveOrUpdate(Association association) {
        if (association != null) {
            if (association.getId() != null && associationRepositoryBasic.findById(association.getId()).isPresent()) {
                this.update(association);
                return association;
            } else {
                return associationRepositoryBasic.save(association);
            }
        }
        return null;
    }
}
