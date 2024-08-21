package org.app.gtsconnected3.service;

import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.User;
import org.app.gtsconnected3.exception.UserAlredyInTripException;
import org.app.gtsconnected3.repository.BikerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class BikerService implements BikerServiceInt {

    private final BikerRepo bikerRepository;

    @Autowired
    public BikerService(BikerRepo bikerRepository) {
        this.bikerRepository = bikerRepository;
    }

    @Override
    @Transactional
    public Biker saveBiker(Biker biker) {
        return bikerRepository.save(biker);
    }

    @Override
    @Transactional
    public void removeBiker(Biker biker) {
        bikerRepository.delete(biker);
    }

    @Override
    @Transactional
    public Biker updateBiker(Biker biker) {
        return bikerRepository.save(biker);
    }

    @Override
    public Biker findBikerById(Long id) {
        return bikerRepository.findById(id).orElse(null);
    }

    @Override
    public Biker findBikerByTripId(Trip trip) {
        return bikerRepository.findByTrip(trip);
    }


    @Override
    public List<Biker> findAllByTrip(Trip trip) {
        return bikerRepository.findAllByTrip(trip);
    }

    @Override
    public void checkIfBikerExists(User user, Trip trip) {
        boolean check = bikerRepository.findAllByUser(user).stream().anyMatch(biker -> biker.getTrip().equals(trip));
        if (check) {
            throw new UserAlredyInTripException();
        }
    }

    @Override
    public List<Biker> findAllByUser(User user) {
        return bikerRepository.findAllByUser(user);
    }

    @Override
    public void isPassengerValid(Biker biker) {
        if (biker.getPassenger() != null)
            throw new RuntimeException("Biker already has a passenger");
    }

    @Override
    @Transactional
    public void removeAllInTrip(Trip trip) {
        bikerRepository.removeAllByTrip(trip);
    }

    @Override
    public List<Biker> findAllByPlannedTrip(User user) {
        List<Biker> bikers = bikerRepository.findAllByUser(user);
        return bikers.stream().filter(biker -> biker.getTrip().getState().getLabel().equals("PLANNED")).toList();
    }

    @Override
    public List<Biker> selectAllWithPassengerNotVerified(List<Biker> bikers) {
        return bikers.stream().filter(biker -> (biker.getPassenger() != null) && !biker.getPassenger().isAccept()).toList();
    }

    @Override
    public List<Biker> selectAllWithPassenger(List<Biker> bikers) {
        return bikers.stream().filter(biker -> biker.getPassenger() != null).toList();
    }
}
