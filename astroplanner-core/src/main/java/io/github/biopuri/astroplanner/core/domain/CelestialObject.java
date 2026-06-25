package io.github.biopuri.astroplanner.core.domain;

/**
 * Supported celestial objects for which astronomical calculations
 * can be performed.
 *
 * <p>The list currently includes the Sun, the Moon and the major planets.
 * It may be extended in the future to support additional objects such as
 * dwarf planets, asteroids or comets.</p>
 *
 * @author seijime
 */
public enum CelestialObject {

    /** The Sun. */
    SUN,

    /** Earth's natural satellite. */
    MOON,

    /** Mercury. */
    MERCURY,

    /** Venus. */
    VENUS,

    /** Mars. */
    MARS,

    /** Jupiter. */
    JUPITER,

    /** Saturn. */
    SATURN,

    /** Uranus. */
    URANUS,

    /** Neptune. */
    NEPTUNE
}
