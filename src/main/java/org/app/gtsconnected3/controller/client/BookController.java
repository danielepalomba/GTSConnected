package org.app.gtsconnected3.controller.client;

import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.User;
import org.app.gtsconnected3.exception.UserAlredyInTripException;
import org.app.gtsconnected3.exception.NotAvailableTripException;
import org.app.gtsconnected3.security.CustomUserDetails;
import org.app.gtsconnected3.service.BikerService;
import org.app.gtsconnected3.service.TripService;
import org.app.gtsconnected3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Slf4j
public class BookController {

    private final BikerService bikerService;

    private final TripService tripService;

    private final UserService userService;

    @Autowired
    public BookController(BikerService bikerService, TripService tripService, UserService userService) {
        this.bikerService = bikerService;
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping("/book/{id}")
    public String showBookingPage(@PathVariable Long id, Model model) {
        Trip trip = tripService.findById(id);
        List<Biker> bikers = bikerService.findAllByTrip(trip);
        model.addAttribute("trip", trip);
        model.addAttribute("bikers", bikers);
        model.addAttribute("biker", new Biker());
        return "book";
    }

    @PostMapping("/users/book/{tripId}")
    public String submitBooking(@PathVariable Long tripId, @ModelAttribute Biker biker) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByEmail(userDetails.getUsername());
        Trip trip = tripService.findById(tripId);
        try{
            tripService.isValid(trip); // check if the trip is still available
            userService.checkIfUserIsAlredyInTrip(user, trip); // check if the user is already in the trip
            biker.setUser(user);
        } catch (UserAlredyInTripException e) {
            log.error(e.getMessage());
            return "redirect:/users/error-biker-already-exists?error=" + e.getMessage();
        } catch (NotAvailableTripException e) {
            log.error("An unexpected error occurred", e);
            return "redirect:/users/error-trip-not-available?error=" + e.getMessage();
        }

        biker.setTrip(trip);
        bikerService.saveBiker(biker);
        return "redirect:/home";
    }

    @GetMapping("/users/error-biker-already-exists")
    public String showErrorPage(Model model, @RequestParam String error ) {
        model.addAttribute("error", error);
        return "error-info";
    }

    @GetMapping("/users/error-trip-not-available")
    public String showErrorPageTripNotAvailable(Model model, @RequestParam String error) {
        model.addAttribute("error", error);
        return "error-info";
    }
}

