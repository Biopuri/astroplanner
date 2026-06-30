package io.github.biopuri.astroplanner.desktop.model;

/**
 * Search mode selected in the desktop UI.
 *
 * <p>This enum belongs to the desktop module because it describes
 * how the user wants to run the search, not a real astronomical object.</p>
 *
 * @author seijime
 */
public enum SearchMode {

    /**
     * Search only for the celestial object selected in the object field.
     */
    SELECTED_OBJECT,

    /**
     * Search for all celestial objects supported by the application.
     */
    ALL_OBJECTS
}