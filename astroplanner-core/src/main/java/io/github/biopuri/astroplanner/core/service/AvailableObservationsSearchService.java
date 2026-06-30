package io.github.biopuri.astroplanner.core.service;

import io.github.biopuri.astroplanner.core.domain.ObservationSearchRequest;
import io.github.biopuri.astroplanner.core.domain.ObservationWindow;

import java.util.List;

/**
 * Service responsible for finding observation windows
 * for all currently supported celestial objects.
 *
 * This service does not calculate coordinates directly.
 * It reuses the regular single-object observation search.
 *
 * @author seijime
 */
public interface AvailableObservationsSearchService {

    /**
     * Finds observation windows for all supported celestial objects
     * using the same observer, time interval and search criteria.
     *
     * @param request base search request.
     *                The object inside this request is ignored and replaced
     *                with every supported celestial object one by one.
     * @return observation windows for all available objects.
     */
    List<ObservationWindow> findAvailableObservations(ObservationSearchRequest request);
}