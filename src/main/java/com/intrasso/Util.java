package com.intrasso;

import com.intrasso.model.*;
import com.intrasso.repository.AssociationRepository;
import com.intrasso.repository.EventRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        } else if (associationList == null) {
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

    public static <P extends Page, Q extends JpaRepository> List<P> getSome(Queue<P> queue, int size, Q repository) {
        List<P> newList = new ArrayList<>();
        while (size > 0 && queue.size() > 0) {
            P element = queue.remove();
            if(element instanceof Event){
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                System.out.println("endate : " + ((Event) element).getEndDateString("dd-MM-yyyy hh:mm"));
                System.out.println("current date : " + df.format(new Date()));
                if(((Event) element).getEndDate().before(new Date())){
//                    repository.delete((Event)element);
                    System.out.println("event gaved : " + element.getAsMap());
                    System.out.println("event found : " + repository.getOne(element.getId()));
                    repository.deleteById(element.getId());
                    element = null;
                }
            }
            if(element != null) {
                size--;
                newList.add(element);
            }
        }
        return newList;
    }

    public static Map<String, Long> getMapMember(List<Member> memberList) {
        Map<String, Long> memberMap = new HashMap<>();
        for (Member member : memberList) {
            memberMap.put(member.getAssociation().getName(), member.getAssociation().getId());
        }
        return memberMap;
    }

//    public static Map<Long, Map<String, Boolean>> getAutorizations(List<Member> memberList) {
//        Map<Long, Map<String, Boolean>> autorizationMap = new HashMap<>();
//        for (Member member : memberList) {
//            autorizationMap.put(member.getUser().getId(), new HashMap<>());
//            autorizationMap.get(member.getUser().getId())
//
//        }
//    }
}


