package io.github.biopuri.astroplanner.desktop.model;

import io.github.biopuri.astroplanner.core.domain.CelestialObject;
import io.github.biopuri.astroplanner.core.domain.SkyCondition;

import java.time.LocalDate;

/**
 * Data entered by the user in the observation search form.
 *
 * <p>This record keeps raw UI values as strings where validation and parsing
 * are performed later by the controller and validator.</p>
 *
 * @author seijime
 */
public record SearchFormData(
        SearchMode searchMode,
        CelestialObject object,
        String latitude,
        String longitude,
        String elevationMeters,
        String timeZone,
        LocalDate startDate,
        LocalDate endDate,
        String minAltitude,
        String maxAltitude,
        String minAzimuth,
        String maxAzimuth,
        SkyCondition skyCondition,
        String stepMinutes,
        String minimumDurationMinutes
) {
}