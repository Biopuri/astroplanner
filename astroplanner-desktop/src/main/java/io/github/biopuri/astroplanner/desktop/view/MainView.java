package io.github.biopuri.astroplanner.desktop.view;

import io.github.biopuri.astroplanner.core.domain.CelestialObject;
import io.github.biopuri.astroplanner.core.domain.SkyCondition;
import io.github.biopuri.astroplanner.desktop.controller.MainController;
import io.github.biopuri.astroplanner.desktop.model.ObservationWindowRow;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Function;

/**
 * Main JavaFX view of the Astroplanner desktop application.
 *
 * <p>This class is responsible only for creating and arranging UI controls.
 * Search logic is delegated to {@link MainController}.</p>
 *
 * @author seijime
 */
public class MainView {

    private final MainController controller;

    private final BorderPane root = new BorderPane();

    private final ProgressIndicator progressIndicator = new ProgressIndicator();

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

    private final TableView<ObservationWindowRow> table = new TableView<>();
    private final Label statusLabel = new Label("Ready");

    private boolean updatingTimeZoneItems = false;

    public MainView(MainController controller) {
        this.controller = controller;

        objectBox.setValue(CelestialObject.MOON);
        skyConditionBox.setValue(SkyCondition.ANYTIME);

        timeZoneBox.setEditable(true);
        timeZoneBox.setVisibleRowCount(12);
        configureTimeZoneAutocomplete();

        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(24, 24);

        configureLayout();
    }

    /**
     * Returns the root JavaFX node.
     *
     * @return root UI node.
     */
    public Parent getRoot() {
        return root;
    }

    private void configureLayout() {
        root.setTop(new VBox(
                createHeader(),
                createSearchPanel()
        ));
        root.setCenter(createResultsPanel());
        root.setBottom(createStatusBar());
    }

    private Parent createHeader() {
        Label title = new Label("Astroplanner");
        title.getStyleClass().add("app-title");

        Label subtitle = new Label("Plan astronomical observations by altitude, azimuth and sky condition.");
        subtitle.getStyleClass().add("app-subtitle");

        VBox header = new VBox(4, title, subtitle);
        header.getStyleClass().add("app-header");

        return header;
    }

