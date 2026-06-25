package io.github.biopuri.astroplanner.core.search;

import io.github.biopuri.astroplanner.core.domain.CelestialPosition;
import io.github.biopuri.astroplanner.core.domain.ObservationSearchRequest;

/**
 * Evaluates whether a calculated celestial position satisfies
 * observation search criteria.
 *
 * @author seijime
 */
public class ObservationCriteriaEvaluator {

    /**
     * Checks whether the given position matches the search request.
     *
     * @param position calculated celestial position.
     * @param request observation search criteria.
     * @return {@code true} if the position satisfies all criteria.
     */
    public boolean matches(
            CelestialPosition position,
            ObservationSearchRequest request
    ) {
        return request.altitudeRange().contains(
                position.horizontalCoordinates().altitudeDegrees()
        ) && request.azimuthRange().contains(
                position.horizontalCoordinates().azimuthDegrees()
        );
    }
}