package org.app.gtsconnected3.exception;

public class EmailAlredyExistsException extends RuntimeException{
    public EmailAlredyExistsException() {
        super("Email already exists");
    }
}
