package io.github.biopuri.astroplanner.core.service;

import io.github.biopuri.astroplanner.core.domain.ObservationSearchRequest;
import io.github.biopuri.astroplanner.core.domain.ObservationWindow;

import java.util.List;

/**
 * Service responsible for finding time intervals during which
 * a celestial object satisfies observation criteria.
 *
 * @author seijime
 */
public interface ObservationSearchService {

    /**
     * Finds observation windows matching the given search request.
     *
     * @param request observation search criteria.
     * @return matching observation windows.
     */
    List<ObservationWindow> findObservationWindows(ObservationSearchRequest request);
}
