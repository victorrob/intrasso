package com.intrasso.controller;

import com.intrasso.model.User;
import com.intrasso.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String createUser(Model model, HttpServletRequest request){
        String mail = request.getParameter("mail");
        String password = request.getParameter("pswd");
        List<User> userList = userRepository.findByMail(mail);
        User newUser;
        if(userList.isEmpty()) {
            newUser = new User();
            newUser.setFirstName("john");
            newUser.setLastName("doe");
            newUser.setMail(mail);
            newUser = userRepository.save(newUser);
        }
        else{
            newUser = userList.get(0);
        }
        HttpSession session = request.getSession();
        session.setAttribute("userId", newUser.getId());
        return "redirect:/user";
    }

    @GetMapping("/user")
    public String showUser(Model model, HttpServletRequest request){
        long userId = (long) request.getSession().getAttribute("userId");
        User user = userRepository.getOne(userId);
        model.addAttribute("user", user);
        return "user/showUser";
    }


}
