package org.app.gtsconnected3.service;

import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.dto.RegistrationForm;
import org.app.gtsconnected3.entity.*;
import org.app.gtsconnected3.exception.EmailAlredyExistsException;
import org.app.gtsconnected3.exception.UserAlredyInTripException;
import org.app.gtsconnected3.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserService implements UserServiceInt {

    private final UserRepo userRepository;
    private final RoleRepo roleRepository;
    private final TripRepo tripRepository;
    private final BikerRepo bikerRepository;
    private final PassengerRepo passengerRepo;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final EmailServiceInt emailService;

    @Autowired
    public UserService(UserRepo userRepository, RoleRepo roleRepository, TripRepo tripRepository, BikerRepo bikerRepository, PassengerRepo passengerRepo, EmailServiceInt emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tripRepository = tripRepository;
        this.bikerRepository = bikerRepository;
        this.passengerRepo = passengerRepo;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeUser(User user) {
        if(user.getPassengers() == null && user.getBikers() == null){
            userRepository.delete(user);
        }else if(user.getPassengers() == null) {
            bikerRepository.deleteAll(user.getBikers());
            userRepository.delete(user);
        }else if(user.getBikers() == null){
            passengerRepo.deleteAll(user.getPassengers());
            userRepository.delete(user);
        }else{
            emailService.sendNoBiker(user.getPassengers()); //send email to passengers
            passengerRepo.deleteAll(user.getPassengers());
            bikerRepository.deleteAll(user.getBikers());
            userRepository.delete(user);
        }
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public List<Trip> getAllTripByUser(User user) {
        List<Biker> bikers = user.getBikers();
        List<Trip> trips = new ArrayList<>();
        for (Biker b : bikers) {
            trips.add(tripRepository.findByBikersContains(b));
        }
        return trips;
    }

    @Override
    @Transactional
    public void cancelTripAsBiker(User user, Trip trip) {
        List<Biker> bikers = bikerRepository.findAllByUser(user);

        for (Biker b : bikers) {
            if (trip.getId().equals(b.getTrip().getId())) {
                log.info("Rimuovo il biker: " + b.getId());
                Passenger passenger = passengerRepo.findByBiker(b);
                if (passenger != null) {
                    emailService.sendPassengerRemovedTripAlertMsg(passenger, trip, b);
                    passengerRepo.delete(passenger);
                    log.info("Passenger rimosso");
                }
                user.getBikers().remove(b);
                trip.getBikers().remove(b);
                bikerRepository.delete(b);
                return;
            }
        }
    }

    @Override
    @Transactional
    public void cancelTripAsPassenger(User user, Trip trip) {
        List<Passenger> userPassengers = user.getPassengers();

        for (Passenger p : userPassengers) {
            if (trip.getId().equals(p.getBiker().getTrip().getId())) {
                log.info("Rimuovo il passenger: " + p.getId());
                user.getPassengers().remove(p);
                p.getBiker().setPassenger(null);
                emailService.sendToBikerPassengerRemovedFromTripAlertMsg(p, trip);
                passengerRepo.delete(p);
                return;
            }
        }
    }

    public void registerUser(User user, String siteURL, RegistrationForm registrationForm) {
        if (userRepository.findByEmail(registrationForm.getEmail()) != null) {
            throw new EmailAlredyExistsException();
        } else {
            user.setEmail(registrationForm.getEmail());
            user.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
            user.setFirstName(registrationForm.getName());
            user.setLastName(registrationForm.getSurname());
            Role role = roleRepository.findByName("ROLE_USER");
            Role role1 = roleRepository.findByName("ROLE_ADMIN");
            //NOT THE BEST CHOICE BUT IT WORKS EHEHEHEH
            if(registrationForm.getEmail().equals("palombad46@gmail.com")){
                user.setRoles(List.of(role, role1));
            }else{
                user.setRoles(List.of(role));
            }
            user.setNewsletter(registrationForm.isNewsletter());
            user.setVerificationToken(UUID.randomUUID().toString());
            user.setDateBirth(Timestamp.valueOf(registrationForm.getBirth().atStartOfDay()));
            userRepository.save(user);

            emailService.sendVerificationEmail(user, siteURL);
        }
    }

    public void verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token);
        if (user != null) {
            user.setVerificationToken(null);
            userRepository.save(user);
        }
    }

    @Override
    public List<User> extractUsers(List<Biker> bikers, List<Passenger> passengers) {
        List<User> users = new ArrayList<>();
        for(Biker b : bikers){
            users.add(b.getUser());
        }
        for(Passenger p : passengers){
            users.add(p.getUser());
        }
        return users;
    }

    @Override
    public List<User> findAllByNewsLetter() {
        return userRepository.findAllByNewsletterIsTrue();
    }

    @Override
    public User startPasswordRecovery(String email, String siteURL) {
        User user = userRepository.findByEmail(email);
        if(user != null){
            user.setResetPasswordToken(UUID.randomUUID().toString());
            userRepository.save(user);
            emailService.sendPasswordResetEmail(user, siteURL);
        }
        return user;
    }

    @Override
    public void notyfyRemovedTripByAdmin(List<User> users, Trip trip) {
        emailService.notifyRemovedTripByAdmin(users, trip);
    }

    @Override
    public void notifyUsersForNewTrip(List<User> users, Trip trip, String siteURL) {
        emailService.notifyUsersForNewTrip(users, trip, siteURL);
    }

    @Override
    public boolean checkIfUserIsAlredyInTrip(User user, Trip trip) {
        List<Biker> bikers = user.getBikers();
        for(Biker b : bikers){
            if(b.getTrip().getId().equals(trip.getId())){
                throw new UserAlredyInTripException();
            }
        }

        List<Passenger> passengersInTrip = passengerRepo.findByBiker_Trip(trip);
        for(Passenger p : passengersInTrip){
            if(p.getUser().getId().equals(user.getId())){
                throw new UserAlredyInTripException();
            }
        }

        return true;
    }
}