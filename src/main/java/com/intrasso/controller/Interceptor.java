package com.intrasso.controller;

import com.intrasso.model.Association;
import com.intrasso.model.Member;
import com.intrasso.model.User;
import com.intrasso.repository.AssociationRepository;
import com.intrasso.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Transactional
public class Interceptor extends HandlerInterceptorAdapter {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssociationRepository associationRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("interceptor");
        System.out.println(request.getRequestURL());
        URL url = new URL(request.getRequestURL().toString());
        System.out.println(url.getPath());
        HttpSession session = request.getSession();
        Pattern pattern = Pattern.compile("/(?:css|img)/.*");
        if(pattern.matcher(url.getPath()).matches()){
            return super.preHandle(request, response, handler);
        }
        if (!url.getPath().equals("/index.html") && !url.getPath().equals("/login")) {
            if (session == null) {
                System.out.println("no session");
                response.sendRedirect("/");
                return true;
            }
            if (session.getAttribute("userId") == null) {
                System.out.println("no user id");
                response.sendRedirect("/");
                return true;
            }
            User user = userRepository.getOne((Long) session.getAttribute("userId"));
            if (user == null) {
                System.out.println("no registered user");
                response.sendRedirect("/");
                return true;
            }
            pattern = Pattern.compile("/(\\w+)/(\\d+)/(?:(?:add|edit|delete)(Member|Event|Publication|JobVacancy))");
            Matcher matcher = pattern.matcher(url.getPath());
            if (matcher.find()) {
                System.out.println(matcher.group(1));
                System.out.println(matcher.group(2));
                System.out.println(matcher.group(3));
                long associationId = Long.parseLong(matcher.group(2));
                Optional<Association> associationOptional = associationRepository.findById(associationId);
                if (!associationOptional.isPresent()) {
                    System.out.println("asso not found");
                    response.setStatus(403);
                    response.sendRedirect("/403");
                    return true;
                }
                String type = matcher.group(3);
//                Hibernate.initialize(user.getMembers());
//                if(user.getMembers() == null){
//                    System.out.println("no member");
//                    response.setStatus(403);
//                    return true;
//                }
                List<Member> members = user.getMembers();
                Member member = null;
                for (Member element : members) {
                    if (associationId == element.getAssociation().getId()) {
                        member = element;
                    }
                }
                if (member == null) {
                    System.out.println("not a member");
                    response.setStatus(403);
                    response.sendRedirect("/403");
                    return true;
                }
                boolean canContinue;
                switch (type) {
                    case "Event":
                        canContinue = member.canEditEvent();
                        break;
                    case "Publication":
                        canContinue = member.canEditPublication();
                        break;
                    case "JobVacancy":
                        canContinue = member.canEditJobVacancy();
                        break;
                    case "Association":
                        canContinue = member.canEditAssociation();
                        break;
                    case "Member":
                        canContinue = member.canManageMembers();
                        break;
                    default:
                        canContinue = false;
                }
                if (!canContinue) {
                    System.out.println("can't continue");
                    response.setStatus(403);
                    response.sendRedirect("/403");
                    return true;
                }

            }
        }
        return super.preHandle(request, response, handler);
    }


}
