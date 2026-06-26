package io.github.biopuri.astroplanner.desktop.view;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * View component responsible for displaying application status messages.
 *
 * @author seijime
 */
public class StatusBarView {

    private final Label statusLabel = new Label("Ready");
    private final HBox root = new HBox(statusLabel);

    public StatusBarView() {
        root.getStyleClass().add("status-bar");
    }

    /**
     * Returns the root JavaFX node.
     *
     * @return status bar root node.
     */
    public Parent getRoot() {
        return root;
    }

    /**
     * Updates status bar text.
     *
     * @param message status message.
     */
    public void setMessage(String message) {
        statusLabel.setText(message);
    }
}