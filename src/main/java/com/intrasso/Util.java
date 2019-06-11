package com.intrasso;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intrasso.model.*;
import com.intrasso.repository.AssociationRepository;
import com.intrasso.repository.PageWithFormRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Util {
    public static Queue<PageWithForm> getObjects(AssociationRepository repository, String type, List<Association> associationList, long id) {
        Queue<PageWithForm> objectQueue;
        if (type.equals("events")) {
            objectQueue = new PriorityQueue<>(Comparator.comparing(PageWithForm::getEndDate));
        } else {
            objectQueue = new PriorityQueue<>(Comparator.comparing(PageWithForm::getCreatedAt));
        }
        if (id != -1) {
            associationList.add(repository.getOne(id));
        } else if (associationList == null) {
            associationList = repository.findAll();
        }
        for (Association association : associationList) {
            objectQueue.addAll(association.getByType(type));
        }
        return objectQueue;
    }

    public static Queue<PageWithForm> getObjects(AssociationRepository repository, String type, List<Association> associationList) {
        return Util.getObjects(repository, type, associationList, -1);
    }

    public static Queue<PageWithForm> getObjects(AssociationRepository repository, String type, long id) {
        return Util.getObjects(repository, type, new ArrayList<>(), id);
    }

    public static Queue<PageWithForm> getObjects(AssociationRepository repository, String type) {
        return Util.getObjects(repository, type, null, -1);
    }

    public static List<PageWithForm> getSome(Queue<PageWithForm> queue, int size, PageWithFormRepository repository) {
        List<PageWithForm> newList = new ArrayList<>();
        while (size > 0 && queue.size() > 0) {
            PageWithForm element = queue.remove();
            if (element.getType().equals("event")) {
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                if (element.getEndDate().before(new Date())) {
                    repository.deleteById(element.getId());
                    element = null;
                }
            }
            if (element != null) {
                size--;
                newList.add(element);
            }
        }
        return newList;
    }

    public static Member getMember(Association association, long userId){
        for(Member member : association.getMembers()){
            if(member.getUser().getId() == userId){
                return member;
            }
        }
        return null;
    }

    public static Map<String, Long> getMapMember(List<Member> memberList) {
        Map<String, Long> memberMap = new HashMap<>();
        for (Member member : memberList) {
            memberMap.put(member.getAssociation().getName(), member.getAssociation().getId());
        }
        return memberMap;
    }

    public static String objectToString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            System.out.println("error : " + e.getMessage());
        }
        return "";
    }

    public static Object stringToObject(String json, TypeReference typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            System.out.println("error : " + e.getMessage());
        }
        return null;
    }
}


