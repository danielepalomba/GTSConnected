package org.app.gtsconnected3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "passenger")
public class Passenger{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "biker_id", nullable = false)
    private Biker biker;

    private boolean accept;

}