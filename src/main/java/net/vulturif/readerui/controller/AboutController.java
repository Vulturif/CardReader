package net.vulturif.readerui.controller;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import net.vulturif.util.PrefHelper;

public class AboutController {

    @FXML
    private Button closeButton;
    @FXML
    private Label version;

    private HostServices service;


    @FXML
    private void initialize() {
        version.setText(PrefHelper.getInstance().getVersion());
    }

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
