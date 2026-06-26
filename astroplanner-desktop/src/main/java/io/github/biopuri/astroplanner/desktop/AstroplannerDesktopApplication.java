package io.github.biopuri.astroplanner.desktop;

import io.github.biopuri.astroplanner.core.domain.*;
import io.github.biopuri.astroplanner.desktop.controller.MainController;
import io.github.biopuri.astroplanner.desktop.view.MainView;
import io.github.biopuri.astroplanner.ephemeris.orekit.OrekitDataInitializer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.nio.file.Path;

/**
 * Entry point for the Astroplanner desktop application.
 *
 * @author seijime
 */
public class AstroplannerDesktopApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        OrekitDataInitializer.initialize(Path.of("../orekit-data"));

        MainController controller = new MainController();
        MainView mainView = new MainView(controller);

        Scene scene = new Scene(mainView.getRoot(), 1600, 900);

        scene.getStylesheets().add(
                getClass().getResource("/css/application.css").toExternalForm()
        );

        stage.setTitle("Astroplanner");
        stage.setMinWidth(1300);
        stage.setMinHeight(800);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
