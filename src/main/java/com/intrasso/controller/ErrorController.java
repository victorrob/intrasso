package com.intrasso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ErrorController {
    @GetMapping("/{errorCode:\\d{3}}")
    public String returnError(@PathVariable String errorCode){
        return "error/" + errorCode;
    }
}
