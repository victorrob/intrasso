package com.intrasso;

import com.intrasso.model.Association;
import com.intrasso.model.AuditModel;
import com.intrasso.model.Event;
import com.intrasso.model.PageWithForm;
import com.intrasso.repository.AssociationRepository;
import com.intrasso.repository.EventRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public class Util {
    public static <P extends AuditModel> Queue<P> getObjects(AssociationRepository repository, String type, List<Association> associationList, long id) {
        Queue<P> objectQueue;
        if (type.equals("events")) {
            objectQueue = new PriorityQueue(Comparator.comparing(Event::getEndDate));
        } else {
            objectQueue = new PriorityQueue<>(Comparator.comparing(P::getCreatedAt));
        }
        if (id != -1) {
            associationList.add(repository.getOne(id));
        }
        else if (associationList == null){
            associationList = repository.findAll();
        }
        for (Association association : associationList) {
            switch (type) {
                case "events":
                    objectQueue.addAll((List<P>) association.getEvents());
                    break;
                case "publications":
                    objectQueue.addAll((List<P>) association.getPublications());
                    break;
                case "jobVacancies":
                    objectQueue.addAll((List<P>) association.getJobVacancies());
                    break;
            }
        }
        return objectQueue;
    }

    public static <P extends AuditModel> Queue<P> getObjects(AssociationRepository repository, String type, List<Association> associationList) {
        return Util.getObjects(repository, type, associationList, -1);
    }

    public static <P extends AuditModel> Queue<P> getObjects(AssociationRepository repository, String type, long id) {
        return Util.getObjects(repository, type, new ArrayList<>(), id);
    }
    public static <P extends AuditModel> Queue<P> getObjects(AssociationRepository repository, String type) {
        return Util.getObjects(repository, type, null, -1);
    }
}


