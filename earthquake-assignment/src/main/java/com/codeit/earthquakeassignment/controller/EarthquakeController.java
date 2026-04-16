package com.codeit.earthquakeassignment.controller;

import com.codeit.earthquakeassignment.model.Earthquake;

import com.codeit.earthquakeassignment.service.EarthquakeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/earthquakes")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EarthquakeController {
    private final EarthquakeService earthquakeService;

    @PostMapping("/fetch")
    public List<Earthquake> fetchEarthquakes() {
        return  earthquakeService.getEarthquakes();
    }

    @GetMapping
    public List<Earthquake> getAll(){
        return earthquakeService.getAllEarthquakes();
    }

    @GetMapping("/filter-mag/{mag}")
    public List<Earthquake> getEarthquakeByMag(@PathVariable Double mag){
        return earthquakeService.getEarthquakeByMag(mag);
    }

    @GetMapping("/filter-time/{time}")
    public List<Earthquake> getEarthquakeByTime(@PathVariable Instant time){
        return earthquakeService.getEarthquakeByTime(time);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id){
        earthquakeService.deleteById(id);
    }
    @GetMapping("/search")
    public List<Earthquake> search(
            @RequestParam(required = false) Double mag,
            @RequestParam(required = false) String time  // expects ISO "2026-04-16T12:00:00Z"
    ) {
        if (mag != null) {
            return earthquakeService.getEarthquakeByMag(mag);
        }
        if (time != null) {
            Instant instant = Instant.parse(time);
            return earthquakeService.getEarthquakeByTime(instant);
        }
        return earthquakeService.getAllEarthquakes();
    }
}
