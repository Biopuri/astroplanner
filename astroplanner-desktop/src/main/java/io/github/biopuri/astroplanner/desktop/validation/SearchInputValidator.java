package io.github.biopuri.astroplanner.desktop.validation;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Validates desktop search form input values.
 *
 * @author seijime
 */
public class SearchInputValidator {

    /**
     * Validates observer coordinates.
     *
     * @param latitude observer latitude text.
     * @param longitude observer longitude text.
     * @param elevationMeters observer elevation text.
     */
    public void validateObserver(
            String latitude,
            String longitude,
            String elevationMeters
    ) {
        double parsedLatitude = parseDouble(latitude, "Latitude");
        double parsedLongitude = parseDouble(longitude, "Longitude");

        if (!isBlank(elevationMeters)) {
            parseDouble(elevationMeters, "Elevation");
        }

        if (parsedLatitude < -90.0 || parsedLatitude > 90.0) {
            throw new InputValidationException(
                    "Latitude must be between -90 and 90 degrees!",
                    SearchField.LATITUDE);
        }

        if (parsedLongitude < -180.0 || parsedLongitude > 180.0) {
            throw new InputValidationException(
                    "Longitude must be between -180 and 180 degrees!",
                    SearchField.LONGITUDE);
        }
    }

    /**
     * Validates time zone identifier.
     *
     * @param timeZone time zone text.
     */
    public void validateTimeZone(String timeZone) {
        if (isBlank(timeZone)) {
            throw new InputValidationException(
                    "Time zone is required!",
                    SearchField.TIME_ZONE);
        }

        try {
            ZoneId.of(timeZone.trim());
        } catch (Exception exception) {
            throw new InputValidationException(
                    "Invalid time zone: " + timeZone,
                    SearchField.TIME_ZONE);
        }
    }

    /**
     * Validates search dates.
     *
     * @param startDate search start date.
     * @param endDate search end date.
     */
    public void validateDates(
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate == null) {
            throw new InputValidationException(
                    "Start date is required!",
                    SearchField.START_DATE);
        }

        if (endDate == null) {
            throw new InputValidationException(
                    "End date is required!",
                    SearchField.END_DATE);
        }

        if (startDate.isAfter(endDate)) {
            throw new InputValidationException(
                    "Start date must be before or equal to end date!",
                    SearchField.START_DATE);
        }
    }

    /**
     * Validates angle ranges.
     *
     * @param minAltitude minimum altitude text.
     * @param maxAltitude maximum altitude text.
     * @param minAzimuth minimum azimuth text.
     * @param maxAzimuth maximum azimuth text.
     */
    public void validateAngleRanges(
            String minAltitude,
            String maxAltitude,
            String minAzimuth,
            String maxAzimuth
    ) {
        double parsedMinAltitude = isBlank(minAltitude)
                ? -90.0
                : parseDouble(minAltitude, "Minimum altitude");
        double parsedMaxAltitude = isBlank(maxAltitude)
                ? 90.0
                : parseDouble(maxAltitude, "Maximum altitude");
        double parsedMinAzimuth = isBlank(minAzimuth)
                ? 0.0
                : parseDouble(minAzimuth, "Minimum azimuth");
        double parsedMaxAzimuth = isBlank(maxAzimuth)
                ? 360.0
                : parseDouble(maxAzimuth, "Maximum azimuth");

        if (parsedMinAltitude > parsedMaxAltitude) {
            throw new InputValidationException(
                    "Minimum altitude must be less than or equal to maximum altitude!",
                    SearchField.MIN_ALTITUDE);
        }

        if (parsedMinAzimuth > parsedMaxAzimuth) {
            throw new InputValidationException(
                    "Minimum azimuth must be less than or equal to maximum azimuth!",
                    SearchField.MIN_AZIMUTH);
        }

        if (parsedMinAzimuth < 0.0 || parsedMaxAzimuth > 360.0) {
            throw new InputValidationException(
                    "Azimuth must be between 0 and 360 degrees!",
                    SearchField.MIN_AZIMUTH,
                    SearchField.MAX_AZIMUTH);
        }
    }

    /**
     * Validates search step and minimum window duration.
     *
     * @param stepMinutes search step text.
     * @param minimumDurationMinutes minimum window duration text.
     */
    public void validateDurations(
            String stepMinutes,
            String minimumDurationMinutes
    ) {
        long parsedStepMinutes = isBlank(stepMinutes)
                ? 1L
                : parseLong(stepMinutes, "Step");
        long parsedMinimumDurationMinutes = isBlank(minimumDurationMinutes)
                ? 0L
                : parseLong(minimumDurationMinutes, "Minimum duration");

        if (parsedStepMinutes <= 0) {
            throw new InputValidationException(
                    "Step must be greater than 0 minutes!",
                    SearchField.STEP);
        }

        if (parsedMinimumDurationMinutes < 0) {
            throw new InputValidationException(
                    "Minimum duration must be greater than or equal to 0 minutes!",
                    SearchField.MIN_DURATION);
        }
    }

    private double parseDouble(String value, String fieldName) {
        if (isBlank(value)) {
            throw new InputValidationException(fieldName + " is required!");
        }

        try {
            return Double.parseDouble(value.trim().replace(',', '.'));
        } catch (NumberFormatException exception) {
            throw new InputValidationException(fieldName + " must be a number!");
        }
    }

    private long parseLong(String value, String fieldName) {
        if (isBlank(value)) {
            throw new InputValidationException(fieldName + " is required!");
        }

        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException exception) {
            throw new InputValidationException(fieldName + " must be an integer number!");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}