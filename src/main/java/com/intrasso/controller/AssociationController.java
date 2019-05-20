package com.intrasso.controller;


import com.intrasso.model.Association;
import com.intrasso.model.Member;
import com.intrasso.model.User;
import com.intrasso.repository.AssociationRepository;
import com.intrasso.repository.MemberRepository;
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
    @Autowired
    private MemberRepository memberRepository;

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

    @GetMapping("/association/{associationId:\\d+}/addMember")
    public String addMember(Model model, @PathVariable("associationId") long associationId, HttpServletRequest request){
        return manageMember(model, associationId, -1, request);
    }

    @GetMapping("/association/{associationId:\\d+}/editMember/{memberId:\\d+}")
    public String editMember(Model model, @PathVariable long associationId, @PathVariable long memberId, HttpServletRequest request){
        return manageMember(model, associationId, memberId, request);
    }

    private String manageMember(Model model, long associationId, long memberId, HttpServletRequest request){
        System.out.println("asso found : " + associationRepository.getOne(associationId));
        model.addAttribute("association", associationRepository.getOne(associationId));
        if (memberId != -1) {
            System.out.println(memberRepository.getOne(memberId));
            model.addAttribute("member", memberRepository.getOne(memberId));
            request.getSession().setAttribute("editedMemberId", memberId);
        }
        else{
            request.getSession().removeAttribute("editedMemberId");
            model.addAttribute("member", new Member());
        }
        return "association/manageMember";
    }

    @PostMapping("/association/{associationId:\\d+}/addMember")
    public String registerMember(@PathVariable("associationId") long associationId, HttpServletRequest request){
        Object memberIdObject = request.getSession().getAttribute("editedMemberId");
        Member member;
        if (memberIdObject != null){
            member = memberRepository.getOne((long) memberIdObject);
            member.update(request);
            memberRepository.save(member);
        }
        else {
            User user = userRepository.findByEmail(request.getParameter("email")).get(0);
            Association association = associationRepository.getOne(associationId);
            member = new Member(request);
            user.addMember(member);
            association.addMember(member);
            memberRepository.save(member);
        }
        return "redirect:/association/" + associationId;
    }

    @GetMapping("/association/{associationId:\\d+}/deleteMember/{memberId:\\d+}")
    public String deleteMember(@PathVariable long associationId, @PathVariable long memberId){
        memberRepository.deleteById(memberId);
        return "redirect:/association/" + associationId;
    }
}