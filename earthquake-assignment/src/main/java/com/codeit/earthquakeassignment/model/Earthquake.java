package com.codeit.earthquakeassignment.model;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Earthquake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double mag;
    private String magType;
    private String place;
    private String title;
    private Instant time;
    private Double longitude;
    private Double latitude;


    public Earthquake(Double mag, String magType, String place, String title, Instant time, Double longitude, Double latitude) {
        this.mag = mag;
        this.magType = magType;
        this.place = place;
        this.title = title;
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
