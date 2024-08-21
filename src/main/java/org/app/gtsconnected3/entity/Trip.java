package org.app.gtsconnected3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "trip")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departurePlace;
    private String arrivalPlace;
    @Column(columnDefinition= "TIMESTAMP")
    private Timestamp departureTime;
    @Column(columnDefinition= "TIMESTAMP")
    private Timestamp arrivalTime;
    @Enumerated(EnumType.STRING)
    private TripState state;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Biker> bikers;
    private String imageUrl;
    private String note;

    public Trip(String departurePlace, String arrivalPlace, Timestamp departureTime, Timestamp arrivalTime, TripState state) {
        this.departurePlace = departurePlace;
        this.arrivalPlace = arrivalPlace;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.state = state;
        this.bikers = new ArrayList<>();
    }

    public String getDepartureTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return departureTime.toLocalDateTime().format(formatter);
    }

    public String getArrivalTimeFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return arrivalTime.toLocalDateTime().format(formatter);
    }
}