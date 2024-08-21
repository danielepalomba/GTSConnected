package org.app.gtsconnected3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User sender;
    @ManyToOne
    private Trip trip;
    @ManyToOne
    private User receiver;
    private String content;
    @Column(columnDefinition = "TIMESTAMP")
    private Timestamp timestamp;

    public Message(User sender, User receiver, Trip trip, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.trip = trip;
        this.content = content;
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Rome"));
        this.timestamp = Timestamp.valueOf(now);
    }

    public String getTimestampFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return timestamp.toLocalDateTime().format(formatter);
    }
}
