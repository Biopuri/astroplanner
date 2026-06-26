package io.github.biopuri.astroplanner.ephemeris.orekit;

import io.github.biopuri.astroplanner.core.domain.CelestialObject;
import io.github.biopuri.astroplanner.core.domain.CelestialPosition;
import io.github.biopuri.astroplanner.core.domain.ObserverLocation;
import io.github.biopuri.astroplanner.core.domain.SkyCondition;
import io.github.biopuri.astroplanner.core.service.EphemerisService;
import io.github.biopuri.astroplanner.core.service.SkyConditionService;

import java.time.ZonedDateTime;

/**
 * Sky condition service based on Orekit ephemeris calculations.
 *
 * <p>The implementation calculates the Sun altitude for the given observer
 * and classifies the sky illumination level according to common twilight
 * thresholds.</p>
 *
 * @author seijime
 */
public class OrekitSkyConditionService implements SkyConditionService {

    private final EphemerisService ephemerisService;

    /**
     * Creates a new sky condition service.
     *
     * @param ephemerisService service used to calculate the Sun position.
     */
    public OrekitSkyConditionService(EphemerisService ephemerisService) {
        this.ephemerisService = ephemerisService;
    }

    @Override
    public SkyCondition calculateSkyCondition(
            ObserverLocation observer,
            ZonedDateTime dateTime
    ) {
        CelestialPosition sunPosition = ephemerisService.calculatePosition(
                CelestialObject.SUN,
                observer,
                dateTime
        );

        double sunAltitude = sunPosition.horizontalCoordinates().altitudeDegrees();

        if (sunAltitude <= -18.0) {
            return SkyCondition.ASTRONOMICAL_NIGHT;
        }

        if (sunAltitude <= -12.0) {
            return SkyCondition.NAUTICAL_TWILIGHT;
        }

        if (sunAltitude <= -6.0) {
            return SkyCondition.CIVIL_TWILIGHT;
        }

        if (sunAltitude < 0.0) {
            return SkyCondition.AFTER_SUNSET;
        }

        return SkyCondition.ANYTIME;
    }
}
