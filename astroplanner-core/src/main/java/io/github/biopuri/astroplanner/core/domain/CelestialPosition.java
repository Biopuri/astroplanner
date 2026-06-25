package io.github.biopuri.astroplanner.core.domain;

/**
 * Apparent position and observational properties of a celestial object
 * for a specific observer at a particular moment in time.
 *
 * @param horizontalCoordinates horizontal coordinates of the object.
 * @param distanceAu distance from Earth to the object in astronomical units.
 * @param magnitude apparent visual magnitude.
 *
 * @author seijime
 */
public record CelestialPosition(
        HorizontalCoordinates horizontalCoordinates,
        double distanceAu,
        double magnitude
) {
}
