package com.codeit.earthquakeassignment.exception;

public class EarthquakeApiException extends RuntimeException {
    public EarthquakeApiException(String message) {
        super(message);
    }

    public EarthquakeApiException(String message, Throwable cause) {
        super(message, cause);
    }
}