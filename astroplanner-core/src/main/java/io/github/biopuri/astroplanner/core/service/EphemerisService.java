package io.github.biopuri.astroplanner.core.service;

import io.github.biopuri.astroplanner.core.domain.CelestialObject;
import io.github.biopuri.astroplanner.core.domain.CelestialPosition;
import io.github.biopuri.astroplanner.core.domain.ObserverLocation;

import java.time.ZonedDateTime;

/**
 * Service responsible for calculating apparent positions of celestial objects.
 *
 * <p>This interface represents a domain-level contract. It does not depend on
 * any specific astronomical library or calculation algorithm. Concrete
 * implementations may use Swiss Ephemeris, JPARSEC, simplified formulas or
 * any other ephemeris source.</p>
 *
 * @author seijime
 */
public interface EphemerisService {

    /**
     * Calculates the apparent position of a celestial object for a given
     * observer and moment in time.
     *
     * @param object celestial object to calculate.
     * @param observer observer location on Earth.
     * @param dateTime date and time of observation.
     * @return calculated apparent position of the object.
     */
    CelestialPosition calculatePosition(
            CelestialObject object,
            ObserverLocation observer,
            ZonedDateTime dateTime
    );
}
