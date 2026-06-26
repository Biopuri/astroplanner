package io.github.biopuri.astroplanner.desktop.view;

import io.github.biopuri.astroplanner.core.domain.moon.MoonInfo;
import io.github.biopuri.astroplanner.desktop.util.MoonPhaseIconResolver;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Objects;

/**
 * Displays additional Moon information for Moon observation results.
 *
 * @author seijime
 */
public class MoonInfoPanel extends GridPane {

    private final Label phaseValue = new Label("-");
    private final Label illuminationValue = new Label("-");
    private final Label ageValue = new Label("-");
    private final ImageView phaseImage = new ImageView();

    /**
     * Creates Moon information panel.
     */
    public MoonInfoPanel() {
        configureLayout();
        addContent();
        setVisible(false);
        setManaged(false);
    }

    /**
     * Updates displayed Moon information.
     *
     * @param moonInfo calculated Moon information
     */
    public void update(MoonInfo moonInfo) {
        phaseValue.setText(moonInfo.phase().getDisplayName());
        illuminationValue.setText(String.format("%.1f %%", moonInfo.illumination()));
        ageValue.setText(String.format("%.1f days", moonInfo.ageDays()));

        phaseImage.setImage(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream(
                        MoonPhaseIconResolver.resolve(moonInfo.phase())
                ))
        ));

        setVisible(true);
        setManaged(true);
    }

    /**
     * Hides Moon information panel.
     */
    public void hidePanel() {
        setVisible(false);
        setManaged(false);
    }

    private void configureLayout() {
        setHgap(12);
        setVgap(8);
        setPadding(new Insets(12));
        phaseImage.setFitWidth(60);
        phaseImage.setFitHeight(60);
        phaseImage.setPreserveRatio(true);
        phaseImage.setSmooth(true);
    }

    private void addContent() {
        Label title = new Label("Moon information");

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(12);
        infoGrid.setVgap(8);

        infoGrid.add(new Label("Phase:"), 0, 0);
        infoGrid.add(phaseValue, 1, 0);

        infoGrid.add(new Label("Illumination:"), 0, 1);
        infoGrid.add(illuminationValue, 1, 1);

        infoGrid.add(new Label("Age:"), 0, 2);
        infoGrid.add(ageValue, 1, 2);

        HBox content = new HBox(16, phaseImage, infoGrid);

        add(title, 0, 0);
        add(content, 0, 1);
    }
}