package io.github.biopuri.astroplanner.desktop.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class OrekitDataExtractor {

    private static final String RESOURCE_ZIP = "orekit-data.zip";

    private OrekitDataExtractor() {
    }

    public static Path extractToUserDirectory() throws IOException {
        Path appDir = Path.of(System.getProperty("user.home"), ".astroplanner");
        Path targetDir = appDir.resolve("orekit-data-main");

        if (Files.exists(targetDir.resolve("UTC-TAI.history"))) {
            return targetDir;
        }

        Files.createDirectories(appDir);

        try (InputStream inputStream = OrekitDataExtractor.class
                .getClassLoader()
                .getResourceAsStream(RESOURCE_ZIP)) {

            if (inputStream == null) {
                throw new IOException("Resource not found: " + RESOURCE_ZIP);
            }

            unzip(inputStream, appDir);
        }

        return targetDir;
    }

    private static void unzip(InputStream inputStream, Path targetDir) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(inputStream)) {
            ZipEntry entry;

            while ((entry = zipInputStream.getNextEntry()) != null) {
                Path outputPath = targetDir.resolve(entry.getName()).normalize();

                if (!outputPath.startsWith(targetDir)) {
                    throw new IOException("Bad zip entry: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    Files.createDirectories(outputPath);
                } else {
                    Files.createDirectories(outputPath.getParent());
                    Files.copy(zipInputStream, outputPath, StandardCopyOption.REPLACE_EXISTING);
                }

                zipInputStream.closeEntry();
            }
        }
    }
}