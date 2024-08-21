package org.app.gtsconnected3.controller.client;

import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.User;
import org.app.gtsconnected3.security.CustomUserDetails;
import org.app.gtsconnected3.service.TripService;
import org.app.gtsconnected3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class UserTripsController {

    private final UserService userService;
    private final TripService tripService;

    @Autowired
    public UserTripsController(UserService userService, TripService tripService) {
        this.userService = userService;
        this.tripService = tripService;
    }

    @GetMapping("/users/all-trips")
    public String showAllTrips(Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByEmail(userDetails.getUsername());

        List<Trip> bikerTrips = tripService.findAllByUser_bikers(user);
        List<Trip> passengerTrips = tripService.findAllByUser_passengers(user);

        model.addAttribute("bikerTrips", bikerTrips);
        model.addAttribute("passengerTrips", passengerTrips);

        return "all-trips";
    }
}
