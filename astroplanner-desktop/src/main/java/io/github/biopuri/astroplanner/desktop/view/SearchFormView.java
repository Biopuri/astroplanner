package io.github.biopuri.astroplanner.desktop.view;

import io.github.biopuri.astroplanner.core.domain.CelestialObject;
import io.github.biopuri.astroplanner.core.domain.SkyCondition;
import io.github.biopuri.astroplanner.desktop.model.SearchFormData;
import io.github.biopuri.astroplanner.desktop.validation.SearchField;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * View component responsible for displaying and managing
 * observation search form controls.
 *
 * @author seijime
 */
public class SearchFormView {

    private final ComboBox<CelestialObject> objectBox =
            new ComboBox<>(FXCollections.observableArrayList(CelestialObject.values()));

    private final TextField latitudeField = new TextField();
    private final TextField longitudeField = new TextField();
    private final TextField elevationField = new TextField();

    private final List<String> timeZoneOptions = createTimeZoneOptions();

    private final ComboBox<String> timeZoneBox =
            new ComboBox<>(FXCollections.observableArrayList(timeZoneOptions));

    private final DatePicker startDatePicker = new DatePicker(LocalDate.now());
    private final DatePicker endDatePicker = new DatePicker(LocalDate.now().plusDays(30));

    private final TextField minAltitudeField = new TextField();
    private final TextField maxAltitudeField = new TextField();
    private final TextField minAzimuthField = new TextField();
    private final TextField maxAzimuthField = new TextField();

    private final ComboBox<SkyCondition> skyConditionBox =
            new ComboBox<>(FXCollections.observableArrayList(SkyCondition.values()));

    private final TextField stepMinutesField = new TextField();
    private final TextField minimumDurationField = new TextField();

    private final Button searchButton = new Button("Find observation windows");

    private final Map<SearchField, Control> validationControls =
            new EnumMap<>(SearchField.class);

    private final Map<SearchField, Label> validationLabels =
            new EnumMap<>(SearchField.class);

    private final VBox root;

    public SearchFormView() {
        objectBox.setValue(CelestialObject.MOON);
        skyConditionBox.setValue(SkyCondition.ANYTIME);

        timeZoneBox.setEditable(true);
        timeZoneBox.setVisibleRowCount(12);
        configureTimeZoneAutocomplete();

        registerValidationControls();

        root = createRoot();
    }

    /**
     * Returns the root JavaFX node.
     *
     * @return search form root node.
     */
    public Parent getRoot() {
        return root;
    }

    /**
     * Returns data currently entered in the search form.
     *
     * @return search form data.
     */
    public SearchFormData getData() {
        return new SearchFormData(
                objectBox.getValue(),
                latitudeField.getText(),
                longitudeField.getText(),
                elevationField.getText(),
                timeZoneBox.getEditor().getText(),
                startDatePicker.getValue(),
                endDatePicker.getValue(),
                minAltitudeField.getText(),
                maxAltitudeField.getText(),
                minAzimuthField.getText(),
                maxAzimuthField.getText(),
                skyConditionBox.getValue(),
                stepMinutesField.getText(),
                minimumDurationField.getText()
        );
    }

    /**
     * Sets action executed when the search button is pressed.
     *
     * @param action action to execute.
     */
    public void setOnSearch(Runnable action) {
        searchButton.setOnAction(event -> action.run());
    }

    /**
     * Enables or disables the search button.
     *
     * @param enabled whether the button should be enabled.
     */
    public void setSearchEnabled(boolean enabled) {
        searchButton.setDisable(!enabled);
    }

    /**
     * Removes validation highlighting and messages from all fields.
     */
    public void clearValidationErrors() {
        validationControls.values().forEach(control ->
                control.getStyleClass().remove("input-error")
        );

        validationLabels.values().forEach(label -> {
            label.setText("");
            label.setVisible(false);
            label.setManaged(false);
        });
    }

    /**
     * Shows validation error for a specific field.
     *
     * @param field invalid field.
     * @param message validation message.
     */
    public void showValidationError(
            SearchField field,
            String message
    ) {
        Control control = validationControls.get(field);
        Label label = validationLabels.get(field);

        if (control != null && !control.getStyleClass().contains("input-error")) {
            control.getStyleClass().add("input-error");
        }

        if (label != null) {
            label.setText(message);
            label.setVisible(true);
            label.setManaged(true);
        }
    }

    private VBox createRoot() {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);
        grid.setPadding(new Insets(16));

        int row = 0;

        addRow(grid, row, "Object", objectBox, 0, true);
        addRow(grid, row, "Sky condition", skyConditionBox, 2, true);
        row++;

