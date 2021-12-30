package net.exoa.readerui.fxml;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AboutController {

    @FXML
    private Button closeButton;

    private HostServices service;

    @FXML
    public void openWebsite() {
        service.showDocument("https://github.com/Vulturif/CardReader");
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void setHostService(HostServices service) {
        this.service = service;
    }
}
