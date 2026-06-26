package io.github.biopuri.astroplanner.core.domain.moon;

import lombok.Getter;

/**
 * Represents the main visual phase of the Moon.
 *
 * @author seijime
 */
@Getter
public enum MoonPhase {

    NEW_MOON("New Moon"),
    WAXING_CRESCENT("Waxing Crescent"),
    FIRST_QUARTER("First Quarter"),
    WAXING_GIBBOUS("Waxing Gibbous"),
    FULL_MOON("Full Moon"),
    WANING_GIBBOUS("Waning Gibbous"),
    LAST_QUARTER("Last Quarter"),
    WANING_CRESCENT("Waning Crescent");

    private final String displayName;

    MoonPhase(String displayName) {
        this.displayName = displayName;
    }
}