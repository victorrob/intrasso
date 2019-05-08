package com.intrasso.controller;


import com.intrasso.repository.association.AssociationRepository;
import com.intrasso.model.Association;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class AssociationController {

    @Autowired
    private AssociationRepository associationRepository;

    @GetMapping("/association")
    public String setAssociation(Model model) {
        System.out.println("show asso form");
        Association association = new Association();
        model.addAttribute("association", association);
        return "association";
    }


    @PostMapping("/association")
    public String createAssociation(@ModelAttribute Association association, HttpServletRequest request) {
        System.out.println("get asso form");
        association = associationRepository.saveOrUpdate(association);
        if (association != null){
            System.out.println("asso created or updated");
            association = associationRepository.saveOrUpdate(association);
            return "redirect:/association/"+ association.getId();
        }

        return "";
    }

    @RequestMapping(value = "/association/{associationId:\\d+}", method = RequestMethod.GET)
    public String getAssociation(@PathVariable("associationId") long associationId, Model model){
        System.out.println("looking for association");
        Optional<Association> opt = associationRepository.findById(associationId);
        if(opt.isPresent()) {
            System.out.println("association found");
            Association association = opt.get();
            model.addAttribute("association", association);
            return "result";
        }
        System.out.println("association not found");
        // TODO no association page
        return "redirect:/association";
    }
}