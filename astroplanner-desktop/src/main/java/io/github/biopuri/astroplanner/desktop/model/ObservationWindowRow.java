package io.github.biopuri.astroplanner.desktop.model;

/**
 * Table row model for displaying observation windows
 * in the desktop application.
 *
 * @author seijime
 */
public record ObservationWindowRow(
        String start,
        String end,
        String duration,
        String object,
        String startAltitude,
        String endAltitude,
        String startAzimuth,
        String endAzimuth
) {
}
