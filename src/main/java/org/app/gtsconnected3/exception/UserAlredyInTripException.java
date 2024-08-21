package org.app.gtsconnected3.exception;

public class UserAlredyInTripException extends RuntimeException{
    public UserAlredyInTripException() {
        super("Oops! Sembra che tu sia gia' registrato a questo viaggio.");
    }
}
