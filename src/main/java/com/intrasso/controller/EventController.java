package com.intrasso.controller;


import com.intrasso.model.Association;
import com.intrasso.model.Event;
import com.intrasso.model.Member;
import com.intrasso.model.User;
import com.intrasso.repository.AssociationRepository;
import com.intrasso.repository.EventRepository;
import com.intrasso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class EventController {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private AssociationRepository associationRepository;

    @GetMapping("/association/{associationId:\\d+}/addEvent")
    public String setAssociation(Model model, @PathVariable long associationId) {
        System.out.println("show event form");
        model.addAttribute("event", new Event());
        model.addAttribute("association", associationRepository.getOne(associationId));
        return "event/addEvent";
    }


    @PostMapping("/association/{associationId:\\d+}/addEvent")
    public String createEvent(@ModelAttribute Event event, @PathVariable long associationId, HttpServletRequest request) {
        System.out.println("get event form");

        if (event != null) {
            String dateString = request.getParameter("endDateString");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            try {
                date = df.parse(dateString);
            }
            catch (java.text.ParseException e){
                System.out.println("aie");
            }
            System.out.println("date = " + date.toString());
            event.setEndDate(date);
            Association association = associationRepository.getOne(associationId);
            association.addEvent(event);
//            eventRepository.save(event);
            associationRepository.save(association);
            System.out.println("event created");
            return "redirect:/association/" + association.getId();
        }

        return "";
    }

    @GetMapping("/association/{associationId:\\d+}/events")
    public String showAssociationEvents(Model model, @PathVariable long associationId) {
        System.out.println("show all association event");
        Queue<Event> events = new PriorityQueue<>(Comparator.comparing(Event::getEndDate));
        Association association = associationRepository.getOne(associationId);
        model.addAttribute("association", association);
        System.out.println(association.getEvents());
        System.out.println(events);
        List<Event> list = association.getEvents();
        list.addAll(events);
        System.out.println(list);
        events.addAll(list);
        System.out.println(association.getEvents());
        model.addAttribute("events", events);
        return "event/events";
    }

    @GetMapping("/events")
    public String showAllEvents(Model model) {
        System.out.println("show all events");
        Queue<Event> events = new PriorityQueue<>(Comparator.comparing(Event::getEndDate));
        List<Association> associations = associationRepository.findAll();
        for (Association association : associations) {
            events.addAll(association.getEvents());
        }
        model.addAttribute("events", events);
        model.addAttribute("association", null);
        return "event/events";
    }

    @GetMapping("/association/{associationId:\\d+}/events/{eventId:\\d+}")
    public String showEvent(Model model, @PathVariable long associationId, @PathVariable long eventId) {
        System.out.println("event found : " + eventRepository.getOne(eventId));
        model.addAttribute("event", eventRepository.getOne(eventId));
        model.addAttribute("association", associationRepository.getOne(associationId));
        return "event/showEvent";
    }

    @PostMapping("/association/{associationId:\\d+}/event/{eventId:\\d+}")
    public String editEvent(Model model, @PathVariable long associationId, @PathVariable long eventId, @ModelAttribute Event event) {
        System.out.println("start update");
        if (event != null) {
            eventRepository.save(event);
            System.out.println("event updated");
            return "redirect:/association/" + associationId + "/event/" + eventId;
        }
        return "";
    }
}