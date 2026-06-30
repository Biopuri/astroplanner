package io.github.biopuri.astroplanner.desktop.controller;

import io.github.biopuri.astroplanner.core.domain.*;
import io.github.biopuri.astroplanner.core.search.AvailableObservationsSearchServiceImpl;
import io.github.biopuri.astroplanner.core.search.ObservationCriteriaEvaluator;
import io.github.biopuri.astroplanner.core.search.ObservationSearchServiceImpl;
import io.github.biopuri.astroplanner.core.search.SkyConditionEvaluator;
import io.github.biopuri.astroplanner.core.service.AvailableObservationsSearchService;
import io.github.biopuri.astroplanner.core.service.EphemerisService;
import io.github.biopuri.astroplanner.core.service.ObservationSearchService;
import io.github.biopuri.astroplanner.core.service.SkyConditionService;
import io.github.biopuri.astroplanner.desktop.model.ObservationWindowRow;
import io.github.biopuri.astroplanner.desktop.model.SearchFormData;
import io.github.biopuri.astroplanner.desktop.model.SearchMode;
import io.github.biopuri.astroplanner.desktop.validation.SearchInputValidator;
import io.github.biopuri.astroplanner.ephemeris.orekit.OrekitEphemerisService;
import io.github.biopuri.astroplanner.ephemeris.orekit.OrekitSkyConditionService;

import java.time.Duration;
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

    private final AvailableObservationsSearchService availableObservationsSearchService =
            new AvailableObservationsSearchServiceImpl(searchService);

    /**
     * Searches observation windows using values entered in the desktop form.
     *
     * <p>The method supports two desktop search modes:
     * selected object search and all objects search.</p>
     *
     * @return rows prepared for display in the result table.
     */
    public List<ObservationWindowRow> search(SearchFormData data) {
        ObservationSearchRequest request = buildSearchRequest(data);

        List<ObservationWindow> windows;

        if (data.searchMode() == SearchMode.ALL_OBJECTS) {
            windows = availableObservationsSearchService.findAvailableObservations(request);
        } else {
            windows = searchService.findObservationWindows(request);
        }

        return windows.stream()
                .map(this::toRow)
                .toList();
    }

    /**
     * Converts desktop form values into a domain search request.
     */
    private ObservationSearchRequest buildSearchRequest(SearchFormData data) {
        validator.validateObserver(
                data.latitude(),
                data.longitude(),
                data.elevationMeters()
        );
        validator.validateTimeZone(data.timeZone());
        validator.validateDates(data.startDate(), data.endDate());
        validator.validateAngleRanges(
                data.minAltitude(),
                data.maxAltitude(),
                data.minAzimuth(),
                data.maxAzimuth()
        );
        validator.validateDurations(
                data.stepMinutes(),
                data.minimumDurationMinutes()
        );

        ZoneId zoneId = ZoneId.of(data.timeZone().trim());

        ObserverLocation observer = new ObserverLocation(
                parseDouble(data.latitude()),
                parseDouble(data.longitude()),
                parseOptionalDouble(data.elevationMeters(), 0.0),
                zoneId
        );

        return new ObservationSearchRequest(
                data.object(),
                observer,
                data.startDate().atStartOfDay(zoneId),
                data.endDate().atTime(23, 59).atZone(zoneId),
                new AngleRange(
                        parseOptionalDouble(data.minAltitude(), -90.0),
                        parseOptionalDouble(data.maxAltitude(), 90.0)
                ),
                new AngleRange(
                        parseOptionalDouble(data.minAzimuth(), 0.0),
                        parseOptionalDouble(data.maxAzimuth(), 360.0)
                ),
                Duration.ofMinutes(parseOptionalLong(data.stepMinutes(), 1L)),
                data.skyCondition(),
                Duration.ofMinutes(parseOptionalLong(data.minimumDurationMinutes(), 0L))
        );
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
                String.format("%.2f°", window.endCoordinates().azimuthDegrees()),
                window.start().toLocalDateTime(),
                window.end().toLocalDateTime()
        );
    }

    private double parseOptionalDouble(
            String value,
            double defaultValue
    ) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return parseDouble(value);
    }

    private long parseOptionalLong(
            String value,
            long defaultValue
    ) {
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        return parseLong(value);
    }

    private double parseDouble(String value) {
        return Double.parseDouble(value.trim().replace(',', '.'));
    }

    private long parseLong(String value) {
        return Long.parseLong(value.trim());
    }
}