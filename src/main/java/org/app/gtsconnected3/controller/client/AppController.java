package org.app.gtsconnected3.controller.client;

import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AppController {

    private final TripService tripService;

    @Autowired
    public AppController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("")
    public String defaultHomePage() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Trip> trips = tripService.findAll();
        model.addAttribute("trips", trips);
        return "home";
    }

    @GetMapping("/policy")
    public String policy() {
        return "policy";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

}
