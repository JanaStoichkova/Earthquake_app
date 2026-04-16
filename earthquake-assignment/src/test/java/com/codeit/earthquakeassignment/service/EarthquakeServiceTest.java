package com.codeit.earthquakeassignment.service;

import com.codeit.earthquakeassignment.dto.Feature;
import com.codeit.earthquakeassignment.dto.GeoJsonResponse;
import com.codeit.earthquakeassignment.dto.Geometry;
import com.codeit.earthquakeassignment.dto.Properties;
import com.codeit.earthquakeassignment.exception.EarthquakeApiException;
import com.codeit.earthquakeassignment.model.Earthquake;
import com.codeit.earthquakeassignment.repository.EarthquakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EarthquakeServiceTest {

    @Autowired
    private EarthquakeService earthquakeService;

    @MockBean  // Fixed: @MockBean, not @MockitoBean
    private EarthquakeRepository earthquakeRepository;

    @MockBean  // Fixed: @MockBean, not @MockitoBean
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        reset(earthquakeRepository, restTemplate);
    }

    @Test
    void testGetAllEarthquakes_ReturnsList() {
        Earthquake earthquake = new Earthquake(2.5, "ml", "Test Place", "M 2.5 - Test Place",
                Instant.now(), -155.0, 19.0);
        when(earthquakeRepository.findAll()).thenReturn(List.of(earthquake));

        List<Earthquake> result = earthquakeService.getAllEarthquakes();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(2.5, result.get(0).getMag());
        verify(earthquakeRepository, times(1)).findAll();
    }

    @Test
    void testGetAllEarthquakes_EmptyList() {
        when(earthquakeRepository.findAll()).thenReturn(Collections.emptyList());

        List<Earthquake> result = earthquakeService.getAllEarthquakes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(earthquakeRepository, times(1)).findAll();
    }

    @Test
    void testGetEarthquakeByMag_ReturnsFilteredResults() {
        Earthquake eq1 = new Earthquake(1.5, "ml", "Place 1", "M 1.5 - Place 1",
                Instant.now(), -155.0, 19.0);
        Earthquake eq2 = new Earthquake(3.5, "ml", "Place 2", "M 3.5 - Place 2",
                Instant.now(), -155.0, 19.0);
        when(earthquakeRepository.findByMagGreaterThan(2.0)).thenReturn(List.of(eq2));

        List<Earthquake> result = earthquakeService.getEarthquakeByMag(2.0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(3.5, result.get(0).getMag());
        verify(earthquakeRepository, times(1)).findByMagGreaterThan(2.0);
    }

    @Test
    void testGetEarthquakeByTime_ReturnsFilteredResults() {
        Instant testTime = Instant.parse("2026-04-16T08:00:00Z");
        Instant laterTime = Instant.parse("2026-04-16T09:00:00Z");
        Earthquake earthquake = new Earthquake(2.5, "ml", "Test Place", "M 2.5 - Test Place",
                laterTime, -155.0, 19.0);
        when(earthquakeRepository.findByTimeGreaterThan(testTime)).thenReturn(List.of(earthquake));

        List<Earthquake> result = earthquakeService.getEarthquakeByTime(testTime);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(earthquakeRepository, times(1)).findByTimeGreaterThan(testTime);
    }

    @Test
    void testDeleteById_CallsRepository() {
        Long testId = 1L;

        earthquakeService.deleteById(testId);

        verify(earthquakeRepository, times(1)).deleteById(testId);
    }

    @Test
    void testGetEarthquakes_FetchesFromApiAndSaves() {
        Properties props = new Properties(2.5, "Test Place", System.currentTimeMillis(), "M 2.5 - Test Place", "ml");
        Geometry geometry = new Geometry(List.of(-155.0, 19.0));
        Feature feature = new Feature(props, geometry);
        GeoJsonResponse response = new GeoJsonResponse(List.of(feature));

        when(restTemplate.getForObject(anyString(), eq(GeoJsonResponse.class))).thenReturn(response);
        when(earthquakeRepository.saveAll(any())).thenReturn(Collections.emptyList());

        List<Earthquake> result = earthquakeService.getEarthquakes();

        assertNotNull(result);
        verify(earthquakeRepository, times(1)).deleteAll();
        verify(restTemplate, times(1)).getForObject(anyString(), eq(GeoJsonResponse.class));
    }

    @Test
    void testGetEarthquakes_ThrowsException_WhenApiFails() {
        when(restTemplate.getForObject(anyString(), eq(GeoJsonResponse.class)))
                .thenThrow(new RestClientException("API unavailable"));

        EarthquakeApiException exception = assertThrows(EarthquakeApiException.class,
                () -> earthquakeService.getEarthquakes());

        assertTrue(exception.getMessage().contains("Failed to fetch earthquake data from USGS API"));
        verify(restTemplate, times(1)).getForObject(anyString(), eq(GeoJsonResponse.class));
    }

    @Test
    void testGetEarthquakes_ReturnsEmpty_WhenNoFeatures() {
        GeoJsonResponse emptyResponse = new GeoJsonResponse(Collections.emptyList());
        when(restTemplate.getForObject(anyString(), eq(GeoJsonResponse.class))).thenReturn(emptyResponse);

        List<Earthquake> result = earthquakeService.getEarthquakes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(earthquakeRepository, times(1)).deleteAll();
    }
}