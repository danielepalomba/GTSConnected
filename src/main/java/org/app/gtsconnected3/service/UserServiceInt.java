package org.app.gtsconnected3.service;

import org.app.gtsconnected3.dto.RegistrationForm;
import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Passenger;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.User;

import java.util.List;

public interface UserServiceInt {
    @SuppressWarnings("UnusedReturnValue")
    User saveUser(User user);

    void removeUser(User user);

    User updateUser(User user);

    User findUserById(Long id);

    User findUserByEmail(String email);

    boolean emailExists(String email);

    List<Trip> getAllTripByUser(User user);

    void cancelTripAsBiker(User user, Trip trip);

    void cancelTripAsPassenger(User user, Trip trip);

    void registerUser(User user, String siteURL, RegistrationForm registrationForm);

    void verifyUser(String token);

    List<User> extractUsers(List<Biker> bikers, List<Passenger> passengers);
    List<User> findAllByNewsLetter();
    User startPasswordRecovery(String email, String siteURL);
    void notyfyRemovedTripByAdmin(List<User> users, Trip trip);
    void notifyUsersForNewTrip(List<User> users, Trip trip, String siteURL);
    boolean checkIfUserIsAlredyInTrip(User user, Trip trip);
}
