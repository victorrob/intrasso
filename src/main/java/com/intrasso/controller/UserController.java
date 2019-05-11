package com.intrasso.controller;

import com.intrasso.model.User;
import com.intrasso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String createUser(Model model, HttpServletRequest request){
        String mail = request.getParameter("mail");
        String password = request.getParameter("pswd");
        User newUser = new User();
        newUser.setFirstName("john");
        newUser.setLastName("doe");
        newUser.setMail(mail);
        newUser = userRepository.save(newUser);
        model.addAttribute("user", newUser);
        return "user/showUser";
    }
}
