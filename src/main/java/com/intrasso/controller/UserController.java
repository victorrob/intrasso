package com.intrasso.controller;

import com.intrasso.LDAP.LDAPObject;
import com.intrasso.LDAP.LDAPaccess;
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
    public String createUser(HttpServletRequest request) {
        String user = request.getParameter("user");
        String password = request.getParameter("pswd");
        LDAPaccess ldaPaccess = new LDAPaccess();
        LDAPObject ldapObject = new LDAPObject();
        //TODO update
        //TODO difference between the students and the others.
//        try {
//            ldapObject = ldaPaccess.LDAPget(user, password);
//            System.out.println("log successful");
//        }
//        catch (Exception e) {
//            e.getCause();
//            return "redirect:/";
//        }
        ldapObject.mail = "victor.robert@isep.fr";
        List<User> userList = userRepository.findByEmail(ldapObject.getEmail());
        User newUser;
        if (userList.isEmpty()) {
            newUser = new User(ldapObject);
            newUser = userRepository.save(newUser);
        } else {
            newUser = userList.get(0);
        }
        HttpSession session = request.getSession();
        session.setAttribute("userId", newUser.getId());
        return "redirect:/user";
    }

    @GetMapping("/user")
    public String showUser(Model model, HttpServletRequest request) {
        long userId = (long) request.getSession().getAttribute("userId");
        User user = userRepository.getOne(userId);
        model.addAttribute("user", user);
        return "user/showUser";
    }


}
