package io.github.biopuri.astroplanner.core.domain;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Search criteria for finding observation windows.
 *
 * @param object celestial object to observe.
 * @param observer observer location.
 * @param start search interval start.
 * @param end search interval end.
 * @param altitudeRange acceptable altitude range.
 * @param azimuthRange acceptable azimuth range.
 * @param step time step used during search.
 * @param skyCondition preferred sky illumination by Sun.
 * @param minimumWindowDuration minimum window duration for observation.
 *                              Windows shorter than this duration are ignored.
 *
 * @author seijime
 */
public record ObservationSearchRequest(
        CelestialObject object,
        ObserverLocation observer,
        ZonedDateTime start,
        ZonedDateTime end,
        AngleRange altitudeRange,
        AngleRange azimuthRange,
        Duration step,
        SkyCondition skyCondition,
        Duration minimumWindowDuration
) {
}
