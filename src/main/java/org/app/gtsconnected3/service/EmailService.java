package org.app.gtsconnected3.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.app.gtsconnected3.dto.QuestionForm;
import org.app.gtsconnected3.entity.Biker;
import org.app.gtsconnected3.entity.Passenger;
import org.app.gtsconnected3.entity.Trip;
import org.app.gtsconnected3.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/*ERROR: Description:  The bean 'emailService' could not be injected because it is a JDK dynamic proxy
    The bean is of type 'jdk.proxy2.$Proxy129' and implements: org.app.gtsconnected3.service.EmailServiceInt org.springframework.aop.SpringProxy org.springframework.aop.framework.Advised org.springframework.core.DecoratingProxy
    Expected a bean of type 'org.app.gtsconnected3.service.EmailService' which implements: org.app.gtsconnected3.service.EmailServiceInt  Process finished with exit code 1
*/

/*RESOLVED BY INJECT THE INTERFACES NOT THE CLASS ITSELF, @Async maybe create this error*/
@Service
public class EmailService implements EmailServiceInt {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    @Override
    public void sendVerificationEmail(User user, String siteURL) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Complete Registration!");
            helper.setFrom("gtsnowconnected@gmail.com");

            String htmlMsg =
                    "<div style='display: block; justify-content: center; text-align: center; background-color: white; border: 2px solid red; padding: 50px; border-radius: 5px;'>"
                    + "<h2 style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>Verifica la tua email</h2>"
                    + "<p style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>Per confermare il tuo account, clicca il tasto verifica</p>"
                    + "<button style='background-color: red; border: none; border-radius: 5px; padding: 10px;'><a href='" + siteURL + "/verify-account?token=" + user.getVerificationToken() + "' style='text-decoration: none; color: white; font-weight: bold;'>Verifica</a></button>"
                    + "</div>";

            helper.setText(htmlMsg, true);

