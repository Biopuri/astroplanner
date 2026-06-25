package io.github.biopuri.astroplanner.ephemeris.orekit;

import io.github.biopuri.astroplanner.core.domain.CelestialObject;
import io.github.biopuri.astroplanner.core.domain.CelestialPosition;
import io.github.biopuri.astroplanner.core.domain.HorizontalCoordinates;
import io.github.biopuri.astroplanner.core.domain.ObserverLocation;
import io.github.biopuri.astroplanner.core.service.EphemerisService;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.TopocentricFrame;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.utils.Constants;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Ephemeris service implementation based on the Orekit library.
 *
 * <p>This class adapts Orekit's astronomical and reference-frame API to the
 * Astroplanner domain-level {@link EphemerisService} contract. It calculates
 * topocentric horizontal coordinates of celestial objects for a specific
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
        AbsoluteDate absoluteDate = toAbsoluteDate(dateTime);
        Frame inertialFrame = FramesFactory.getEME2000();
        TopocentricFrame observerFrame = createObserverFrame(observer);

        CelestialBody body = resolveBody(object);
        Vector3D bodyPosition = body.getPosition(absoluteDate, inertialFrame);

        double altitudeDegrees = FastMath.toDegrees(
                observerFrame.getElevation(bodyPosition, inertialFrame, absoluteDate)
        );

        double azimuthDegrees = FastMath.toDegrees(
                observerFrame.getAzimuth(bodyPosition, inertialFrame, absoluteDate)
        );

        return new CelestialPosition(
                new HorizontalCoordinates(altitudeDegrees, azimuthDegrees),
                Double.NaN,
                Double.NaN
        );
    }

    private TopocentricFrame createObserverFrame(ObserverLocation observer) {
        Frame earthFrame = FramesFactory.getITRF(
                org.orekit.utils.IERSConventions.IERS_2010,
                true
        );

        OneAxisEllipsoid earth = new OneAxisEllipsoid(
                Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
                Constants.WGS84_EARTH_FLATTENING,
                earthFrame
        );

        GeodeticPoint point = new GeodeticPoint(
                FastMath.toRadians(observer.latitude()),
                FastMath.toRadians(observer.longitude()),
                0.0
        );

        return new TopocentricFrame(earth, point, "observer");
    }

    private CelestialBody resolveBody(CelestialObject object) {
        return switch (object) {
            case SUN -> CelestialBodyFactory.getSun();
            case MOON -> CelestialBodyFactory.getMoon();
            case MERCURY -> CelestialBodyFactory.getMercury();
            case VENUS -> CelestialBodyFactory.getVenus();
            case MARS -> CelestialBodyFactory.getMars();
            case JUPITER -> CelestialBodyFactory.getJupiter();
            case SATURN -> CelestialBodyFactory.getSaturn();
            case URANUS -> CelestialBodyFactory.getUranus();
            case NEPTUNE -> CelestialBodyFactory.getNeptune();
        };
    }

    private AbsoluteDate toAbsoluteDate(ZonedDateTime dateTime) {
        ZonedDateTime utc = dateTime.withZoneSameInstant(ZoneOffset.UTC);
        TimeScale utcScale = org.orekit.data.DataContext.getDefault()
                .getTimeScales()
                .getUTC();

        return new AbsoluteDate(
                utc.getYear(),
                utc.getMonthValue(),
                utc.getDayOfMonth(),
                utc.getHour(),
                utc.getMinute(),
                utc.getSecond() + utc.getNano() / 1_000_000_000.0,
                utcScale
        );
    }
}