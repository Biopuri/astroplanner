package io.github.biopuri.astroplanner.cli;

import io.github.biopuri.astroplanner.cli.console.ConsolePrinter;
import io.github.biopuri.astroplanner.cli.console.ConsoleReader;
import io.github.biopuri.astroplanner.core.domain.*;
import io.github.biopuri.astroplanner.core.search.ObservationCriteriaEvaluator;
import io.github.biopuri.astroplanner.core.search.ObservationSearchServiceImpl;
import io.github.biopuri.astroplanner.core.search.SkyConditionEvaluator;
import io.github.biopuri.astroplanner.core.service.EphemerisService;
import io.github.biopuri.astroplanner.core.service.ObservationSearchService;
import io.github.biopuri.astroplanner.core.service.SkyConditionService;
import io.github.biopuri.astroplanner.ephemeris.orekit.OrekitDataInitializer;
import io.github.biopuri.astroplanner.ephemeris.orekit.OrekitEphemerisService;
import io.github.biopuri.astroplanner.ephemeris.orekit.OrekitSkyConditionService;
import org.orekit.data.DataContext;
import org.orekit.time.TimeScale;

import java.nio.file.Path;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Entry point for the Astroplanner command-line application.
 *
 * @author seijime
 */
public class AstroplannerCliApplication {

    public static void main(String[] args) throws Exception {
        OrekitDataInitializer.initialize(Path.of("orekit-data"));

        ConsoleReader reader = new ConsoleReader();
        ConsolePrinter printer = new ConsolePrinter();

        EphemerisService ephemerisService = new OrekitEphemerisService();
        SkyConditionService skyConditionService = new OrekitSkyConditionService(ephemerisService);

        ObservationSearchService searchService = new ObservationSearchServiceImpl(
                ephemerisService,
                new ObservationCriteriaEvaluator(),
                skyConditionService,
                new SkyConditionEvaluator()
        );

        ObserverLocation observer = reader.readObserverLocation();
        ObservationSearchRequest request = reader.readObservationSearchRequest(observer);

        List<ObservationWindow> windows = searchService.findObservationWindows(request);

        printer.printObservationWindows(windows);
    }
}
