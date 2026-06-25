package io.github.biopuri.astroplanner.ephemeris.orekit;

import org.orekit.data.DataContext;
import org.orekit.data.DirectoryCrawler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Initializes Orekit data providers.
 *
 * <p>Orekit requires external data files for reference frames, Earth orientation
 * parameters, time scales and celestial body ephemerides. This class registers
 * a local directory containing the required Orekit data archive contents.</p>
 *
 * @author seijime
 */
public final class OrekitDataInitializer {

    private OrekitDataInitializer() {
    }

    /**
     * Registers the Orekit data directory.
     *
     * @param dataDirectory path to the Orekit data directory.
     * @throws IOException if the directory does not exist.
     */
    public static void initialize(Path dataDirectory) throws IOException {

        if (!Files.isDirectory(dataDirectory)) {
            throw new IOException("Orekit data directory not found: " + dataDirectory);
        }

        DataContext.getDefault()
                .getDataProvidersManager()
                .addProvider(new DirectoryCrawler(dataDirectory.toFile()));
    }
}