            javaMailSender.send(mail);
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }

    @Async
    @Override
    public void sendPassengerRemovedTripAlertMsg(Passenger passenger, Trip trip, Biker biker) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setTo(passenger.getUser().getEmail());
            helper.setSubject("Trip Cancelled!");
            helper.setFrom("gtsnowconnected@gmail.com");

            String htmlMsg = "<div style='display: block; justify-content: center; text-align: center; background-color: white; border: 2px solid red; padding: 50px; border-radius: 5px;'>"
                    + "<h2 style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>Il tuo viaggio con " + biker.getUser().getFirstName() + " è stato cancellato</h2>"
                    + "<p style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>Il tuo viaggio come passeggero con " + biker.getUser().getFirstName() + " " + biker.getUser().getLastName() + " è stato cancellato. Il viaggio era programmato per il " + trip.getDepartureTimeFormatted() + " da " + trip.getDeparturePlace() + " a " + trip.getArrivalPlace() + ". Ci scusiamo per l'inconveniente.</p>"
                    + "</div>";

            helper.setText(htmlMsg, true);

            javaMailSender.send(mail);
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }

    @Async
    @Override
    public void sendToBikerPassengerRemovedFromTripAlertMsg(Passenger passenger, Trip trip) {
        Biker biker = passenger.getBiker();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(biker.getUser().getEmail());
        mailMessage.setSubject("Your seat is now free!");
        mailMessage.setFrom("gtsnowconnected@gmail.com");
        mailMessage.setText("Il passeggero " + passenger.getUser().getFirstName() + " " + passenger.getUser().getLastName() + " ha disdetto la prenotazione per il viaggio programmato per il " + trip.getDepartureTimeFormatted() + " da " + trip.getDeparturePlace() + " a " + trip.getArrivalPlace() + ".");

        javaMailSender.send(mailMessage);
    }

    @Async
    @Override
    public void sendEmailToAllBikersForEndedTrip(Trip trip) {
        List<Passenger> passengers = new ArrayList<>();
        trip.getBikers().forEach(biker -> passengers.add(biker.getPassenger()));
        List<Biker> bikers = trip.getBikers();
        List<User> users = new ArrayList<>();
        bikers.forEach(biker -> users.add(biker.getUser()));
        passengers.forEach(passenger -> users.add(passenger.getUser()));

        for (User user : users) {
            try {
                MimeMessage mail = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mail, true);

                helper.setTo(user.getEmail());
                helper.setSubject("Trip Ended!");
                helper.setFrom("gtsnowconnected@gmail.com");

                String htmlMsg = "<div style='display: block; justify-content: center; text-align: center; background-color: white; border: 2px solid red; padding: 50px; border-radius: 5px;'>"
                        + "<h2 style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>Grazie per aver partecipato!</h2>"
                        + "<p style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>Il viaggio programmato per il " + trip.getDepartureTimeFormatted() + " da " + trip.getDeparturePlace() + " a " + trip.getArrivalPlace() + " è terminato. Grazie per aver utilizzato GTS Connected!</p>"
                        + "</div>";

                helper.setText(htmlMsg, true);

                javaMailSender.send(mail);
            } catch (MessagingException e) {
                throw new MailParseException(e);
            }
        }
    }

    @Async
    @Override
    public void notifyRemovedTripByAdmin(List<User> users, Trip trip) {
        for (User user : users) {
            try {
                MimeMessage mail = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mail, true);

                helper.setTo(user.getEmail());
                helper.setSubject("Trip Cancelled!");
                helper.setFrom("gtsnowconnected@gmail.com");

                String htmlMsg = "<div style='display: block; justify-content: center; text-align: center; background-color: white; border: 2px solid red; padding: 50px; border-radius: 5px;'>"
                        + "<h2 style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>Viaggio annullato!</h2>"
                        + "<p style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>Ciao " + user.getFirstName() + ", il viaggio programmato in data " + trip.getDepartureTimeFormatted() + " con partenza da " + trip.getDeparturePlace() + " e arrivo a " + trip.getArrivalPlace() + ", è stato cancellato, ci scusiamo per il disagio!</p>"
                        + "</div>";

                helper.setText(htmlMsg, true);

                javaMailSender.send(mail);
            } catch (MessagingException e) {
                throw new MailParseException(e);
            }
        }
    }

    @Async
    @Override
    public void notifyUsersForNewTrip(List<User> users, Trip trip, String siteURL) {
        for (User user : users) {
            try {
                MimeMessage mail = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mail, true);

                helper.setTo(user.getEmail());
                helper.setSubject("New Trip Available!");
                helper.setFrom("gtsnowconnected@gmail.com");

                String htmlMsg = "<div style='display: block; justify-content: center; text-align: center; background-color: white; border: 2px solid red; padding: 50px; border-radius: 5px;'>"
                        + "<h2 style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>Un nuovo viaggio è disponibile ora!</h2>"
                        + "<p style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>Ciao " + user.getFirstName() + ", un nuovo viaggio è disponibile per te! <br> Il viaggio è programmato per il " + trip.getDepartureTimeFormatted() + " con partenza da " + trip.getDeparturePlace() + " e arrivo a " + trip.getArrivalPlace() + ".</p>"
                        + "</div>"
                        + "<div style='border:2px solid red; padding: 10px; margin-top: 5px; border-radius: 5px;'>"
                        + "<h2 style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>Informazioni sul viaggio:</h2>"
                        + "<ul style='list-style-type: none; padding: 0; font-family: Segoe UI, Tahoma, Geneva, Verdana, sans-serif'>"
                        + "<li style='margin-bottom: 10px;'><strong>Luogo di partenza:</strong> " + trip.getDeparturePlace() + "</li>"
                        + "<li style='margin-bottom: 10px;'><strong>Data e ora di partenza:</strong> " + trip.getDepartureTimeFormatted() + "</li>"
                        + "<li style='margin-bottom: 10px;'><strong>Luogo di arrivo:</strong> " + trip.getArrivalPlace() + "</li>"
                        + "<li style='margin-bottom: 10px;'><strong>Data e ora di arrivo:</strong> " + trip.getArrivalTimeFormatted() + "</li>"
                        + "<li style='margin-bottom: 10px;'><strong>Note sul viaggio:</strong> " + trip.getNote() + "</li>"
                        + "</ul>"
                        + "</div>"
                        + "<div style='border:2px solid red; padding: 10px; margin-top: 5px; border-radius: 5px; text-align: center;'>"
                        + "<button style='background-color: red; border: none; border-radius: 5px; padding: 10px;'><a href='" + siteURL + "/book/" + trip.getId() + "' style='text-decoration: none; color: white; font-weight: bold;'>Prenota ora</a></button>"
                        + "</div>";

                helper.setText(htmlMsg, true);

                javaMailSender.send(mail);
            } catch (MessagingException e) {
                throw new MailParseException(e);
            }
        }
    }

    @Override
    @Async
    public void sendPasswordResetEmail(User user, String siteURL) {
        if (user != null) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Reset Password");
            mailMessage.setText("To reset your password, click the link below:\n"
                    + siteURL + "/reset-password?token=" + user.getResetPasswordToken());

            javaMailSender.send(mailMessage);
        }
    }

    @Async
    @Override
    public void sendBookingRejectedEmail(Passenger passenger, Trip trip, Biker biker) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setTo(passenger.getUser().getEmail());
            helper.setSubject("Prenotazione rifiutata");
            helper.setFrom("gtsnowconnected@gmail.com");

            String htmlMsg = "<div style='display: block; justify-content: center; text-align: center; background-color: white; border: 2px solid red; padding: 50px; border-radius: 5px;'>"
                    + "<h2 style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>La tua prenotazione è stata rifiutata</h2>"
                    + "<p style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>La tua richiesta di prenotazione per il viaggio con " + biker.getUser().getFirstName() + " " + biker.getUser().getLastName() + " programmato per il " + trip.getDepartureTimeFormatted() + " da " + trip.getDeparturePlace() + " a " + trip.getArrivalPlace() + " è stata rifiutata. Ci scusiamo per l'inconveniente.</p>"
                    + "</div>";

            helper.setText(htmlMsg, true);

            javaMailSender.send(mail);
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }

    @Async
    @Override
    public void sendBookingAcceptedEmail(Passenger passenger, Trip trip, Biker biker) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setTo(passenger.getUser().getEmail());
            helper.setSubject("Prenotazione accettata");
            helper.setFrom("gtsnowconnected@gmail.com");

            String htmlMsg = "<div style='display: block; justify-content: center; text-align: center; background-color: white; border: 2px solid red; padding: 50px; border-radius: 5px;'>"
                    + "<h2 style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>La tua prenotazione è stata accettata</h2>"
                    + "<p style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>La tua richiesta di prenotazione per il viaggio con " + biker.getUser().getFirstName() + " " + biker.getUser().getLastName() + " programmato per il " + trip.getDepartureTimeFormatted() + " da " + trip.getDeparturePlace() + " a " + trip.getArrivalPlace() + " è stata accettata. Ti aspettiamo!</p>"
                    + "</div>";

            helper.setText(htmlMsg, true);

            javaMailSender.send(mail);
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }

    @Override
    @Async
    public void sendNoBiker(List<Passenger> passengers) {
        if(passengers.isEmpty())
            return;

        try {
            for (Passenger passenger : passengers){
                MimeMessage mail = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mail, true);

                helper.setTo(passenger.getUser().getEmail());
                helper.setSubject("Biker non più disponibile");
                helper.setFrom("gtsnowconnected@gmail.com");

                String htmlMsg = "<div style='display: block; justify-content: center; text-align: center; background-color: white; border: 2px solid red; padding: 50px; border-radius: 5px;'>"
                        + "<h2 style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>Il tuo biker non è più disponibile</h2>"
                        + "<p style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>Il tuo biker " + passenger.getBiker().getUser().getFirstName() + " " + passenger.getBiker().getUser().getLastName() + " non è più disponibile per il viaggio programmato per il " + passenger.getBiker().getTrip().getDepartureTimeFormatted() + " da " + passenger.getBiker().getTrip().getDeparturePlace() + " a " + passenger.getBiker().getTrip().getArrivalPlace() + ". Ci scusiamo per l'inconveniente.</p>"
                        + "</div>";

                helper.setText(htmlMsg, true);

                javaMailSender.send(mail);
            }
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }

    @Async
    @Override
    public void sendEmailToAdmin(QuestionForm questionForm) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setTo("palombad46@gmail.com");
            helper.setSubject("Nuova domanda da " + questionForm.getName());
            helper.setFrom("gtsnowconnected@gmail.com");

            String htmlMsg = "<div style='display: block; justify-content: center; text-align: center; background-color: white; border: 2px solid red; padding: 50px; border-radius: 5px;'>"
                    + "<h2 style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>New Question from " + questionForm.getName() + "</h2>"
                    + "<p style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>Email: " + questionForm.getEmail() + "</p>"
                    + "<p style='font-family: Franklin Gothic Medium, Arial Narrow, Arial, sans-serif;'>Question: " + questionForm.getQuestion() + "</p>"
                    + "</div>";

            helper.setText(htmlMsg, true);

            javaMailSender.send(mail);
        } catch (MessagingException e) {
            throw new MailParseException(e);
        }
    }
}