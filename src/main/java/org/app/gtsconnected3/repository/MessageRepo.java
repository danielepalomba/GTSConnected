package org.app.gtsconnected3.repository;

import org.app.gtsconnected3.entity.Message;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findBySenderAndReceiver(User sender, User receiver);
    List<Message> findBySenderOrReceiver(User sender, User receiver);
    List<Message> findAllBy();
    void deleteAllByTrip(Trip trip);
    void deleteAllBySenderAndReceiver(User sender, User receiver);
    void deleteAllBySenderOrReceiver(User sender, User receiver);
}
