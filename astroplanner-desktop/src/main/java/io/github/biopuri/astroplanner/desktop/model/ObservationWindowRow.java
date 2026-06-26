package io.github.biopuri.astroplanner.desktop.model;

import java.time.LocalDateTime;

/**
 * Table row model for displaying observation windows
 * in the desktop application.
 *
 * @param start          formatted observation window start time.
 * @param end            formatted observation window end time.
 * @param duration       formatted observation window duration.
 * @param object         observed celestial object name.
 * @param startAltitude  formatted object altitude at window start.
 * @param endAltitude    formatted object altitude at window end.
 * @param startAzimuth   formatted object azimuth at window start.
 * @param endAzimuth     formatted object azimuth at window end.
 * @param startDateTime  observation window start date and time.
 * @param endDateTime    observation window end date and time.
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
        String endAzimuth,
        LocalDateTime startDateTime,
        LocalDateTime endDateTime
) {
}