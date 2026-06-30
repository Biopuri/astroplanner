package io.github.biopuri.astroplanner.desktop.view;

import io.github.biopuri.astroplanner.core.domain.CelestialObject;
import io.github.biopuri.astroplanner.core.domain.moon.MoonInfo;
import io.github.biopuri.astroplanner.core.search.ApproximateMoonInfoCalculator;
import io.github.biopuri.astroplanner.core.service.MoonInfoCalculator;
import io.github.biopuri.astroplanner.desktop.controller.MainController;
import io.github.biopuri.astroplanner.desktop.model.ObservationWindowRow;
import io.github.biopuri.astroplanner.desktop.validation.InputValidationException;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.time.ZoneId;
import java.util.List;

/**
 * Main JavaFX view of the Astroplanner desktop application.
 *
 * <p>This class coordinates independent view components and delegates
 * observation search logic to {@link MainController}.</p>
 *
 * @author seijime
 */
public class MainView {

    private final MainController controller;

    private final HeaderView headerView = new HeaderView();
    private final SearchFormView searchFormView = new SearchFormView();
    private final ResultsTableView resultsTableView = new ResultsTableView();
    private final StatusBarView statusBarView = new StatusBarView();
    private final MoonInfoCalculator moonInfoCalculator =
            new ApproximateMoonInfoCalculator();

    private final BorderPane root = new BorderPane();

    public MainView(MainController controller) {
        this.controller = controller;

        configureLayout();
        configureActions();
    }

    /**
     * Returns the root JavaFX node.
     *
     * @return root UI node.
     */
    public Parent getRoot() {
        return root;
    }

    /**
     * Configures the main application layout.
     */
    private void configureLayout() {
        root.setTop(new VBox(
                headerView.getRoot(),
                searchFormView.getRoot()
        ));
        root.setCenter(resultsTableView.getRoot());
        root.setBottom(statusBarView.getRoot());
    }

    /**
     * Configures user actions.
     */
    private void configureActions() {
        searchFormView.setOnSearch(this::runSearch);
        resultsTableView.setOnSelectedRowChanged(this::updateMoonInfo);
    }

    /**
     * Executes observation window search in a background thread.
     */
    private void runSearch() {
        searchFormView.clearValidationErrors();
        searchFormView.setSearchEnabled(false);
        resultsTableView.setSearching(true);
        statusBarView.setMessage("Searching observation windows...");

        Task<List<ObservationWindowRow>> task = new Task<>() {
            @Override
            protected List<ObservationWindowRow> call() {
                return controller.search(searchFormView.getData());
            }
        };

        task.setOnSucceeded(event -> {
            resultsTableView.setRows(task.getValue());

            /*
             * Moon information is shown only when the selected table row
             * represents the Moon. In all-objects mode the first result
             * may belong to any celestial object.
             */
            resultsTableView.hideMoonInfo();

            searchFormView.setSearchEnabled(true);
            resultsTableView.setSearching(false);
            statusBarView.setMessage("Found windows: " + resultsTableView.getRowCount());
        });

        task.setOnFailed(event -> {
            searchFormView.setSearchEnabled(false);

            Throwable exception = task.getException();

            if (exception instanceof InputValidationException validationException) {
                validationException.getFields().forEach(field ->
                        searchFormView.showValidationError(
                                field,
                                validationException.getMessage()
                        )
                );

                statusBarView.setMessage(validationException.getMessage());
                return;
            }

            statusBarView.setMessage("Search failed");

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Search error");
            alert.setHeaderText("Failed to search observation windows");
            alert.setContentText(
                    exception == null
                            ? "Unknown error."
                            : exception.getMessage()
            );
            alert.showAndWait();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Shows Moon information for the selected observation window.
     *
     * <p>In all-objects mode the selected object is determined
     * from the table row, not from the search form.</p>
     *
     * @param row selected observation window row.
     */
    private void updateMoonInfo(ObservationWindowRow row) {
        if (row == null || !CelestialObject.MOON.name().equals(row.object())) {
            resultsTableView.hideMoonInfo();
            return;
        }

        ZoneId zoneId = ZoneId.of(searchFormView.getData().timeZone());

        MoonInfo moonInfo = moonInfoCalculator.calculate(
                row.startDateTime().atZone(zoneId)
        );

        resultsTableView.showMoonInfo(moonInfo);
    }
}