package org.app.gtsconnected3.service;

import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.TripState;
import org.app.gtsconnected3.entity.User;

import java.util.List;

public interface BikerServiceInt {
    @SuppressWarnings("UnusedReturnValue")
    Biker saveBiker(Biker biker);
    void removeBiker(Biker biker);
    Biker updateBiker(Biker biker);
    Biker findBikerById(Long id);
    Biker findBikerByTripId(Trip trip);
    List<Biker> findAllByTrip(Trip trip);
    void checkIfBikerExists(User user, Trip trip);
    List<Biker> findAllByUser(User user);
    void isPassengerValid(Biker biker);
    void removeAllInTrip(Trip trip);
    List<Biker> findAllByPlannedTrip(User user);
    List<Biker> selectAllWithPassengerNotVerified(List<Biker> bikers);
    List<Biker> selectAllWithPassenger(List<Biker> bikers);

}
