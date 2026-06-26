package io.github.biopuri.astroplanner.core.domain;

/**
 * Defines sky illumination conditions that may be required
 * during an observation search.
 *
 * @author seijime
 */
public enum SkyCondition {

    /**
     * No restrictions.
     */
    ANYTIME,

    /**
     * The Sun is below the horizon.
     */
    AFTER_SUNSET,

    /**
     * The Sun is below -6°.
     */
    CIVIL_TWILIGHT,

    /**
     * The Sun is below -12°.
     */
    NAUTICAL_TWILIGHT,

    /**
     * The Sun is below -18°.
     */
    ASTRONOMICAL_NIGHT
}
