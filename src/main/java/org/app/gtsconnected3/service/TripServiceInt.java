package org.app.gtsconnected3.service;

import org.app.gtsconnected3.dto.NewTripForm;
import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.User;

import java.util.List;

public interface TripServiceInt {
    Trip save(Trip trip, NewTripForm tripForm);
    Trip save (Trip trip);

    Trip update(Trip trip);

    void delete(Trip trip);

    Trip findById(Long id);

    List<Trip> findAll();

    void updateTripStates();

    Trip findByBiker(Biker biker);

    List<Trip> findAllByUser_passengers(User user);

    List<Trip> findAllByUser_bikers(User user);

    void isValid(Trip trip);

    List<String> findAllImages();

}
