package com.intrasso.controller;


import com.intrasso.Util;
import com.intrasso.model.*;
import com.intrasso.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Controller
public class AssociationController {

    @Autowired
    private AssociationRepository associationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PageWithFormRepository pageWithFormRepository;
    @Autowired
    private CandidateRepository candidateRepository;

    @GetMapping("/addAssociation")
    public String setAssociation(Model model) {
        System.out.println("show asso form");
        model.addAttribute("association", new Association());
        model.addAttribute("action", "/addAssociation");
        return "association/addAssociation";
    }

    @PostMapping("/addAssociation")
    public String createAssociation(@ModelAttribute Association association, HttpServletRequest request) {
        System.out.println("get asso form");
        if (association != null) {
            System.out.println("asso created or updated");
            User user = userRepository.getOne((Long) request.getSession().getAttribute("userId"));
            association = associationRepository.save(association);
            Member member = new Member();
            member.setRole("Président");
            member.giveAllRights();
            user.addMember(member);
            association.addMember(member);
            memberRepository.save(member);
            request.getSession().setAttribute("memberMap", Util.getMapMember(user.getMembers()));
            return "redirect:/association/" + association.getId();
        }

        return "";
    }

    @GetMapping("/association/{associationId:\\d+}/edit")
    public String editAssociation(@PathVariable long associationId, Model model){
        model.addAttribute("association", associationRepository.getOne(associationId));
        model.addAttribute("action", "/association/" + associationId + "/edit");
        return "association/addAssociation";
    }

    @PostMapping("/association/{associationId:\\d+}/edit")
    public String updateAssociation(@PathVariable long associationId, HttpServletRequest request){
        Association association = associationRepository.getOne(associationId);
        association.update(request);
        associationRepository.save(association);
        return "redirect:/association/" + associationId;
    }

    @GetMapping("/associations")
    public String showAllAssociations(Model model) {
        model.addAttribute("associations", associationRepository.findAll());
        return "association/associations";
    }

    @GetMapping("/association/{associationId:\\d+}")
    public String getAssociation(@PathVariable("associationId") long associationId, Model model, HttpServletRequest request) {
        System.out.println("looking for association");
        Optional<Association> opt = associationRepository.findById(associationId);
        if (opt.isPresent()) {
            System.out.println("association found");
            Association association = opt.get();
            long userId = (Long) request.getSession().getAttribute("userId");
            model.addAttribute("association", association);
            int numberDisplayed = 3;
            String[] typeList = {"event", "publication", "jobvacancy"};
            for (String type : typeList) {
                System.out.println(type + " : " + Util.getSome(Util.getObjects(associationRepository, type, associationId, userId), numberDisplayed, pageWithFormRepository));
                model.addAttribute(type, Util.getSome(Util.getObjects(associationRepository, type, associationId, userId), numberDisplayed, pageWithFormRepository));
            }
            model.addAttribute("members", association.getMembers());
            Member member = Util.getMember(association, userId);
            if(member == null){
                member = new Member();
            }
            model.addAttribute("currentMember", member);
            return "association/showAssociation";
        }
        System.out.println("association not found");
        // TODO no association page
        return "redirect:/addAssociation";
    }

    @GetMapping("/association/{associationId:\\d+}/addMember")
    public String addMember(Model model, @PathVariable("associationId") long associationId, HttpServletRequest request) {
        return manageMember(model, associationId, -1, request);
    }

    @GetMapping("/association/{associationId:\\d+}/editMember/{memberId:\\d+}")
    public String editMember(Model model, @PathVariable long associationId, @PathVariable long memberId, HttpServletRequest request) {
        return manageMember(model, associationId, memberId, request);
    }

    private String manageMember(Model model, long associationId, long memberId, HttpServletRequest request) {
        System.out.println("asso found : " + associationRepository.getOne(associationId));
        model.addAttribute("association", associationRepository.getOne(associationId));
        if (memberId != -1) {
            System.out.println(memberRepository.getOne(memberId));
            model.addAttribute("member", memberRepository.getOne(memberId));
            request.getSession().setAttribute("editedMemberId", memberId);
        } else {
            request.getSession().removeAttribute("editedMemberId");
            model.addAttribute("member", new Member());
        }
        return "association/manageMember";
    }

    @PostMapping("/association/{associationId:\\d+}/addMember")
    public String registerMember(@PathVariable("associationId") long associationId, HttpServletRequest request) {
        Object memberIdObject = request.getSession().getAttribute("editedMemberId");
        Member member;
        if (memberIdObject != null) {
            member = memberRepository.getOne((long) memberIdObject);
            member.update(request);
            memberRepository.save(member);
        } else {
            User user = userRepository.findByEmail(request.getParameter("email")).get(0);
            Association association = associationRepository.getOne(associationId);
            member = new Member(request);
            user.addMember(member);
            association.addMember(member);
            memberRepository.save(member);
            request.getSession().setAttribute("memberMap", Util.getMapMember(user.getMembers()));
        }
        return "redirect:/association/" + associationId;
    }

    @GetMapping("/association/{associationId:\\d+}/deleteMember/{memberId:\\d+}")
    public String deleteMember(@PathVariable long associationId, @PathVariable long memberId) {
        memberRepository.deleteById(memberId);
        return "redirect:/association/" + associationId;
    }

    @GetMapping("/association/{associationId:\\d+}/{type:(?:event|publication|jobVacancy)}/{objectId:\\d+}/showCandidates")
    public String getCandidates(@PathVariable long associationId, @PathVariable String type, @PathVariable long objectId, Model model, HttpServletRequest request){
        PageWithForm pageWithForm = pageWithFormRepository.getOne(objectId);
        model.addAttribute("pageWithForm", pageWithForm);
        model.addAttribute("candidateList", pageWithForm.getCandidateList());
        return "association/showCandidates";
    }

    @PostMapping("/association/{associationId:\\d+}/{type:(?:event|publication|jobvacancy)}/{objectId:\\d+}/candidate/{candidateId:\\d+}/{choice:(?:accept|deny|dismiss|delete)}")
    public @ResponseBody int manageCandidates(@PathVariable long associationId, @PathVariable String type, @PathVariable String choice, @PathVariable long candidateId, HttpServletRequest request){
        System.out.println("value : " + choice);
        Association association = associationRepository.getOne(associationId);
        long userId = (Long) request.getSession().getAttribute("userId");
        Member member = Util.getMember(association, userId);
        Candidate candidate = candidateRepository.getOne(candidateId);

        if((choice.equals("dismiss") || choice.equals("delete")) && userId == candidate.getUser().getId()) {
            candidateRepository.deleteById(candidateId);
        }
        else if(member != null && member.canManageMembers()){
            if(choice.equals("deny")){
                candidate.decline();
                candidateRepository.save(candidate);
            }
            else if(choice.equals("accept")){
                candidate.accept();
                if(type.equals("jobVacancy")){
                    association.addMember(userRepository.getOne(userId), candidate.getPageWithForm().getRole());
                }
                candidateRepository.save(candidate);
            }
            else {
                return 0;
            }
        }
        else {
            return 0;
        }
        return 1;
    }
}