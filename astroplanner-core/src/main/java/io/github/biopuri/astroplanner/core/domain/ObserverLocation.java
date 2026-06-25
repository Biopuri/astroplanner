package io.github.biopuri.astroplanner.core.domain;

import java.time.ZoneId;

/**
 * Geographic location of an observer on Earth.
 *
 * @param latitude observer latitude in decimal degrees.
 *                 Positive values indicate the Northern Hemisphere,
 *                 negative values indicate the Southern Hemisphere.
 * @param longitude observer longitude in decimal degrees.
 *                  Positive values indicate east of the Greenwich meridian,
 *                  negative values indicate west.
 * @param zoneId time zone of the observer represented as a {@link ZoneId}.
 *  *               Used for converting between local date/time and UTC during
 *  *               astronomical calculations.
 *
 * @author seijime
 */
public record ObserverLocation(
        double latitude,
        double longitude,
        ZoneId zoneId
) {
}
