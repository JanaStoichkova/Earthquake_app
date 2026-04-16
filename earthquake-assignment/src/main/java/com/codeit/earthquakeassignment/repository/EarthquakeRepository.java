package com.codeit.earthquakeassignment.repository;
import com.codeit.earthquakeassignment.model.Earthquake;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EarthquakeRepository extends JpaRepository<Earthquake,Long> {
    List<Earthquake> findByMagGreaterThan(Double mag);
    List<Earthquake> findByTimeGreaterThan(Instant time);

}
