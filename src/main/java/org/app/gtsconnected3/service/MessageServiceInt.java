package org.app.gtsconnected3.service;

import org.app.gtsconnected3.entity.*;

import java.util.List;

public interface MessageServiceInt {
    Message save(Message message);
    List<Message> getMessagesBySenderAndReceiver(User sender, User receiver);
    List<Message> getMessagesBySenderOrReceiver(Biker biker, Passenger passenger);
    void deleteAllBySenderAndReceiver(User sender, User receiver);
    void deleteAllByTrip(Trip trip);
    void deleteAllBySenderOrReceiver(Biker biker, Passenger passenger);
}
