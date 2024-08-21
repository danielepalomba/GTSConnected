package org.app.gtsconnected3.controller.admin;

import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Passenger;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.exception.NotAvailableTripException;
import org.app.gtsconnected3.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class DeleteTripController {

    private final TripService tripService;
    private final BikerService bikerService;
    private final PassengerService passengerService;
    private final UserService userService;
    private final MessageService messageService;

    @Autowired
    public DeleteTripController(TripService tripService, BikerService bikerService, PassengerService passengerService, UserService userService, MessageService messageService) {
        this.tripService = tripService;
        this.bikerService = bikerService;
        this.passengerService = passengerService;
        this.userService = userService;
        this.messageService = messageService;
    }

    @PostMapping("/users/cancel-trip-admin")
    public String deleteTrip(@RequestParam("id") Long tripId){
        Trip trip = tripService.findById(tripId);
        try{
            //tripService.isValid(trip);
            List<Biker> bikers = bikerService.findAllByTrip(trip);
            List<Passenger> passengers = passengerService.getAllInTrip(trip);
            userService.notyfyRemovedTripByAdmin(userService.extractUsers(bikers, passengers), trip);
            passengerService.removeAllInTrip(trip);
            bikerService.removeAllInTrip(trip);
            messageService.deleteAllByTrip(trip);
            tripService.delete(trip);
        }catch (NotAvailableTripException e){
            log.error(e.getMessage());
            return "redirect:/users/trip-alredy-started";
        }
        return "redirect:/home";
    }

    @GetMapping("/users/trip-alredy-started")
    public String tripAlredyStarted(Model model){
        model.addAttribute("error", "Il trip non può essere cancellato");
        return "error-info";
    }
}
