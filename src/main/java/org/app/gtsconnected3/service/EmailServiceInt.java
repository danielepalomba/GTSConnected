package org.app.gtsconnected3.service;

import org.app.gtsconnected3.dto.QuestionForm;
import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Passenger;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.User;

import java.util.List;

public interface EmailServiceInt {
    void sendVerificationEmail(User user, String siteURL);
    void sendPassengerRemovedTripAlertMsg(Passenger passenger, Trip trip, Biker biker);
    void sendToBikerPassengerRemovedFromTripAlertMsg(Passenger passenger, Trip trip);
    void sendEmailToAllBikersForEndedTrip(Trip trip);
    void notifyRemovedTripByAdmin(List<User> users, Trip trip);
    void notifyUsersForNewTrip(List<User> users, Trip trip, String siteURL);
    void sendPasswordResetEmail(User user, String siteURL);
    void sendBookingRejectedEmail(Passenger passenger, Trip trip, Biker biker);
    void sendBookingAcceptedEmail(Passenger passenger, Trip trip, Biker biker);
    void sendNoBiker(List<Passenger> passengers);
    void sendEmailToAdmin(QuestionForm questionForm);
}
