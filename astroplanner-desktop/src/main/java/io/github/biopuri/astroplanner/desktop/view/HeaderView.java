package io.github.biopuri.astroplanner.desktop.view;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * View component responsible for displaying the application header.
 *
 * @author seijime
 */
public class HeaderView {

    private final VBox root;

    public HeaderView() {
        Label title = new Label("Astroplanner");
        title.getStyleClass().add("app-title");

        Label subtitle = new Label(
                "Plan astronomical observations by altitude, azimuth and sky conditions"
        );
        subtitle.getStyleClass().add("app-subtitle");

        root = new VBox(4, title, subtitle);
        root.getStyleClass().add("app-header");
    }

    /**
     * Returns the root JavaFX node.
     *
     * @return header root node.
     */
    public Parent getRoot() {
        return root;
    }
}