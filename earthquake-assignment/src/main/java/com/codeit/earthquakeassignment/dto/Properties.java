package com.codeit.earthquakeassignment.dto;

import java.time.Instant;

public record Properties(
        Double mag,
        String place,
        Long time,
        String title,
        String magType
) {}