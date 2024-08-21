package org.app.gtsconnected3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "biker")
public class Biker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @OneToOne(mappedBy = "biker", cascade = CascadeType.ALL, orphanRemoval = true)
    private Passenger passenger;

    private String motorcycle;
    private boolean allowPassenger;

}
