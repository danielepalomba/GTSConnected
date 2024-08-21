package org.app.gtsconnected3.service;

import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.entity.*;
import org.app.gtsconnected3.repository.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageService implements MessageServiceInt{

    private final MessageRepo messageRepo;

    @Autowired
    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    @Override
    @Transactional
    public Message save(Message message) {
        log.info("MESSAGGIO SALVATO: " + message);
        return messageRepo.save(message);
    }

    @Override
    public List<Message> getMessagesBySenderAndReceiver(User sender, User receiver) {
        return messageRepo.findBySenderAndReceiver(sender, receiver);
    }

    @Override
    public List<Message> getMessagesBySenderOrReceiver(Biker biker, Passenger passenger) {
        List<Message> messages = messageRepo.findAllBy();
        Trip trip = biker.getTrip();

        if (biker.getTrip().equals(passenger.getBiker().getTrip())) {
            return messages.stream()
                    .filter(message -> message.getTrip().equals(trip) &&
                            (message.getSender().equals(biker.getUser()) || message.getReceiver().equals(biker.getUser())) &&
                            (message.getSender().equals(passenger.getUser()) || message.getReceiver().equals(passenger.getUser())))
                    .sorted(Comparator.comparing(Message::getTimestamp))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }


    @Override
    @Transactional
    public void deleteAllBySenderAndReceiver(User sender, User receiver) {
        messageRepo.deleteAllBySenderAndReceiver(sender, receiver);
    }

    @Override
    @Transactional
    public void deleteAllByTrip(Trip trip) {
        messageRepo.deleteAllByTrip(trip);
    }

    @Override
    @Transactional
    public void deleteAllBySenderOrReceiver(Biker sender, Passenger receiver) {
        List<Message> messages = messageRepo.findAllBy();
        if(sender == null || receiver == null){
            return;
        }
        if(sender.getTrip().equals(receiver.getBiker().getTrip())){
            for (Message message : messages) {
                if(message.getTrip().equals(sender.getTrip()) && (message.getSender().equals(sender.getUser()) || message.getReceiver().equals(sender.getUser())) && (message.getSender().equals(receiver.getUser()) || message.getReceiver().equals(receiver.getUser()))){
                    messageRepo.delete(message);
                }
            }
        }
    }
}
