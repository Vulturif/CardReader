package net.exoa.readerui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CardReaderUi.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setMinHeight(700);
            stage.setMinWidth(900);
            stage.setTitle("CardReader");
            stage.getIcons().add(new Image(Objects.requireNonNull(Launcher.class.getClassLoader().getResourceAsStream("malteser.png"))));
            stage.setScene(scene);
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