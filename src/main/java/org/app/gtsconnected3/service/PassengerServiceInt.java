package org.app.gtsconnected3.service;

import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Passenger;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.User;

import java.util.List;

public interface PassengerServiceInt {

    @SuppressWarnings("UnusedReturnValue")
    Passenger savePassenger(Passenger passenger);
    Passenger findPassengerById(Long id);
    void delete(Passenger passenger);
    Passenger update(Passenger passenger);
    void validate(Biker biker, User user);
    List<Passenger> getAllInTrip(Trip trip);
    void removeAllInTrip(Trip trip);
    List<Passenger> findAllByUserAndAcceptedIsFalse(User user);
    List<Passenger> findAllByUser(User user);
    void notyfyPassengerBookTrue(Passenger passenger, Trip trip, Biker biker);
    void notyfyPassengerBookFalse(Passenger passenger, Trip trip, Biker biker);
}
