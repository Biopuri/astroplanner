package io.github.biopuri.astroplanner.core.search;

import io.github.biopuri.astroplanner.core.domain.CelestialObject;
import io.github.biopuri.astroplanner.core.domain.HorizontalCoordinates;
import io.github.biopuri.astroplanner.core.domain.ObservationWindow;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Collects consecutive matching timestamps into continuous
 * observation windows.
 *
 * @author seijime
 */
public class ObservationWindowCollector {

    private final List<ObservationWindow> windows = new ArrayList<>();

    private ZonedDateTime currentWindowStart;
    private ZonedDateTime previousMatchingTime;

    private HorizontalCoordinates startCoordinates;
    private HorizontalCoordinates previousCoordinates;

    /**
     * Adds another matching timestamp.
     *
     * @param time timestamp satisfying search criteria.
     * @param coordinates object's coordinates at the given timestamp.
     */
    public void add(
            ZonedDateTime time,
            HorizontalCoordinates coordinates
    ) {
        if (currentWindowStart == null) {
            currentWindowStart = time;
            startCoordinates = coordinates;
        }

        previousMatchingTime = time;
        previousCoordinates = coordinates;
    }

    /**
     * Closes the current observation window if one is open.
     *
     * @param object observed celestial object.
     */
    public void closeWindow(CelestialObject object) {
        if (currentWindowStart == null) {
            return;
        }

        windows.add(new ObservationWindow(
                currentWindowStart,
                previousMatchingTime,
                object,
                startCoordinates,
                previousCoordinates
        ));

        currentWindowStart = null;
        previousMatchingTime = null;
        startCoordinates = null;
        previousCoordinates = null;
    }

    /**
     * Returns collected observation windows.
     *
     * @return immutable list of observation windows.
     */
    public List<ObservationWindow> getWindows() {
        return List.copyOf(windows);
    }
}