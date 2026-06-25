package io.github.biopuri.astroplanner.cli;

import io.github.biopuri.astroplanner.ephemeris.orekit.OrekitDataInitializer;
import org.orekit.data.DataContext;
import org.orekit.time.TimeScale;

import java.nio.file.Path;

/**
 * Entry point for the Astroplanner command-line application.
 *
 * @author seijime
 */
public class AstroplannerCliApplication {

    public static void main(String[] args) throws Exception {
        OrekitDataInitializer.initialize(Path.of("orekit-data"));

        TimeScale utc = DataContext.getDefault()
                .getTimeScales()
                .getUTC();

        System.out.println("Orekit data initialized successfully");
        System.out.println("UTC time scale: " + utc);
    }
}
