package com.intrasso.controller;

import com.intrasso.Util;
import com.intrasso.model.*;
import com.intrasso.repository.AssociationRepository;
import com.intrasso.repository.FormRepository;
import com.intrasso.repository.PageWithFormRepository;
import com.intrasso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.List;
import java.util.Queue;

@Controller
public class MainController {
    @Autowired
    private AssociationRepository associationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private PageWithFormRepository pageWithFormRepository;

    @GetMapping("/{type:event|publication|jobVacancy}")
    public String showAll(@PathVariable String type, Model model, HttpServletRequest request) {
        Queue<PageWithForm> queue = Util.getObjects(associationRepository, type, (Long) request.getSession().getAttribute("userId"));
        model.addAttribute(type, queue);
        return type + "/" + type;
    }

    @GetMapping("/association/{associationId:\\d+}/{type:event|publication|jobVacancy}")
    public String showObjects(@PathVariable long associationId, @PathVariable String type, Model model) {
        model.addAttribute(type, Util.getObjects(associationRepository, type, associationId));
        model.addAttribute("association", associationRepository.getOne(associationId));
        return type + "/" + type;
    }

    @GetMapping("/association/{associationId:\\d+}/{type:event|publication|jobVacancy}/{objectId:\\d+}")
    public String showOneObject(@PathVariable long objectId, @PathVariable String type, Model model, HttpServletRequest request) {
        PageWithForm pageWithForm = pageWithFormRepository.getOne(objectId);
        model.addAttribute("member", Util.getMember(pageWithForm.getAssociation(), (Long) request.getSession().getAttribute("userId")));
        model.addAttribute(type, pageWithForm);
        return type + "/show" + StringUtils.capitalize(type);
    }

    @GetMapping("/association/{associationId:\\d+}/{type:add(?:Event|Publication|JobVacancy)}")
    public String addObject(@PathVariable long associationId, @PathVariable String type, Model model) {
        return manageObject(associationId, type.replace("add", "edit"), model, -1, null);
    }

    @GetMapping("/association/{associationId:\\d+}/{type:edit(?:Event|Publication|JobVacancy)}/{objectId:\\d+}")
    public String editObject(@PathVariable long associationId, @PathVariable String type, Model model, @PathVariable long objectId, HttpServletRequest request) {
        return manageObject(associationId, type, model, objectId, request);
    }

    private String manageObject(long associationId, String type, Model model, long objectId, HttpServletRequest request) {
        boolean edit = objectId != -1;
        type = type.replaceAll("(edit|add)(\\w+)", "$2");
        if (edit) {
            request.getSession().setAttribute("pageWithFormId", objectId);
        }
        System.out.println("type = " + type);
        Association association = associationRepository.getOne(associationId);
        model.addAttribute("association", association);
        model.addAttribute(type.toLowerCase(), ((edit) ? pageWithFormRepository.getOne(objectId) : new PageWithForm(type)));
        return type.toLowerCase() + "/add" + type;
    }

    @PostMapping("/association/{associationId:\\d+}/{type:edit(?:Event|Publication|JobVacancy)}")
    public String editObject(@PathVariable long associationId, HttpServletRequest request, @PathVariable String type) {
        Association association = associationRepository.getOne(associationId);
        type = type.replace("edit", "").toLowerCase();
        if (request.getSession().getAttribute("pageWithFormId") != null) {
            long objectId = (long) request.getSession().getAttribute("pageWithFormId");
            request.getSession().removeAttribute("pageWithFormId");
            PageWithForm pageWithForm = pageWithFormRepository.getOne(objectId);
            pageWithForm.update(request);
            pageWithFormRepository.save(pageWithForm);
            return "redirect:/association/" + association.getId();
        }
        PageWithForm pageWithForm = new PageWithForm(request, type);
        if (request.getParameter("selectName-0") != null) {
            System.out.println("request is not null");
            Form form = new Form(request);
            for (Field field : form.getFields()) {
                System.out.println("type : " + field.getType());
            }
            formRepository.save(form);
            pageWithForm.setForm(form);
        }
        association.addPageWithForm(pageWithForm);
        associationRepository.save(association);
        return "redirect:/association/" + association.getId();
    }

    @GetMapping("/home")
    public String showHomePag(Model model, HttpServletRequest request) {
        int numberDisplayed = 3;
        long userId = (Long) request.getSession().getAttribute("userId");
        String[] typeList = {"event", "publication", "jobvacancy"};
        model.addAttribute("candidateList", userRepository.getOne(userId).getCandidateList());
        for (String type : typeList) {
            model.addAttribute(type, Util.getSome(Util.getObjects(associationRepository, type, userId), numberDisplayed, pageWithFormRepository));
        }
        return "user/homePage";
    }

    @GetMapping("/association/{associationId:\\d+}/{type:event|publication|jobVacancy}/{objectId:\\d++}/form/{formId:\\d+}/{end:(?:show|edit)}")
    public String showEvent(@PathVariable long associationId, @PathVariable String type, @PathVariable long objectId, @PathVariable long formId, @PathVariable String end, Model model, HttpServletRequest request) {
        model.addAttribute("associationId", associationId);
        model.addAttribute("type", type);
        PageWithForm pageWithForm = pageWithFormRepository.getOne(objectId);
        Form form = formRepository.getOne(formId);
        Candidate candidate;
        if(end.equals("show")) {
            candidate = form.getCandidate();
        }
        else {
            candidate = Util.getCandidate(pageWithForm, request);
        }
        String title = pageWithForm.getName();
        boolean editable = form.getCandidate() == null;

        if (candidate != null) {
            if(!end.equals("show")) {
                form = candidate.getForm();
                editable = true;
            }
            title = candidate.getUser().getFirstName() + " " + candidate.getUser().getLastName();
        }
        model.addAttribute("title", StringUtils.capitalize(title));
        model.addAttribute("editable", editable);
        model.addAttribute("form", form);
        return "form/showForm";
    }

    @PostMapping("/association/{associationId:\\d+}/{type:event|publication|jobVacancy}/{objectId:\\d++}/setForm/{formId:\\d+}")
    public String editEvent(@PathVariable long associationId, @PathVariable String type, @PathVariable long formId, HttpServletRequest request) {
        Form form = formRepository.getOne(formId);
        if (form.getCandidate() != null && form.getCandidate().getUser().getId().equals(request.getSession().getAttribute("userId"))) {
            form.update(request, form);
            formRepository.save(form);
        } else {
            Form newForm = new Form(request, form);
            User user = userRepository.getOne((Long) request.getSession().getAttribute("userId"));
            Candidate candidate = new Candidate(newForm, user);
            form.getPageWithForm().addCandidate(candidate);
            formRepository.save(newForm);
        }
        return "redirect:/association/" + associationId;
    }

}
