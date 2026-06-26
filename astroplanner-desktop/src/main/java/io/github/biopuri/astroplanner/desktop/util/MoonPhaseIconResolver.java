package io.github.biopuri.astroplanner.desktop.util;

import io.github.biopuri.astroplanner.core.domain.moon.MoonPhase;

/**
 * Resolves Moon phase icon resource paths.
 *
 * @author seijime
 */
public final class MoonPhaseIconResolver {

    private MoonPhaseIconResolver() {
    }

    /**
     * Returns icon resource path for the specified Moon phase.
     *
     * @param phase Moon phase.
     * @return icon resource path.
     */
    public static String resolve(MoonPhase phase) {
        return switch (phase) {
            case NEW_MOON -> "/images/moon/new_moon.png";
            case WAXING_CRESCENT -> "/images/moon/waxing_crescent.png";
            case FIRST_QUARTER -> "/images/moon/first_quarter.png";
            case WAXING_GIBBOUS -> "/images/moon/waxing_gibbous.png";
            case FULL_MOON -> "/images/moon/full_moon.png";
            case WANING_GIBBOUS -> "/images/moon/waning_gibbous.png";
            case LAST_QUARTER -> "/images/moon/last_quarter.png";
            case WANING_CRESCENT -> "/images/moon/waning_crescent.png";
        };
    }
}