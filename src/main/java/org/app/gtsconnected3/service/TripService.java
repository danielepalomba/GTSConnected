package org.app.gtsconnected3.service;

import lombok.extern.slf4j.Slf4j;
import org.app.gtsconnected3.dto.NewTripForm;
import org.app.gtsconnected3.entity.*;
import org.app.gtsconnected3.exception.NotAvailableTripException;
import org.app.gtsconnected3.repository.MessageRepo;
import org.app.gtsconnected3.repository.TripRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class TripService implements TripServiceInt {

    private final TripRepo tripRepository;
    private final MessageRepo messageRepo;

    @Autowired
    public TripService(TripRepo tripRepository, MessageRepo messageRepo) {
        this.tripRepository = tripRepository;
        this.messageRepo = messageRepo;
    }
    @Override
    @Transactional
    public Trip save(Trip trip, NewTripForm tripForm) {
        trip.setState(TripState.PLANNED);
        trip.setDeparturePlace(tripForm.getDeparturePlace());
        trip.setDepartureTime(Timestamp.valueOf(tripForm.getDepartureTime()));
        trip.setArrivalPlace(tripForm.getArrivalPlace());
        trip.setArrivalTime(Timestamp.valueOf(tripForm.getArrivalTime()));
        trip.setNote(tripForm.getNote());
        if (trip.getArrivalTime().toLocalDateTime().isBefore(trip.getDepartureTime().toLocalDateTime())) {
            throw new RuntimeException("Controlla le date inserite!");
        }
        return tripRepository.save(trip);
    }

    @Override
    public Trip save(Trip trip) {
        return tripRepository.save(trip);
    }

    @Override
    @Transactional
    public Trip update(Trip trip) {
        return tripRepository.save(trip);
    }

    @Override
    @Transactional
    public void delete(Trip trip) {
        tripRepository.delete(trip);
    }

    @Override
    public Trip findById(Long id) {
        return tripRepository.findById(id).orElse(null);
    }

    @Override
    public List<Trip> findAll() {
        return tripRepository.findAllByOrderByIdDesc();
    }

    @Override
    @Scheduled(fixedRate = 10000)
    public void updateTripStates() {
        List<Trip> trips = tripRepository.findByStateNot(TripState.FINISHED);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm");

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Rome"));
        String currentTimeString = now.format(formatter);
        log.info("Current time: {}", currentTimeString);

        for (Trip trip : trips) {
            String departureTime = trip.getDepartureTimeFormatted();
            String arrivalTime = trip.getArrivalTimeFormatted();
            log.info("Departure time: {}", departureTime);
            log.info("Arrival time: {}", arrivalTime);
            //boolean check = currentTimeString.compareTo(departureTime) >= 0 && trip.getState().equals(TripState.PLANNED);

            if(currentTimeString.compareTo(departureTime) >= 0 && trip.getState().equals(TripState.PLANNED)) {
                trip.setState(TripState.STARTED);
                tripRepository.save(trip);
            }

            if(currentTimeString.compareTo(arrivalTime) >= 0 && trip.getState().equals(TripState.STARTED)) {
                trip.setState(TripState.FINISHED);
                messageRepo.deleteAllByTrip(trip);
                tripRepository.save(trip);
            }
        }
    }

    @Override
    public Trip findByBiker(Biker biker) {
        return tripRepository.findByBikersContains(biker);
    }

    @Override
    public List<Trip> findAllByUser_passengers(User user) {
        List<Passenger> passengers = user.getPassengers();
        List<Trip> trips = new ArrayList<>();

        for (Passenger p : passengers) {
            if (Objects.nonNull(p.getBiker())) {
                trips.add(p.getBiker().getTrip());
            }
        }
        return trips;
    }

    @Override
    public List<Trip> findAllByUser_bikers(User user) {
        List<Biker> bikers = user.getBikers();
        List<Trip> trips = new ArrayList<>();

        for (Biker b : bikers) {
            trips.add(b.getTrip());
        }
        return trips;
    }

    @Override
    public void isValid(Trip trip) {
        boolean check = trip.getState().getLabel().equals("PLANNED");
        if (!check)
            throw new NotAvailableTripException();
    }

    @Override
    public List<String> findAllImages() {
        return tripRepository.findAll().stream().map(Trip::getImageUrl).toList();
    }
}
