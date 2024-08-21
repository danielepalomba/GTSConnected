package org.app.gtsconnected3.controller.client;

import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.entity.*;
import org.app.gtsconnected3.security.CustomUserDetails;
import org.app.gtsconnected3.service.BikerService;
import org.app.gtsconnected3.service.PassengerService;
import org.app.gtsconnected3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Slf4j
@Controller
public class BikerBookManage {
    private final PassengerService passengerService;
    private final BikerService bikerService;
    private final UserService userService;

    @Autowired
    public BikerBookManage(PassengerService passengerService, BikerService bikerService, UserService userService) {
        this.passengerService = passengerService;
        this.bikerService = bikerService;
        this.userService = userService;
    }

    @GetMapping("/users/user-details/manage-book")
    public String manageBook(Model model) {

        //cerco l'utente loggato
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByEmail(userDetails.getUsername());
        log.info("USER: " + user.getEmail());
        //carico i suoi biker che sono iscritti ad un viaggio planned
        List<Biker> bikers = bikerService.findAllByPlannedTrip(user);
        //seleziono i biker che hanno un passeggero
        bikers = bikerService.selectAllWithPassenger(bikers);

        model.addAttribute("bikers", bikers);
        return "manage-book";
    }

    @RequestMapping(value = "/acceptBooking/{id}", method = RequestMethod.GET)
    public String acceptBooking(@PathVariable("id") Long id) {
        Passenger passenger = passengerService.findPassengerById(id);
        Biker biker = passenger.getBiker();
        Trip trip = biker.getTrip();
        passenger.setAccept(true);
        passengerService.notyfyPassengerBookTrue(passenger, trip, biker);
        passengerService.savePassenger(passenger);
        return "redirect:/users/user-details/manage-book";
    }

    @RequestMapping(value = "/rejectBooking/{id}", method = RequestMethod.GET)
    public String rejectBooking(@PathVariable("id") Long id) {
        Passenger passenger = passengerService.findPassengerById(id);
        Biker biker = passenger.getBiker();
        Trip trip = biker.getTrip();
        passenger.getBiker().setPassenger(null);
        passengerService.notyfyPassengerBookFalse(passenger, trip, biker);
        passengerService.delete(passenger);
        return "redirect:/users/user-details/manage-book";
    }
}
