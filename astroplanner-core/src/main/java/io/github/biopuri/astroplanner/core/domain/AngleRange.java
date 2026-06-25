package io.github.biopuri.astroplanner.core.domain;

/**
 * Inclusive range of angular values measured in degrees.
 *
 * <p>Used for observation constraints such as altitude or azimuth ranges.</p>
 *
 * @param minDegrees minimum allowed angle in degrees.
 * @param maxDegrees maximum allowed angle in degrees.
 *
 * @author seijime
 */
public record AngleRange(
        double minDegrees,
        double maxDegrees
) {

    /**
     * Checks whether the given angle belongs to this range.
     *
     * @param degrees angle in degrees.
     * @return {@code true} if the angle is within the range.
     */
    public boolean contains(double degrees) {
        return degrees >= minDegrees && degrees <= maxDegrees;
    }
}