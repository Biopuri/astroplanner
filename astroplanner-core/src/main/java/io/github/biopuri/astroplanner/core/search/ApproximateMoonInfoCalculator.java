package io.github.biopuri.astroplanner.core.search;

import io.github.biopuri.astroplanner.core.domain.moon.MoonInfo;
import io.github.biopuri.astroplanner.core.domain.moon.MoonPhase;
import io.github.biopuri.astroplanner.core.service.MoonInfoCalculator;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * Calculates approximate Moon information using a simple synodic month model.
 *
 * <p>This calculator is suitable for UI planning purposes, but it is not intended
 * for high-precision astronomical calculations.</p>
 *
 * @author seijime
 */
public class ApproximateMoonInfoCalculator implements MoonInfoCalculator {

    private static final double SYNODIC_MONTH_DAYS = 29.530588853;

    private static final ZonedDateTime KNOWN_NEW_MOON =
            ZonedDateTime.parse("2000-01-06T18:14:00Z");

    /**
     * Calculates approximate Moon information for the specified date and time.
     *
     * @param dateTime observation date and time
     * @return approximate Moon information
     */
    @Override
    public MoonInfo calculate(ZonedDateTime dateTime) {
        double ageDays = calculateAgeDays(dateTime);
        double illumination = calculateIllumination(ageDays);
        MoonPhase phase = calculatePhase(ageDays);

        return new MoonInfo(phase, illumination, ageDays);
    }

    private double calculateAgeDays(ZonedDateTime dateTime) {
        double daysSinceKnownNewMoon = Duration.between(KNOWN_NEW_MOON, dateTime).toSeconds() / 86_400.0;
        double ageDays = daysSinceKnownNewMoon % SYNODIC_MONTH_DAYS;

        if (ageDays < 0) {
            ageDays += SYNODIC_MONTH_DAYS;
        }

        return ageDays;
    }

    private double calculateIllumination(double ageDays) {
        double phaseAngle = 2.0 * Math.PI * ageDays / SYNODIC_MONTH_DAYS;
        return (1.0 - Math.cos(phaseAngle)) / 2.0 * 100.0;
    }

    private MoonPhase calculatePhase(double ageDays) {
        if (ageDays < 1.84566) {
            return MoonPhase.NEW_MOON;
        }
        if (ageDays < 5.53699) {
            return MoonPhase.WAXING_CRESCENT;
        }
        if (ageDays < 9.22831) {
            return MoonPhase.FIRST_QUARTER;
        }
        if (ageDays < 12.91963) {
            return MoonPhase.WAXING_GIBBOUS;
        }
        if (ageDays < 16.61096) {
            return MoonPhase.FULL_MOON;
        }
        if (ageDays < 20.30228) {
            return MoonPhase.WANING_GIBBOUS;
        }
        if (ageDays < 23.99361) {
            return MoonPhase.LAST_QUARTER;
        }
        if (ageDays < 27.68493) {
            return MoonPhase.WANING_CRESCENT;
        }

        return MoonPhase.NEW_MOON;
    }
}