        addRow(grid, row, "Latitude", latitudeField, 0, true);
        addRow(grid, row, "Longitude", longitudeField, 2, true);
        addRow(grid, row, "Elevation, m", elevationField, 4, false);
        row++;

        addRow(grid, row, "Time zone", timeZoneBox, 0, true);
        addRow(grid, row, "Start date", startDatePicker, 2, true);
        addRow(grid, row, "End date", endDatePicker, 4, true);
        row++;

        addRow(grid, row, "Min altitude", minAltitudeField, 0, false);
        addRow(grid, row, "Max altitude", maxAltitudeField, 2, false);
        row++;

        addRow(grid, row, "Min azimuth", minAzimuthField, 0, false);
        addRow(grid, row, "Max azimuth", maxAzimuthField, 2, false);
        row++;

        addRow(grid, row, "Step, min", stepMinutesField, 0, false);
        addRow(grid, row, "Min duration, min", minimumDurationField, 2, false);

        searchButton.getStyleClass().add("search-button");
        searchButton.setPrefWidth(250);

        HBox buttonBox = new HBox(searchButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(8, 16, 16, 16));

        Label title = new Label("Search parameters");
        title.getStyleClass().add("section-title");

        VBox panel = new VBox(4, title, grid, buttonBox);
        panel.getStyleClass().add("search-panel");

        return panel;
    }

    private void registerValidationControls() {
        validationControls.put(SearchField.LATITUDE, latitudeField);
        validationControls.put(SearchField.LONGITUDE, longitudeField);
        validationControls.put(SearchField.ELEVATION, elevationField);

        validationControls.put(SearchField.TIME_ZONE, timeZoneBox);

        validationControls.put(SearchField.START_DATE, startDatePicker);
        validationControls.put(SearchField.END_DATE, endDatePicker);

        validationControls.put(SearchField.MIN_ALTITUDE, minAltitudeField);
        validationControls.put(SearchField.MAX_ALTITUDE, maxAltitudeField);

        validationControls.put(SearchField.MIN_AZIMUTH, minAzimuthField);
        validationControls.put(SearchField.MAX_AZIMUTH, maxAzimuthField);

        validationControls.put(SearchField.STEP, stepMinutesField);
        validationControls.put(SearchField.MIN_DURATION, minimumDurationField);
    }

    private void addRow(
            GridPane grid,
            int row,
            String label,
            Control control,
            int column,
            boolean required
    ) {
        Parent labelNode = createLabel(label, required);

        control.setPrefWidth(180);
        control.setMaxWidth(Double.MAX_VALUE);

        Label validationLabel = new Label();
        validationLabel.getStyleClass().add("validation-message");
        validationLabel.setVisible(false);
        validationLabel.setManaged(false);

        SearchField searchField = findSearchField(control);
        if (searchField != null) {
            validationLabels.put(searchField, validationLabel);
        }

        VBox controlBox = new VBox(3, control, validationLabel);

        grid.add(labelNode, column, row);
        grid.add(controlBox, column + 1, row);

        GridPane.setHgrow(controlBox, Priority.ALWAYS);
    }

    private SearchField findSearchField(Control control) {
        return validationControls.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == control)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private List<String> createTimeZoneOptions() {
        return ZoneId.getAvailableZoneIds()
                .stream()
                .sorted()
                .toList();
    }

    private void configureTimeZoneAutocomplete() {
        final boolean[] updating = {false};

        timeZoneBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (updating[0]) {
                return;
            }

            String typedText = newValue == null ? "" : newValue;

            List<String> filtered = typedText.isBlank()
                    ? timeZoneOptions
                    : timeZoneOptions.stream()
                    .filter(zone -> zone.toLowerCase().contains(typedText.toLowerCase()))
                    .toList();

            updating[0] = true;
            try {
                timeZoneBox.setItems(FXCollections.observableArrayList(filtered));
                timeZoneBox.getEditor().setText(typedText);
                timeZoneBox.getEditor().positionCaret(typedText.length());
            } finally {
                updating[0] = false;
            }

            if (!filtered.isEmpty() && timeZoneBox.isFocused() && !timeZoneBox.isShowing()) {
                timeZoneBox.show();
            }
        });
    }

    private Parent createLabel(
            String text,
            boolean required
    ) {
        if (!required) {
            return new Label(text);
        }

        Label textLabel = new Label(text);

        Label requiredLabel = new Label(" *");
        requiredLabel.getStyleClass().add("required-mark");

        HBox box = new HBox(textLabel, requiredLabel);

        return box;
    }
}