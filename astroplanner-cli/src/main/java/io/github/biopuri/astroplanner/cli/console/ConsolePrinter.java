package io.github.biopuri.astroplanner.cli.console;

import io.github.biopuri.astroplanner.core.domain.ObservationWindow;

import java.util.List;

/**
 * Prints command-line output for observation search results.
 *
 * @author seijime
 */
public class ConsolePrinter {

    /**
     * Prints found observation windows.
     *
     * @param windows observation windows to print.
     */
    public void printObservationWindows(List<ObservationWindow> windows) {

        if (windows.isEmpty()) {
            System.out.println("No observation windows found.");
            return;
        }

        windows.forEach(window -> System.out.printf(
                "%s — %s | %s | alt %.2f -> %.2f | az %.2f -> %.2f%n",
                window.start(),
                window.end(),
                window.object(),
                window.startCoordinates().altitudeDegrees(),
                window.endCoordinates().altitudeDegrees(),
                window.startCoordinates().azimuthDegrees(),
                window.endCoordinates().azimuthDegrees()
        ));
    }
}
