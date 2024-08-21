package org.app.gtsconnected3.exception;

public class PassengerEqualsToBikerException extends RuntimeException{
    public PassengerEqualsToBikerException() {
        super("Oops! Sembra che tu stia prenotando un posto dietro te stesso.");
    }
}
