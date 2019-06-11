//package com.intrasso.controller;
//
//
//import com.intrasso.Util;
//import com.intrasso.model.Association;
//import com.intrasso.model.Event;
//import com.intrasso.model.Member;
//import com.intrasso.model.User;
//import com.intrasso.repository.AssociationRepository;
//import com.intrasso.repository.EventRepository;
//import com.intrasso.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import javax.servlet.http.HttpServletRequest;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@Controller
//public class EventController {
//
//    @Autowired
//    private EventRepository eventRepository;
//    @Autowired
//    private AssociationRepository associationRepository;
//
//    @GetMapping("/association/{associationId:\\d+}/events/{eventId:\\d+}")
//    public String showEvent(Model model, @PathVariable long associationId, @PathVariable long eventId) {
//        System.out.println("events found : " + eventRepository.getOne(eventId));
//        model.addAttribute("event", eventRepository.getOne(eventId));
//        model.addAttribute("association", associationRepository.getOne(associationId));
//        return "events/showEvent";
//    }
//
//    @PostMapping("/association/{associationId:\\d+}/event/{eventId:\\d+}")
//    public String editEvent(Model model, @PathVariable long associationId, @PathVariable long eventId, @ModelAttribute Event event) {
//        System.out.println("start update");
//        if (event != null) {
//            eventRepository.save(event);
//            System.out.println("events updated");
//            return "redirect:/association/" + associationId + "/events/" + eventId;
//        }
//        return "";
//    }
//}