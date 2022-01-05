package net.vulturif.readerui.fxml;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.vulturif.util.PrefHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public class SettingsController {

    String filePath;
    String newFilePath = "";
    @FXML
    private TextField tfFilepath;
    @FXML
    private Node settingsNode;

    public void setCurrentFilePath(String filePath) {
        this.filePath = filePath;
        if (tfFilepath != null) {
            tfFilepath.setText(filePath);
        }
    }

    @FXML
    public void initialize() {
        tfFilepath.setText(filePath);
    }

    @FXML
    public void choosePath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File(filePath));
        Optional<File> selectedDirectory = Optional.ofNullable(directoryChooser.showDialog(settingsNode.getScene().getWindow()));

        selectedDirectory.ifPresent(file -> newFilePath = file.getAbsolutePath());
    }

    @FXML
    public void actionOk() {
        if (!filePath.equals(newFilePath)) {
            moveCurrentFiles();
            setCurrentFilePath(newFilePath);
            PrefHelper.getInstance().setFilePath(newFilePath);
        }
        actionCancel();
    }

    @FXML
    public void actionCancel() {
        Stage stage = (Stage) settingsNode.getScene().getWindow();
        stage.close();
    }

    private void moveCurrentFiles() {
        File oldPath = new File(filePath);
        Arrays.stream(Objects.requireNonNull(oldPath
                        .listFiles(pathname -> pathname.getName().toLowerCase(Locale.ROOT).endsWith(".csv"))))
                .forEach(file -> {
                    try {
                        Files.move(file.toPath(), Path.of(newFilePath + "//" + file.getName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
