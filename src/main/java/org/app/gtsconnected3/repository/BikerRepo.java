package org.app.gtsconnected3.repository;

import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.TripState;
import org.app.gtsconnected3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BikerRepo extends JpaRepository<Biker, Long> {

    Biker findByTrip(Trip trip);

    List<Biker> findAllByUserAndTrip_State(User user, TripState tripState);
    List<Biker> findAllByUser(User user);

    List<Biker> findAllByTrip(Trip trip);

    void removeAllByTrip(Trip trip);
}
