package io.github.biopuri.astroplanner.core.domain;

/**
 * Horizontal coordinates of a celestial object relative to the observer.
 *
 * @param altitudeDegrees altitude above the horizon in decimal degrees.
 *                        A value of {@code 0°} corresponds to the horizon,
 *                        {@code +90°} to the zenith.
 *                        Negative values indicate that the object is below
 *                        the horizon.
 * @param azimuthDegrees azimuth measured clockwise from the geographic north.
 *                       The value is expected to be within the range
 *                       {@code [0°, 360°)}.
 *
 * @author seijime
 */
public record HorizontalCoordinates(
        double altitudeDegrees,
        double azimuthDegrees
) {
}
