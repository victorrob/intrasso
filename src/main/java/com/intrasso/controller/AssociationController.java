package com.intrasso.controller;


import com.intrasso.model.Association;
import com.intrasso.model.Member;
import com.intrasso.model.User;
import com.intrasso.repository.AssociationRepository;
import com.intrasso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class AssociationController {

    @Autowired
    private AssociationRepository associationRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/addAssociation")
    public String setAssociation(Model model) {
        System.out.println("show asso form");
        model.addAttribute("association", new Association());
        return "association/addAssociation";
    }


    @PostMapping("/addAssociation")
    public String createAssociation(@ModelAttribute Association association, HttpServletRequest request) {
        System.out.println("get asso form");
        if (association != null){
            System.out.println("asso created or updated");
            association = associationRepository.save(association);
            return "redirect:/association/"+ association.getId();
        }

        return "";
    }

    @GetMapping("/association/{associationId:\\d+}")
    public String getAssociation(@PathVariable("associationId") long associationId, Model model){
        System.out.println("looking for association");
        Optional<Association> opt = associationRepository.findById(associationId);
        if(opt.isPresent()) {
            System.out.println("association found");
            Association association = opt.get();
            model.addAttribute("association", association);
            return "association/association";
        }
        System.out.println("association not found");
        // TODO no association page
        return "redirect:/addAssociation";
    }

    @GetMapping("/association/{associationId:\\d+}/addMembers")
    public String addMember(Model model, @PathVariable("associationId") long associationId){
        System.out.println("asso found : " + associationRepository.getOne(associationId));
        model.addAttribute("association", associationRepository.getOne(associationId));
        return "association/addMembers";
    }

    @PostMapping("/association/{associationId:\\d+}/addMembers")
    public String registerMember(@PathVariable("associationId") long associationId, HttpServletRequest request){
        Member member = new Member();
        member.setRole(request.getParameter("role"));

        User user = userRepository.findByEmail(request.getParameter("email")).get(0);
        user.addMember(member);
        userRepository.save(user);

        Association association = associationRepository.getOne(associationId);
        association.addMember(member);
        associationRepository.save(association);
        return "redirect:/association/" + associationId;
    }
}