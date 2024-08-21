package org.app.gtsconnected3.controller.client;

import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Message;
import org.app.gtsconnected3.entity.Passenger;
import org.app.gtsconnected3.entity.User;
import org.app.gtsconnected3.security.CustomUserDetails;
import org.app.gtsconnected3.service.BikerService;
import org.app.gtsconnected3.service.MessageService;
import org.app.gtsconnected3.service.PassengerService;
import org.app.gtsconnected3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
public class BikerChatController {
    private final UserService userService;
    private final PassengerService passengerService;
    private final MessageService messageService;
    private final BikerService bikerService;

    @Autowired
    public BikerChatController(UserService userService, PassengerService passengerService, MessageService messageService, BikerService bikerService) {
        this.userService = userService;
        this.passengerService = passengerService;
        this.messageService = messageService;
        this.bikerService = bikerService;
    }

    @GetMapping("/users/user-details/manage-book/chat/{passengerId}")
    public String chat(Model model, @PathVariable Long passengerId) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByEmail(userDetails.getUsername());

        Passenger passenger = passengerService.findPassengerById(passengerId);
        log.info("PASSENGER: " + passenger.getUser().getEmail());

        model.addAttribute("currentUser", user);
        model.addAttribute("passenger", passenger);
        model.addAttribute("tripId", passenger.getBiker().getTrip().getId());
        model.addAttribute("messages", messageService.getMessagesBySenderOrReceiver(passenger.getBiker(), passenger));
        return "chat";
    }

    @GetMapping("/users/user-details/passenger-requests/chat/{bikerId}")
    public String pchat(Model model, @PathVariable Long bikerId) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByEmail(userDetails.getUsername());

        Biker biker = bikerService.findBikerById(bikerId);

        model.addAttribute("currentUser", user);
        model.addAttribute("biker", biker);
        model.addAttribute("tripId", biker.getTrip().getId());
        model.addAttribute("messages", messageService.getMessagesBySenderOrReceiver(biker, biker.getPassenger()));
        return "chat";
    }
}

