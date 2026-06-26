package io.github.biopuri.astroplanner.desktop.controller;

import io.github.biopuri.astroplanner.core.domain.*;
import io.github.biopuri.astroplanner.core.search.ObservationCriteriaEvaluator;
import io.github.biopuri.astroplanner.core.search.ObservationSearchServiceImpl;
import io.github.biopuri.astroplanner.core.search.SkyConditionEvaluator;
import io.github.biopuri.astroplanner.core.service.EphemerisService;
import io.github.biopuri.astroplanner.core.service.ObservationSearchService;
import io.github.biopuri.astroplanner.core.service.SkyConditionService;
import io.github.biopuri.astroplanner.desktop.model.ObservationWindowRow;
import io.github.biopuri.astroplanner.desktop.validation.SearchInputValidator;
import io.github.biopuri.astroplanner.ephemeris.orekit.OrekitEphemerisService;
import io.github.biopuri.astroplanner.ephemeris.orekit.OrekitSkyConditionService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Controller of the main desktop view.
 *
 * <p>This class converts UI input values into domain search requests and
 * delegates observation search to the shared Astroplanner core services.</p>
 *
 * @author seijime
 */
public class MainController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm",
                    Locale.ENGLISH);

    private final SearchInputValidator validator = new SearchInputValidator();

    private final EphemerisService ephemerisService = new OrekitEphemerisService();

    private final SkyConditionService skyConditionService =
            new OrekitSkyConditionService(ephemerisService);

    private final ObservationSearchService searchService =
            new ObservationSearchServiceImpl(
                    ephemerisService,
                    new ObservationCriteriaEvaluator(),
                    skyConditionService,
                    new SkyConditionEvaluator()
            );

    /**
     * Searches observation windows using values entered in the desktop form.
     *
     * @return rows prepared for display in the result table.
     */
    public List<ObservationWindowRow> search(
            CelestialObject object,
            String latitude,
            String longitude,
            String elevationMeters,
            String timeZone,
            LocalDate startDate,
            LocalDate endDate,
            String minAltitude,
            String maxAltitude,
            String minAzimuth,
            String maxAzimuth,
            SkyCondition skyCondition,
            String stepMinutes,
            String minimumDurationMinutes
    ) {
        validator.validateObserver(latitude, longitude, elevationMeters);
        validator.validateTimeZone(timeZone);
        validator.validateDates(startDate, endDate);
        validator.validateAngleRanges(minAltitude, maxAltitude, minAzimuth, maxAzimuth);
        validator.validateDurations(stepMinutes, minimumDurationMinutes);

        ZoneId zoneId = ZoneId.of(timeZone);

        ObserverLocation observer = new ObserverLocation(
                parseDouble(latitude),
                parseDouble(longitude),
                parseDouble(elevationMeters),
                zoneId
        );

        ObservationSearchRequest request = new ObservationSearchRequest(
                object,
                observer,
                startDate.atStartOfDay(zoneId),
                endDate.atTime(23, 59).atZone(zoneId),
                new AngleRange(
                        parseDouble(minAltitude),
                        parseDouble(maxAltitude)
                ),
                new AngleRange(
                        parseDouble(minAzimuth),
                        parseDouble(maxAzimuth)
                ),
                Duration.ofMinutes(parseLong(stepMinutes)),
                skyCondition,
                Duration.ofMinutes(parseLong(minimumDurationMinutes))
        );

        return searchService.findObservationWindows(request)
                .stream()
                .map(this::toRow)
                .toList();
    }

    private ObservationWindowRow toRow(ObservationWindow window) {
        return new ObservationWindowRow(
                DATE_TIME_FORMATTER.format(window.start()),
                DATE_TIME_FORMATTER.format(window.end()),
                window.duration().toMinutes() + " min",
                window.object().name(),
                String.format("%.2f°", window.startCoordinates().altitudeDegrees()),
                String.format("%.2f°", window.endCoordinates().altitudeDegrees()),
                String.format("%.2f°", window.startCoordinates().azimuthDegrees()),
                String.format("%.2f°", window.endCoordinates().azimuthDegrees())
        );
    }

    private double parseDouble(String value) {
        return Double.parseDouble(value.trim().replace(',', '.'));
    }

    private long parseLong(String value) {
        return Long.parseLong(value.trim());
    }
}