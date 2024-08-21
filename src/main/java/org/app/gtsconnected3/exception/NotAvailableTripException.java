package org.app.gtsconnected3.exception;

public class NotAvailableTripException extends RuntimeException{
    public NotAvailableTripException() {
        super("Oops! Il viaggio non e' piu' disponibile.");
    }
}
