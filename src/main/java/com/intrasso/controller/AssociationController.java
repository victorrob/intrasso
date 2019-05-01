package com.intrasso.controller;


import com.intrasso.repository.AssociationRepository;
import com.intrasso.model.Association;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class AssociationController {

    @Autowired
    private AssociationRepository associationRepository;

    @GetMapping("/association")
    public String setAssociation(Model model) {
        System.out.println("base");
        Association association = new Association();
        model.addAttribute("association", association);
        return "association";
    }


    @PostMapping("/association")
    public String createAssociation(@ModelAttribute Association association) {
        association = associationRepository.saveOrUpdate(association);
        if (association != null){
            return "redirect:/association/"+ association.getId();
        }

        return "";
    }

    @RequestMapping(value = "/association/{associationId:[\\d]}", method = RequestMethod.GET)
    public String getAssociation(@PathVariable("associationId") long associationId, Model model){
        System.out.println(associationId);
        Optional<Association> opt = associationRepository.findById(associationId);
        System.out.println(associationRepository.findAll());
        if(opt.isPresent()) {
            System.out.println("find");
            Association association = opt.get();
            model.addAttribute("association", association);
            return "result";
        }
        System.out.println("nothing");
        // TODO no association page
        return "association";
    }


//    @PutMapping("/questions/{questionId}")
//    public Association updateQuestion(@PathVariable Long questionId,
//                                   @Valid @RequestBody Association associationRequest) {
//        return associationRepository.findById(questionId)
//                .map(association -> {
//                    association.setName(associationRequest.setName(););
//                    association.setDescription(associationRequest.getDescription());
//                    return associationRepository.save(association);
//                }).orElseThrow(() -> new ResourceNotFoundException("Association not found with id " + questionId));
//    }
//
//
//    @DeleteMapping("/questions/{questionId}")
//    public ResponseEntity<?> deleteQuestion(@PathVariable Long questionId) {
//        return associationRepository.findById(questionId)
//                .map(association -> {
//                    associationRepository.delete(association);
//                    return ResponseEntity.ok().build();
//                }).orElseThrow(() -> new ResourceNotFoundException("Association not found with id " + questionId));
//    }
}