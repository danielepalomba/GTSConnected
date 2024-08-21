package org.app.gtsconnected3.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
public class RegistrationForm {
    private String email;
    private String password;
    private String name;
    private String surname;
    private boolean newsletter;
    private LocalDate birth;
}
