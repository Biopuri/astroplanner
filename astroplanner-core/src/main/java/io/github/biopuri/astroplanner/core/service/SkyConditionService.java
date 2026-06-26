package io.github.biopuri.astroplanner.core.service;

import io.github.biopuri.astroplanner.core.domain.ObserverLocation;
import io.github.biopuri.astroplanner.core.domain.SkyCondition;

import java.time.ZonedDateTime;

/**
 * Determines the sky illumination condition
 * for a given observer and time.
 *
 * @author seijime
 */
public interface SkyConditionService {

    /**
     * Calculates the current sky condition.
     *
     * @param observer observer location.
     * @param dateTime observation time.
     * @return calculated sky condition.
     */
    SkyCondition calculateSkyCondition(
            ObserverLocation observer,
            ZonedDateTime dateTime
    );

}
