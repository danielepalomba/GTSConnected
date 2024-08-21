package org.app.gtsconnected3.controller.client;

import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.User;
import org.app.gtsconnected3.security.CustomUserDetails;
import org.app.gtsconnected3.service.BikerService;
import org.app.gtsconnected3.service.MessageService;
import org.app.gtsconnected3.service.TripService;
import org.app.gtsconnected3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
public class TripDetailsController {

    private final TripService tripService;
    private final BikerService bikerService;
    private final UserService userService;
    private final MessageService messageService;

    @Autowired
    public TripDetailsController(TripService tripService, BikerService bikerService, UserService userService, MessageService messageService) {
        this.tripService = tripService;
        this.bikerService = bikerService;
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("/users/trip-details")
    public String showTripDetails(Model model, @RequestParam("id") Long tripId) {
        model.addAttribute("trip", tripService.findById(tripId));
        model.addAttribute("bikers", bikerService.findAllByTrip(tripService.findById(tripId)));
        log.info("Trip details page visited");
        return "trip-details";
    }

    @PostMapping("/users/cancel-trip")
    public String cancelTrip(@RequestParam("id") Long tripId) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByEmail(userDetails.getUsername());
        Trip trip = tripService.findById(tripId);

        if (trip.getState().getLabel().equals("PLANNED")){
            List<Biker> bikers = trip.getBikers(); //tutti i biker che partecipano ad un trip

            //devo cercare il biker che ha come passeggero l'utente loggato
            //nel caso in cui lo trovo cancello il trip come passeggero
            //altrimenti cancello il trip come biker
            for (Biker biker : bikers) {
                if(biker.getPassenger() != null) {
                    if (biker.getPassenger().getUser().equals(user)) {
                        messageService.deleteAllBySenderOrReceiver(biker, biker.getPassenger());
                        userService.cancelTripAsPassenger(user, trip);
                        tripService.findAllByUser_bikers(user).remove(trip);
                        tripService.findAllByUser_passengers(user).remove(trip);
                        return "redirect:/users/all-trips";
                    }
                }
            }

            for (Biker biker : bikers) {
                if (biker.getTrip().equals(trip)) {
                    messageService.deleteAllBySenderOrReceiver(biker, biker.getPassenger());
                    userService.cancelTripAsBiker(user, trip);
                    tripService.findAllByUser_bikers(user).remove(trip);
                    tripService.findAllByUser_passengers(user).remove(trip);
                    return "redirect:/users/all-trips";
                }
            }
            return "redirect:/users/error-trip-not-found";
        }

        return "redirect:/users/error-trip-already-started";
    }

    @GetMapping("/users/error-trip-not-found")
    public String showErrorPage(Model model) {
        model.addAttribute("error","Oops! Il viaggio non è stato trovato. Riprova.");
        return "error-info";
    }

    @GetMapping("/users/error-trip-already-started")
    public String showErrorPageTripAlreadyStarted(Model model) {
        model.addAttribute("error","Oops! Il viaggio è già iniziato. Non puoi cancellarlo.");
        return "error-info";
    }
}
