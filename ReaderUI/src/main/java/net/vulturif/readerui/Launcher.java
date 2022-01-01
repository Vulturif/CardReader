package net.vulturif.readerui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.vulturif.readerui.fxml.CardReaderUiController;
import net.vulturif.readerui.util.PrefHelper;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/CardReaderUi.fxml"));
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

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }
}