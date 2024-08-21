package org.app.gtsconnected3.repository;

import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Passenger;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.TripState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepo extends JpaRepository<Trip, Long>{
    List<Trip> findByStateNot(TripState state);
    Trip findByBikersContains(Biker biker); //trova il viaggio in cui è presente il biker
    List<Trip> findAllByOrderByIdDesc();

}
