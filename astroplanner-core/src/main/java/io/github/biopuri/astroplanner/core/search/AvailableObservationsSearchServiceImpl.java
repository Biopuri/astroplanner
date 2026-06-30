package io.github.biopuri.astroplanner.core.search;

import io.github.biopuri.astroplanner.core.domain.CelestialObject;
import io.github.biopuri.astroplanner.core.domain.ObservationSearchRequest;
import io.github.biopuri.astroplanner.core.domain.ObservationWindow;
import io.github.biopuri.astroplanner.core.service.AvailableObservationsSearchService;
import io.github.biopuri.astroplanner.core.service.ObservationSearchService;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Default implementation of {@link AvailableObservationsSearchService}.
 * The implementation runs the existing single-object search
 * for every supported celestial object and combines the results
 * into one sorted list.
 *
 * @author seijime
 */
@RequiredArgsConstructor
public class AvailableObservationsSearchServiceImpl implements AvailableObservationsSearchService {

    private final ObservationSearchService observationSearchService;

    @Override
    public List<ObservationWindow> findAvailableObservations(ObservationSearchRequest request) {
        return Arrays.stream(CelestialObject.values())
                /*
                 * The Sun is technically a supported object,
                 * but in "available observations" mode it is usually noise.
                 * We search for night sky objects here.
                 */
                .filter(object -> object != CelestialObject.SUN)
                .flatMap(object -> observationSearchService.findObservationWindows(copyWithObject(request, object)).stream())
                .sorted(Comparator.comparing(ObservationWindow::start))
                .toList();
    }

    /**
     * Creates a copy of the original request with another celestial object.
     *
     * All other search criteria stay unchanged:
     * observer, time interval, altitude range, azimuth range,
     * step, sky condition and minimum window duration.
     */
    private ObservationSearchRequest copyWithObject(
            ObservationSearchRequest request,
            CelestialObject object
    ) {
        return new ObservationSearchRequest(
                object,
                request.observer(),
                request.start(),
                request.end(),
                request.altitudeRange(),
                request.azimuthRange(),
                request.step(),
                request.skyCondition(),
                request.minimumWindowDuration()
        );
    }
}