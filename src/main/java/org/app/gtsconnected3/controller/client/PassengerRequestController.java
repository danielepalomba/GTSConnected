package org.app.gtsconnected3.controller.client;

import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.entity.User;
import org.app.gtsconnected3.security.CustomUserDetails;
import org.app.gtsconnected3.service.PassengerService;
import org.app.gtsconnected3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class PassengerRequestController {
    private final UserService userService;
    private final PassengerService passengerService;

    @Autowired
    public PassengerRequestController(UserService userService, PassengerService passengerService) {
        this.userService = userService;
        this.passengerService = passengerService;
    }

    @GetMapping("/users/user-details/passenger-requests")
    public String pendingRequest(Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByEmail(userDetails.getUsername());
        model.addAttribute("passengers", passengerService.findAllByUser(user));
        return "passenger-requests";
    }

}
