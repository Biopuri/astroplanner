package io.github.biopuri.astroplanner.core.domain.moon;

/**
 * Contains calculated Moon information for a selected observation date.
 *
 * @param phase        visual Moon phase
 * @param illumination illuminated part of the Moon in percent
 * @param ageDays      Moon age in days from the last new moon
 *
 * @author seijime
 */
public record MoonInfo(
        MoonPhase phase,
        double illumination,
        double ageDays
) {
}
