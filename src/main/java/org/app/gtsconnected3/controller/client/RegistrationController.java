package org.app.gtsconnected3.controller.client;

import jakarta.servlet.http.HttpServletRequest;
import org.app.gtsconnected3.dto.RegistrationForm;
import org.app.gtsconnected3.entity.User;
import org.app.gtsconnected3.exception.EmailAlredyExistsException;
import org.app.gtsconnected3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("registrationForm", new RegistrationForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegistrationForm registrationForm, Model model, HttpServletRequest request) {
        try {
            User user = new User();
            String siteURL = request.getRequestURL().toString();
            userService.registerUser(user, siteURL, registrationForm);
            return "email-notification";
        } catch (EmailAlredyExistsException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/register/verify-account")
    public String verifyUser(@RequestParam("token") String token) {
        userService.verifyUser(token);
        return "verify-account";
    }

}