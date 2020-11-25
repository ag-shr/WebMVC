package com.webapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
@Controller
public class ViewController {

    @GetMapping(path = "signUp")
    public String getRegisterPage(Principal principal) {
        if (principal != null)
            return "index";
        return "register";
    }

    @GetMapping(path = "login")
    public String getLoginPage(Principal principal) {
        if (principal != null)
            return "index";
        return "login";
    }

    @GetMapping("forgot")
    public String forgot(Principal principal) {
        if (principal != null)
            return "index";
        return "forgotPassword";
    }

    @GetMapping("reset")
    public String reset(Principal principal) {
        if (principal != null)
            return "index";
        return "resetPassword";
    }

    @GetMapping("changePassword")
    public String changePassword() {
        return "changePassword";
    }
}
