package com.codeit.earthquakeassignment.dto;

import java.util.List;

public record GeoJsonResponse(List<Feature> features)  {
}
