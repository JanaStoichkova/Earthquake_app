package com.codeit.earthquakeassignment.service;
import com.codeit.earthquakeassignment.dto.GeoJsonResponse;
import com.codeit.earthquakeassignment.dto.Properties;
import com.codeit.earthquakeassignment.exception.EarthquakeApiException;
import com.codeit.earthquakeassignment.repository.EarthquakeRepository;
import com.codeit.earthquakeassignment.model.Earthquake;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class EarthquakeService {
    private static final String USGS_URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";
    private final EarthquakeRepository earthquakeRepository;
    private final RestTemplate restTemplate;

    public List<Earthquake> getEarthquakes() {
        earthquakeRepository.deleteAll();
        GeoJsonResponse response;
        try {
            response = this.restTemplate.getForObject(USGS_URL, GeoJsonResponse.class);
        } catch (RestClientException e) {
            throw new EarthquakeApiException("Failed to fetch earthquake data from USGS API", e);
        }
        if (response == null) {
            throw new EarthquakeApiException("Empty response from USGS API");
        }
        if (response.features() == null) {
            return List.of();
        }
        List<Earthquake> earthquakes = response.features().stream()
                .filter(f -> f != null && f.properties() != null && f.geometry() != null)
                .filter(f -> f.geometry().coordinates() != null && f.geometry().coordinates().size() >= 2)
                .filter(f -> f.properties().mag() != null && f.properties().time() != null)
                .map(f -> {
                    Properties properties = f.properties();
                    List<Double> coords = f.geometry().coordinates();
                    Double longitude = coords.get(0);
                    Double latitude = coords.get(1);

                    return new Earthquake(
                            properties.mag(),
                            properties.magType(),
                            properties.place(),
                            properties.title(),
                            Instant.ofEpochMilli(properties.time()),
                            longitude,
                            latitude
                    );
                })
                .collect(Collectors.toList());
        return earthquakeRepository.saveAll(earthquakes);
    }

    public List<Earthquake> getAllEarthquakes() {
        return earthquakeRepository.findAll();
    }

    public List<Earthquake> getEarthquakeByMag(Double mag) {
        return earthquakeRepository.findByMagGreaterThan(mag);
    }

    public List<Earthquake> getEarthquakeByTime(Instant time) {
        return earthquakeRepository.findByTimeGreaterThan(time);
    }

    public void deleteById(Long id) {
        earthquakeRepository.deleteById(id);
    }

}
