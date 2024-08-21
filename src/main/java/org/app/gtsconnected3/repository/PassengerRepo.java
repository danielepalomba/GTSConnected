package org.app.gtsconnected3.repository;

import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Passenger;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PassengerRepo extends JpaRepository<Passenger, Long>{
    Passenger findByBiker(Biker biker);
    List<Passenger> findAllByUser(User user);
    List<Passenger> findByBiker_Trip(Trip trip); //trova i passeggeri di un viaggio
}
