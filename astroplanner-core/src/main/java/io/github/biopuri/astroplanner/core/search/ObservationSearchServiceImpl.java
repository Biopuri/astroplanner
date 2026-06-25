package io.github.biopuri.astroplanner.core.search;

import io.github.biopuri.astroplanner.core.domain.CelestialPosition;
import io.github.biopuri.astroplanner.core.domain.ObservationSearchRequest;
import io.github.biopuri.astroplanner.core.domain.ObservationWindow;
import io.github.biopuri.astroplanner.core.service.EphemerisService;
import io.github.biopuri.astroplanner.core.service.ObservationSearchService;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Default implementation of {@link ObservationSearchService}.
 *
 * <p>The search iterates through the specified time interval using a fixed
 * time step and groups consecutive matching moments into observation windows.</p>
 *
 * @author seijime
 */
public class ObservationSearchServiceImpl implements ObservationSearchService {

    private final EphemerisService ephemerisService;
    private final ObservationCriteriaEvaluator criteriaEvaluator;

    /**
     * Creates a new observation search service.
     *
     * @param ephemerisService service used to calculate celestial positions.
     * @param criteriaEvaluator evaluator of observation constraints.
     */
    public ObservationSearchServiceImpl(
            EphemerisService ephemerisService,
            ObservationCriteriaEvaluator criteriaEvaluator
    ) {
        this.ephemerisService = ephemerisService;
        this.criteriaEvaluator = criteriaEvaluator;
    }

    @Override
    public List<ObservationWindow> findObservationWindows(
            ObservationSearchRequest request
    ) {

        ObservationWindowCollector collector =
                new ObservationWindowCollector();

        ZonedDateTime current = request.start();

        while (!current.isAfter(request.end())) {

            CelestialPosition position =
                    ephemerisService.calculatePosition(
                            request.object(),
                            request.observer(),
                            current
                    );

            if (criteriaEvaluator.matches(position, request)) {

                collector.add(
                        current,
                        position.horizontalCoordinates()
                );

            } else {

                collector.closeWindow(request.object());

            }

            current = current.plus(request.step());
        }

        collector.closeWindow(request.object());

        return collector.getWindows();
    }
}
