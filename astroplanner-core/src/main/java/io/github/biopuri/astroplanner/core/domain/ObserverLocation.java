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
 * @param elevationMeters observer elevation above mean sea level in meters.
 *                        Used to improve topocentric coordinate calculations.
 * @param zoneId observer's time zone represented as a {@link ZoneId}.
 *               Used to convert local date and time to UTC during
 *               astronomical calculations.
 *
 * @author seijime
 */
public record ObserverLocation(
        double latitude,
        double longitude,
        double elevationMeters,
        ZoneId zoneId
) {
}
