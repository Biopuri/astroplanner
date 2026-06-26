package io.github.biopuri.astroplanner.desktop.view;

import io.github.biopuri.astroplanner.core.domain.moon.MoonInfo;
import io.github.biopuri.astroplanner.desktop.model.ObservationWindowRow;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * View component responsible for displaying observation search results.
 *
 * @author seijime
 */
public class ResultsTableView {

    private final TableView<ObservationWindowRow> table = new TableView<>();
    private final ProgressIndicator progressIndicator =
            new ProgressIndicator();
    private final StackPane tableContainer =
            new StackPane();
    private final MoonInfoPanel moonInfoPanel = new MoonInfoPanel();
    private final VBox root;

    public ResultsTableView() {
        configureTable();

        Label title = new Label("Observation windows");
        title.getStyleClass().add("section-title");

        progressIndicator.setVisible(false);
        progressIndicator.setMaxSize(48, 48);

        tableContainer.getChildren().addAll(table, progressIndicator);

        VBox.setVgrow(tableContainer, Priority.ALWAYS);

        root = new VBox(10, title, tableContainer, moonInfoPanel);

        VBox.setVgrow(table, Priority.ALWAYS);
    }

    /**
     * Returns the root JavaFX node.
     *
     * @return result table root node.
     */
    public Parent getRoot() {
        return root;
    }

    /**
     * Replaces table items with the given rows.
     *
     * @param rows rows to display.
     */
    public void setRows(List<ObservationWindowRow> rows) {
        table.setItems(FXCollections.observableArrayList(rows));
    }

    /**
     * Returns current number of displayed rows.
     *
     * @return displayed row count.
     */
    public int getRowCount() {
        return table.getItems().size();
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
            Function<ObservationWindowRow, String> mapper
    ) {
        TableColumn<ObservationWindowRow, String> column = new TableColumn<>(title);

        column.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(mapper.apply(cell.getValue()))
        );

        return column;
    }

    /**
     * Sets action executed when selected result row changes.
     *
     * @param action action to execute for selected row.
     */
    public void setOnSelectedRowChanged(Consumer<ObservationWindowRow> action) {
        table.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        action.accept(newValue);
                    }
                });
    }

    /**
     * Selects the first result row if table is not empty.
     */
    public void selectFirstRow() {
        if (!table.getItems().isEmpty()) {
            table.getSelectionModel().selectFirst();
        }
    }

    /**
     * Shows or hides the loading indicator and disables the table during search.
     *
     * @param searching whether search is currently running.
     */
    public void setSearching(boolean searching) {
        progressIndicator.setVisible(searching);
        table.setDisable(searching);
    }

    /**
     * Shows calculated Moon information below the results table.
     *
     * @param moonInfo calculated Moon information.
     */
    public void showMoonInfo(MoonInfo moonInfo) {
        moonInfoPanel.update(moonInfo);
    }

    /**
     * Hides Moon information block.
     */
    public void hideMoonInfo() {
        moonInfoPanel.hidePanel();
    }
}