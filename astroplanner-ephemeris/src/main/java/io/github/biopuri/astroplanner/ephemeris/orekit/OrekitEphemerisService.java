package io.github.biopuri.astroplanner.ephemeris.orekit;

import io.github.biopuri.astroplanner.core.domain.CelestialObject;
import io.github.biopuri.astroplanner.core.domain.CelestialPosition;
import io.github.biopuri.astroplanner.core.domain.ObserverLocation;
import io.github.biopuri.astroplanner.core.service.EphemerisService;

import java.time.ZonedDateTime;

/**
 * Ephemeris service implementation based on the Orekit library.
 *
 * <p>This class adapts Orekit's astronomical and reference-frame API to the
 * Astroplanner domain-level {@link EphemerisService} contract. It is responsible
 * for calculating apparent positions of celestial objects for a specific
 * observer and moment in time.</p>
 *
 * @author seijime
 */
public class OrekitEphemerisService implements EphemerisService {

    @Override
    public CelestialPosition calculatePosition(
            CelestialObject object,
            ObserverLocation observer,
            ZonedDateTime dateTime
    ) {
        throw new UnsupportedOperationException("Orekit integration is not implemented yet");
    }
}