    /**
     * Creates the search parameters panel.
     *
     * @return search parameters UI panel.
     */
    private Parent createSearchPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);
        grid.setPadding(new Insets(16));

        int row = 0;

        addRow(grid, row, "Object", objectBox, 0);
        addRow(grid, row, "Sky condition", skyConditionBox, 2);
        row++;

        addRow(grid, row, "Latitude", latitudeField, 0);
        addRow(grid, row, "Longitude", longitudeField, 2);
        addRow(grid, row, "Elevation, m", elevationField, 4);
        row++;

        addRow(grid, row, "Time zone", timeZoneBox, 0);
        addRow(grid, row, "Start date", startDatePicker, 2);
        addRow(grid, row, "End date", endDatePicker, 4);
        row++;

        addRow(grid, row, "Min altitude", minAltitudeField, 0);
        addRow(grid, row, "Max altitude", maxAltitudeField, 2);
        addRow(grid, row, "Step, min", stepMinutesField, 4);
        row++;

        addRow(grid, row, "Min azimuth", minAzimuthField, 0);
        addRow(grid, row, "Max azimuth", maxAzimuthField, 2);
        addRow(grid, row, "Min duration, min", minimumDurationField, 4);

        Button searchButton = new Button("Search");
        searchButton.setText("Find observation windows");
        searchButton.getStyleClass().add("search-button");
        searchButton.setPrefWidth(250);
        searchButton.setOnAction(event -> runSearch());

        HBox buttonBox = new HBox(12, searchButton, progressIndicator);
        buttonBox.setPadding(new Insets(0, 16, 16, 16));

        Label title = new Label("Search parameters");
        title.getStyleClass().add("section-title");

        VBox panel = new VBox(4, title, grid, buttonBox);
        panel.getStyleClass().add("search-panel");

        return panel;
    }

    private Parent createResultsPanel() {
        configureTable();

        Label title = new Label("Observation windows");
        title.getStyleClass().add("section-title");

        VBox panel = new VBox(10, title, table);
        panel.getStyleClass().add("results-panel");
        VBox.setVgrow(table, Priority.ALWAYS);

        return panel;
    }

    private Parent createStatusBar() {
        HBox statusBar = new HBox(statusLabel);
        statusBar.setPadding(new Insets(8, 16, 8, 16));
        statusBar.getStyleClass().add("status-bar");
        return statusBar;
    }

    /**
     * Creates a sorted list of available IANA time zones.
     *
     * @return sorted time zone identifiers.
     */
    private List<String> createTimeZoneOptions() {
        return ZoneId.getAvailableZoneIds()
                .stream()
                .sorted()
                .toList();
    }

    /**
     * Configures filtering for the editable time zone selector.
     *
     * <p>The user can either select a value from the drop-down list
     * or type part of a time zone identifier to filter available options.</p>
     */
    private void configureTimeZoneAutocomplete() {
        timeZoneBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (updatingTimeZoneItems) {
                return;
            }

            String typedText = newValue == null ? "" : newValue;

            List<String> filtered = typedText.isBlank()
                    ? timeZoneOptions
                    : timeZoneOptions.stream()
                    .filter(zone -> zone.toLowerCase().contains(typedText.toLowerCase()))
                    .toList();

            updatingTimeZoneItems = true;
            try {
                timeZoneBox.setItems(FXCollections.observableArrayList(filtered));
                timeZoneBox.getEditor().setText(typedText);
                timeZoneBox.getEditor().positionCaret(typedText.length());
            } finally {
                updatingTimeZoneItems = false;
            }

            if (!filtered.isEmpty() && timeZoneBox.isFocused() && !timeZoneBox.isShowing()) {
                timeZoneBox.show();
            }
        });
    }

    /**
     * Adds a labeled control to the search form.
     *
     * @param grid target grid.
     * @param row target row.
     * @param label control label.
     * @param control input control.
     * @param column target column.
     */
    private void addRow(
            GridPane grid,
            int row,
            String label,
            Control control,
            int column
    ) {
        Label labelNode = new Label(label);
        control.setPrefWidth(180);
        control.setMaxWidth(Double.MAX_VALUE);

        grid.add(labelNode, column, row);
        grid.add(control, column + 1, row);

        GridPane.setHgrow(control, Priority.ALWAYS);
    }

    private void configureTable() {
        table.getColumns().clear();

        table.getColumns().add(column("Start", ObservationWindowRow::start));
        table.getColumns().add(column("End", ObservationWindowRow::end));
        table.getColumns().add(column("Duration", ObservationWindowRow::duration));
        table.getColumns().add(column("Object", ObservationWindowRow::object));
        table.getColumns().add(column("Start alt", ObservationWindowRow::startAltitude));
        table.getColumns().add(column("End alt", ObservationWindowRow::endAltitude));
        table.getColumns().add(column("Start az", ObservationWindowRow::startAzimuth));
        table.getColumns().add(column("End az", ObservationWindowRow::endAzimuth));

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private TableColumn<ObservationWindowRow, String> column(
            String title,
            Function<ObservationWindowRow, String> valueExtractor
    ) {
        TableColumn<ObservationWindowRow, String> column = new TableColumn<>(title);

        column.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(valueExtractor.apply(cellData.getValue()))
        );

        return column;
    }

    /**
     * Executes observation window search in a background thread.
     */
    private void runSearch() {
        statusLabel.setText("Searching...");
        progressIndicator.setVisible(true);

        Task<List<ObservationWindowRow>> task = new Task<>() {
            @Override
            protected java.util.List<ObservationWindowRow> call() {
                return controller.search(
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
        };

        task.setOnSucceeded(event -> {
            table.setItems(FXCollections.observableArrayList(task.getValue()));
            statusLabel.setText("✓ Found windows: " + table.getItems().size());
            progressIndicator.setVisible(false);
        });

        task.setOnFailed(event -> {
            progressIndicator.setVisible(false);
            statusLabel.setText("Search failed");

            Throwable exception = task.getException();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Search error");
            alert.setHeaderText("Failed to search observation windows");
            alert.setContentText(exception == null ? "Unknown error" : exception.getMessage());
            alert.showAndWait();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}