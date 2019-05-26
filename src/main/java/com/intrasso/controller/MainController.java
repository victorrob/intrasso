package com.intrasso.controller;

import com.intrasso.Util;
import com.intrasso.model.Event;
import com.intrasso.repository.AssociationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Queue;

@Controller
public class MainController {
    @Autowired
    private AssociationRepository associationRepository;

    @GetMapping("/{type:events|publications|jobVacancies}")
    public String showAll(@PathVariable String type, Model model){
        Queue<Event> queue = Util.getObjects(associationRepository, type);
        model.addAttribute(type, queue);
        return type + "/" + type;
    }

    @GetMapping("/association/{associationId:\\d+}/{type:events|publications|jobVacancies}")
    public String showObjects(@PathVariable long associationId, @PathVariable String type, Model model){
        model.addAttribute(type, Util.getObjects(associationRepository, type,associationId));
        model.addAttribute("association", associationRepository.getOne(associationId));
        return type + "/" + type;
    }

    @GetMapping("/home")
    public String showHomePag(Model model, HttpServletRequest request){
        int numberDisplayed = 3;
        List<Event> eventList = Util.getSome(Util.getObjects(associationRepository, "events"), numberDisplayed);
        model.addAttribute("events", eventList);
        List<Event> publicationList = Util.getSome(Util.getObjects(associationRepository, "publications"), numberDisplayed);
        model.addAttribute("publications", publicationList);
        List<Event> jobVacancyList = Util.getSome(Util.getObjects(associationRepository, "jobVacancies"), numberDisplayed);
        model.addAttribute("jobVacancies", jobVacancyList);
        return "user/homePage";
    }
}
