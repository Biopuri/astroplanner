package io.github.biopuri.astroplanner.core.service;

import io.github.biopuri.astroplanner.core.domain.moon.MoonInfo;

import java.time.ZonedDateTime;

/**
 * Calculates Moon information for a selected observation date and time.
 *
 * @author seijime
 */
public interface MoonInfoCalculator {

    /**
     * Calculates Moon information for the specified date and time.
     *
     * @param dateTime observation date and time
     * @return calculated Moon information
     */
    MoonInfo calculate(ZonedDateTime dateTime);
}