package io.github.biopuri.astroplanner.core.search;

import io.github.biopuri.astroplanner.core.domain.CelestialPosition;
import io.github.biopuri.astroplanner.core.domain.ObservationSearchRequest;
import io.github.biopuri.astroplanner.core.domain.ObservationWindow;
import io.github.biopuri.astroplanner.core.domain.SkyCondition;
import io.github.biopuri.astroplanner.core.service.EphemerisService;
import io.github.biopuri.astroplanner.core.service.ObservationSearchService;
import io.github.biopuri.astroplanner.core.service.SkyConditionService;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class ObservationSearchServiceImpl implements ObservationSearchService {

    private final EphemerisService ephemerisService;
    private final ObservationCriteriaEvaluator criteriaEvaluator;
    private final SkyConditionService skyConditionService;
    private final SkyConditionEvaluator skyConditionEvaluator;

    @Override
    public List<ObservationWindow> findObservationWindows(
            ObservationSearchRequest request
    ) {
        ObservationWindowCollector collector = new ObservationWindowCollector();

        ZonedDateTime current = request.start();

        while (!current.isAfter(request.end())) {
            CelestialPosition position = ephemerisService.calculatePosition(
                    request.object(),
                    request.observer(),
                    current
            );

            SkyCondition actualSkyCondition = skyConditionService.calculateSkyCondition(
                    request.observer(),
                    current
            );

            boolean matchesPosition = criteriaEvaluator.matches(position, request);
            boolean matchesSkyCondition = skyConditionEvaluator.matches(
                    actualSkyCondition,
                    request.skyCondition()
            );

            if (matchesPosition && matchesSkyCondition) {
                collector.add(current, position.horizontalCoordinates());
            } else {
                collector.closeWindow(
                        request.object(),
                        request.minimumWindowDuration()
                );
            }

            current = current.plus(request.step());
        }

        collector.closeWindow(
                request.object(),
                request.minimumWindowDuration()
        );

        return collector.getWindows();
    }
}
