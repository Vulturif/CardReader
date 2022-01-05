package net.vulturif.readerui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import net.vulturif.readerui.controller.CardReaderUiController;
import net.vulturif.util.PrefHelper;
import net.vulturif.util.Updater;

import java.util.Optional;


public class Launcher extends Application {

    Updater update;

    @Override
    public void start(Stage stage) {

        if (PrefHelper.getInstance().checkForUpdates()) {
            update = new Updater();
            if (update.checkForUpdate()) {
                showUpdateDialog();
            }
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("controller/CardReaderUi.controller"));
            Scene scene = new Scene(fxmlLoader.load(), 950, 700);
            stage.setMinHeight(750);
            stage.setMinWidth(1000);
            stage.setTitle("CardReader");
            stage.getIcons().add(PrefHelper.getInstance().getIcon());
            stage.setScene(scene);
            ((CardReaderUiController) fxmlLoader.getController()).setHostService(getHostServices());
            stage.setOnHidden(e -> ((CardReaderUiController) fxmlLoader.getController()).shutdown());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showUpdateDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CardReader");
        alert.setHeaderText("Neue Version verfügbar!");
        alert.setContentText("Jetzt herunterladen und intallieren?");

        ButtonType never = new ButtonType("Nicht mehr anzeigen");
        ButtonType yes = new ButtonType("Ja");
        ButtonType no = new ButtonType("Später", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(never, yes, no);

        Optional<ButtonType> dialogResult = alert.showAndWait();
        if (dialogResult.isPresent()) {
            if (dialogResult.get() == never) {
                PrefHelper.getInstance().setCheckForUpdates(false);
            } else if (dialogResult.get() == yes) {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setTitle("Update wird heruntergeladen");
                alert2.setHeaderText("");
                alert2.setContentText("Update wird heruntergeladen, bitte warten!");
                alert2.show();

                update.download();
            }
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }

}