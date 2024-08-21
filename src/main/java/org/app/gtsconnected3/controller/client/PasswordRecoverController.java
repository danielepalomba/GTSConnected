package org.app.gtsconnected3.controller.client;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.entity.User;
import org.app.gtsconnected3.service.PasswordResetService;
import org.app.gtsconnected3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class PasswordRecoverController {

    private final UserService userService;
    private final PasswordResetService passwordResetService;

    @Autowired
    public PasswordRecoverController(UserService userService, PasswordResetService passwordResetService) {
        this.userService = userService;
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/lost-password")
    public String lostPassword() {
        log.info("GET /lost-password");
        return "lost-password";
    }

    @PostMapping("/lost-password")
    public String recoverPassword(@RequestParam String email, HttpServletRequest request) {
        if(!userService.emailExists(email))
            return "redirect:/email-not-found?email=" + email;

        String siteURL = request.getRequestURL().toString();
        userService.startPasswordRecovery(email, siteURL);
        return "redirect:/info";
    }

    @GetMapping("/email-not-found")
    public String emailNotFound(Model model, @RequestParam String email){
        model.addAttribute("error", "Non risulta nessun utente associato alla email: " + email);
        return "error-info";
    }

    @GetMapping("/info")
    public String info(Model model) {
        model.addAttribute("info", "Ti abbiamo inviato un'email con le istruzioni per il recupero della password");
        return "info";
    }


    @GetMapping("lost-password/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        User user = passwordResetService.get(token);
        if (user == null) {
            model.addAttribute("error", "Invalid token");
            return "error-info";
        }

        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPasswordForm(@RequestParam("token") String token, @RequestParam("password") String password, Model model) {
        User user = passwordResetService.get(token);
        if (user == null) {
            model.addAttribute("error", "Invalid token");
            return "error-info";
        }

        passwordResetService.updatePassword(user, password);
        return "redirect:/login";
    }

}
