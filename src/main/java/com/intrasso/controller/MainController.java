package com.intrasso.controller;

import com.intrasso.Util;
import com.intrasso.model.Event;
import com.intrasso.repository.AssociationRepository;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
