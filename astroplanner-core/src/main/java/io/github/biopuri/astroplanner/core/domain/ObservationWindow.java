package io.github.biopuri.astroplanner.core.domain;

import java.time.ZonedDateTime;

/**
 * Time interval during which a celestial object satisfies
 * the specified observation criteria.
 *
 * @param start beginning of the observation window.
 * @param end end of the observation window.
 * @param object observed celestial object.
 * @param startCoordinates object's horizontal coordinates at the beginning
 *                         of the observation window.
 * @param endCoordinates object's horizontal coordinates at the end
 *                       of the observation window.
 *
 * @author seijime
 */
public record ObservationWindow(
        ZonedDateTime start,
        ZonedDateTime end,
        CelestialObject object,
        HorizontalCoordinates startCoordinates,
        HorizontalCoordinates endCoordinates
) {
}
