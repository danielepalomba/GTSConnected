package org.app.gtsconnected3.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.dto.NewTripForm;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.User;
import org.app.gtsconnected3.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@Slf4j
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class NewTripController {

    private final TripService tripService;
    private final UserService userService;
    private final S3FileService s3FileService;

    @Autowired
    public NewTripController(TripService tripService, UserService userService, S3FileService s3FileService) {
        this.tripService = tripService;
        this.userService = userService;
        this.s3FileService = s3FileService;
    }

    @GetMapping("/users/new-trip")
    public String showNewTripPage(Model model) {
        model.addAttribute("tripForm", new NewTripForm());
        List<String> images = s3FileService.listAllObjectsByName(System.getenv("AWS_BUCKET_NAME"));
        log.info("Images: {}", images);
        log.info("Bucket name: {}", System.getenv("AWS_BUCKET_NAME"));
        log.info("Region: {}", System.getenv("AWS_REGION"));
        model.addAttribute("images", images);
        return "admin/new-trip";
    }

    @PostMapping("/users/new-trip")
    public String submitNewTrip(@RequestParam("newImage") MultipartFile newImage, @RequestParam("image") String image, Model model, NewTripForm tripForm, HttpServletRequest request) {
        Trip trip = new Trip();
        List<String> images = s3FileService.listAllObjects(System.getenv("AWS_BUCKET_NAME"));
        try {
            if (!newImage.isEmpty()) {
                s3FileService.uploadFileToS3Bucket(trip, newImage);
            } else {
                images.forEach(url -> { //Se non viene caricata una nuova immagine, viene presa quella già presente
                    if (url.contains(image)) { //Se l'url contiene il nome dell'immagine selezionata, viene settata come immagine del viaggio
                        trip.setImageUrl(url);
                    }
                });
            }
            tripService.save(trip, tripForm);
            String siteURL = request.getRequestURL().toString();
            List<User> users = userService.findAllByNewsLetter();
            userService.notifyUsersForNewTrip(users, trip, siteURL);
            return "redirect:/home";
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("trip", trip);
            return "admin/new-trip";
        }
    }

    @GetMapping("/users/new-trip/book/{id}")
    public String showBookingPage (@PathVariable Long id){
        return "redirect:/users/book/" + id;
    }
}
