package org.app.gtsconnected3.controller.client;

import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Passenger;
import org.app.gtsconnected3.entity.User;
import org.app.gtsconnected3.exception.PassengerEqualsToBikerException;
import org.app.gtsconnected3.exception.UserAlredyInTripException;
import org.app.gtsconnected3.security.CustomUserDetails;
import org.app.gtsconnected3.service.BikerService;
import org.app.gtsconnected3.service.PassengerService;
import org.app.gtsconnected3.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class PassengerController {

    private final PassengerService passengerService;
    private final BikerService bikerService;
    private final UserService userService;

    @Autowired
    public PassengerController(PassengerService passengerService, BikerService bikerService, UserService userService) {
        this.passengerService = passengerService;
        this.bikerService = bikerService;
        this.userService = userService;
    }

    @GetMapping("/users/book-passenger/{bikerId}")
    public String showPassengerPage(Model model, @PathVariable Long bikerId) {
        model.addAttribute("biker", bikerService.findBikerById(bikerId));
        model.addAttribute("passenger", new Passenger());
        return "book-passenger";
    }

    @GetMapping("/users/book/passenger-confirmed-book")
    public String showPassengerConfirmedPage() {
        return "passenger-confirmed-book";
    }

    @PostMapping("/users/book-passenger/{bikerId}")
    public String confirmPassenger(@PathVariable Long bikerId, @ModelAttribute Passenger passenger){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUserByEmail(userDetails.getUsername());

        Biker biker = bikerService.findBikerById(bikerId);
        try{
            passengerService.validate(biker, user); //check if passenger is not the same as biker
            userService.checkIfUserIsAlredyInTrip(user, biker.getTrip()); //check if user is already in the trip
            bikerService.isPassengerValid(biker); //check if biker's seat is valid
            passenger.setBiker(biker);
            passenger.setUser(user);
            passenger.setAccept(false);
            passengerService.savePassenger(passenger);
        } catch (PassengerEqualsToBikerException e) {
            log.error(e.getMessage());
            return "redirect:/users/error-passenger-equals-to-biker?error=" + e.getMessage();
        } catch (UserAlredyInTripException e){
            log.error(e.getMessage());
            return "redirect:/users/error-biker-already-exists?error=" + e.getMessage();
        }
        catch (RuntimeException e) {
            log.error(e.getMessage());
            return "redirect:/users/error-generic";
        }
        return "redirect:/users/book/passenger-confirmed-book";
    }

    @GetMapping("/users/error-passenger-equals-to-biker")
    public String showErrorBikerPassenger(Model model, @RequestParam String error) {
        model.addAttribute("error", error);
        return "error-info";
    }

    @GetMapping("/users/error-generic")
    public String showGenericError(Model model) {
        model.addAttribute("error", "Oops! Si è verificato un errore, riprova.");
        return "error-info";
    }
}
