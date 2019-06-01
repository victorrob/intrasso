package com.intrasso.controller;


import com.intrasso.Util;
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
        model.addAttribute("event", new Event());
        model.addAttribute("association", associationRepository.getOne(associationId));
        return "events/addEvent";
    }

    @GetMapping("/association/{associationId:\\d+}/editEvent/{eventId:\\d+}")
    public String editAssociation(Model model, @PathVariable long associationId, @PathVariable long eventId, HttpServletRequest request) {
        request.getSession().setAttribute("eventId", eventId);
        model.addAttribute("event", eventRepository.getOne(eventId));
        model.addAttribute("association", associationRepository.getOne(associationId));
        return "events/addEvent";
    }


    @PostMapping("/association/{associationId:\\d+}/addEvent")
    public String createEvent(@ModelAttribute Event event, @PathVariable long associationId, HttpServletRequest request) {
        System.out.println("get events form");
        long eventId = (long) request.getSession().getAttribute("eventId");
        if (event == null) {
            request.getSession().removeAttribute("eventId");
            eventRepository.getOne(eventId).update(event);
            return "";
        }
        Association association = associationRepository.getOne(associationId);

        if (event.getId() != null) {
            System.out.println("update");
            eventRepository.save(event);
            return "redirect:/association/" + association.getId();
        }
        String dateString = request.getParameter("endDateString");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = df.parse(dateString);
        } catch (java.text.ParseException e) {
            System.out.println("aie");
        }
        System.out.println("date = " + date.toString());
        event.setEndDate(date);
        association.addEvent(event);
//            eventRepository.save(events);
        associationRepository.save(association);
        System.out.println("events created");
        return "redirect:/association/" + association.getId();
    }


    @GetMapping("/association/{associationId:\\d+}/events/{eventId:\\d+}")
    public String showEvent(Model model, @PathVariable long associationId, @PathVariable long eventId) {
        System.out.println("events found : " + eventRepository.getOne(eventId));
        model.addAttribute("event", eventRepository.getOne(eventId));
        model.addAttribute("association", associationRepository.getOne(associationId));
        return "events/showEvent";
    }

    @PostMapping("/association/{associationId:\\d+}/event/{eventId:\\d+}")
    public String editEvent(Model model, @PathVariable long associationId, @PathVariable long eventId, @ModelAttribute Event event) {
        System.out.println("start update");
        if (event != null) {
            eventRepository.save(event);
            System.out.println("events updated");
            return "redirect:/association/" + associationId + "/events/" + eventId;
        }
        return "";
    }
}