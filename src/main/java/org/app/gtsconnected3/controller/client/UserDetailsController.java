package org.app.gtsconnected3.controller.client;

import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.entity.User;
import org.app.gtsconnected3.security.CustomUserDetails;
import org.app.gtsconnected3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class UserDetailsController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Autowired
    public UserDetailsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/user-details")
    public String showUserDetails(Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        log.info("User" + user.getEmail() + "details page");
        return "user-details";
    }

    @GetMapping("/users/user-details/change-password")
    public String showChangePassword(Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        log.info("User" + user.getEmail() + "change password page");
        return "change-password";
    }

    @PostMapping("/users/user-details/change-password")
    public String changePassword(@RequestParam(name = "currentPassword") String currentPassword, @RequestParam(name = "newPassword") String newPassword, Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByEmail(userDetails.getUsername());
        log.info(currentPassword);
        log.info(newPassword);
        if (bCryptPasswordEncoder.matches(currentPassword, userDetails.getPassword())) {
            log.info("ci sono!");
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userService.saveUser(user);
            log.info("User" + user.getEmail() + "password changed");
            return "redirect:/users/user-details";
        } else {
            model.addAttribute("error", "Invalid password");
            log.info("User" + user.getEmail() + "password change failed");
            return "redirect:/change-password";
        }
    }

    @GetMapping("/users/user-details/delete-account")
    public String showDeleteAccount(Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByEmail(userDetails.getUsername());
        model.addAttribute("user", user);
        log.info("User" + user.getEmail() + "delete account page");
        return "delete-account";
    }

    @PostMapping("/users/user-details/delete-account")
    public String deleteAccount() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByEmail(userDetails.getUsername());
            userService.removeUser(user);
            return "redirect:/logout";
    }
}
