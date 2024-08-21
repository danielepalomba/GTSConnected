package org.app.gtsconnected3.service;

import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Passenger;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.User;
import org.app.gtsconnected3.exception.PassengerEqualsToBikerException;
import org.app.gtsconnected3.repository.PassengerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PassengerService implements PassengerServiceInt{
    private final PassengerRepo passengerRepository;
    private final EmailServiceInt emailService;
    @Autowired
    public PassengerService(PassengerRepo passengerRepository, EmailServiceInt emailService) {
        this.passengerRepository = passengerRepository;
        this.emailService = emailService;
    }
    @Override
    @Transactional
    public Passenger savePassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    @Override
    public Passenger findPassengerById(Long id) {
        return passengerRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void delete(Passenger passenger) {
        passengerRepository.delete(passenger);
    }

    @Override
    @Transactional
    public Passenger update(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    @Override
    public void validate(Biker biker, User user) {
        if(!(biker.getUser().getEmail().equals(user.getEmail()))) {
            return;
        }
        throw new PassengerEqualsToBikerException();
    }

    @Override
    public List<Passenger> getAllInTrip(Trip trip) {
        return passengerRepository.findByBiker_Trip(trip);
    }

    @Override
    @Transactional
    public void removeAllInTrip(Trip trip) {
        List<Passenger> passengers = passengerRepository.findByBiker_Trip(trip);
        passengerRepository.deleteAll(passengers);
    }

    @Override
    public List<Passenger> findAllByUserAndAcceptedIsFalse(User user) {
        List<Passenger> list = passengerRepository.findAllByUser(user);
        List<Passenger> result = new ArrayList<>();
        for(Passenger p : list) {
            if(!p.isAccept()) {
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public List<Passenger> findAllByUser(User user) {
        return passengerRepository.findAllByUser(user);
    }

    @Override
    public void notyfyPassengerBookTrue(Passenger passenger, Trip trip, Biker biker) {
        emailService.sendBookingAcceptedEmail(passenger, trip, biker);
    }

    @Override
    public void notyfyPassengerBookFalse(Passenger passenger, Trip trip, Biker biker) {
        emailService.sendBookingRejectedEmail(passenger, trip, biker);
    }
}
