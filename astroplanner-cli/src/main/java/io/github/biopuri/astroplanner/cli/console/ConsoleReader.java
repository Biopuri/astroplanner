package io.github.biopuri.astroplanner.cli.console;

import io.github.biopuri.astroplanner.core.domain.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Scanner;

/**
 * Reads observation search parameters from the command line.
 *
 * @author seijime
 */
public class ConsoleReader {

    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads observer location from the command line.
     *
     * @return observer location.
     */
    public ObserverLocation readObserverLocation() {
        System.out.print("Latitude: ");
        double latitude = Double.parseDouble(scanner.nextLine());

        System.out.print("Longitude: ");
        double longitude = Double.parseDouble(scanner.nextLine());

        System.out.print("Elevation above sea level in meters: ");
        double elevationMeters = Double.parseDouble(scanner.nextLine());

        System.out.print("Time zone, for example Europe/Moscow: ");
        ZoneId zoneId = ZoneId.of(scanner.nextLine());

        return new ObserverLocation(latitude, longitude, elevationMeters, zoneId);
    }

    /**
     * Reads observation search request from the command line.
     *
     * @param observer observer location.
     * @return observation search request.
     */
    public ObservationSearchRequest readObservationSearchRequest(ObserverLocation observer) {
        CelestialObject object = readCelestialObject();

        System.out.print("Start date, yyyy-MM-dd: ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());

        System.out.print("End date, yyyy-MM-dd: ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        AngleRange altitudeRange = readAngleRange("altitude");
        AngleRange azimuthRange = readAngleRange("azimuth");

        System.out.print("Search step in minutes: ");
        long stepMinutes = Long.parseLong(scanner.nextLine());

        SkyCondition skyCondition = readSkyCondition();

        System.out.print("Minimum window duration in minutes: ");
        long minimumWindowMinutes = Long.parseLong(scanner.nextLine());

        return new ObservationSearchRequest(
                object,
                observer,
                startDate.atStartOfDay(observer.zoneId()),
                endDate.atTime(23, 59).atZone(observer.zoneId()),
                altitudeRange,
                azimuthRange,
                Duration.ofMinutes(stepMinutes),
                skyCondition,
                Duration.ofMinutes(minimumWindowMinutes)
        );
    }

    private CelestialObject readCelestialObject() {
        System.out.println("Choose celestial object:");
        CelestialObject[] objects = CelestialObject.values();

        for (int i = 0; i < objects.length; i++) {
            System.out.printf("%d. %s%n", i + 1, objects[i]);
        }

        System.out.print("> ");
        int selected = Integer.parseInt(scanner.nextLine());

        return objects[selected - 1];
    }

    private AngleRange readAngleRange(String name) {
        System.out.printf("Min %s degrees: ", name);
        double min = Double.parseDouble(scanner.nextLine());

        System.out.printf("Max %s degrees: ", name);
        double max = Double.parseDouble(scanner.nextLine());

        return new AngleRange(min, max);
    }

    private SkyCondition readSkyCondition() {
        System.out.println("Choose sky condition:");
        SkyCondition[] conditions = SkyCondition.values();

        for (int i = 0; i < conditions.length; i++) {
            System.out.printf("%d. %s%n", i + 1, conditions[i]);
        }

        System.out.print("> ");
        int selected = Integer.parseInt(scanner.nextLine());

        return conditions[selected - 1];
    }
}
