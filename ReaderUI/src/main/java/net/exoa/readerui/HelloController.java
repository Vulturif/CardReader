package net.exoa.readerui;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import net.exoa.logic.Input;
import net.exoa.logic.CardReader;
import net.exoa.logic.Output;
import net.exoa.logic.Person;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import  java.util.prefs.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class HelloController {

    public static final String SEPPERATOR = ";";
    @FXML
    private Label lblStatus;
    @FXML
    private TextField tfVorname;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfGeburtsdatum;
    @FXML
    private TextField tfPlz;
    @FXML
    private TextField tfOrt;
    @FXML
    private TextField tfStrasse;
    @FXML
    private TextField tfHausnummer;
    @FXML
    private ChoiceBox<String> cbGeschlecht;
    @FXML
    private ChoiceBox<CardTerminal> cbReader;
    @FXML
    private ChoiceBox<String> cbGenesen;
    @FXML
    private ChoiceBox<String> cbJUJ;
    @FXML
    private DatePicker dpImpfdatum;
    @FXML
    private ChoiceBox<String> cbImpfserie;
    @FXML
    private ChoiceBox<String> cbBriefkontakt;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfTelefon;
    @FXML
    private TextField tfAdresszusatz;
    @FXML
    private TextField tfCharge;
    @FXML
    private CheckBox cbAutoSave;
    @FXML
    private Button btnSave;
    @FXML
    private TableView<PersonTableData> tvCurrent;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c1;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c2;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c3;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c4;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c5;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c6;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c7;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c8;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c9;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c10;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c11;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c12;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c13;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c14;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c15;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c16;
    @FXML
    private TableColumn<PersonTableData, SimpleStringProperty> c17;

    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private final String PREF_NAME = "path_to_csv";

    CardReader reader = new CardReader();
    Output out = new Output();
    Input in = new Input();

    File file = new File("D:\\tmp\\bla.csv");

    List<CardTerminal> cardTerminals;
    CardTerminal cardTerminal;
    TimerTask task;

    Preferences prefs = Preferences.userNodeForPackage(net.exoa.readerui.HelloController.class);

    private final ObservableList<PersonTableData> personTableData = FXCollections.observableArrayList();

    boolean reRead = true;

    public HelloController() throws CardException {
        buildTask();
        cardTerminals = reader.listTerminals();
        cardTerminal = cardTerminals.get(0);

        String filePath = prefs.get(PREF_NAME, "C:\\tmp");
        File bla = new File(filePath);
        if (!bla.exists()) {
            bla.mkdir();
        }

        LocalDate current_date = LocalDate.now();
        file = new File(filePath + String.format("\\Impfung_%d%02d%02d_HHMM.csv", current_date.getYear()-2000, current_date.getMonthValue(), current_date.getDayOfMonth()));
    }

    private void buildTask() {
        task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        boolean isCardPresent = cbReader.getValue().isCardPresent();

                        if (reRead) {
                            setStatus(isCardPresent);
                        }

                        if (isCardPresent) {
                            if (reRead) {
                                try {
                                    readCard();
                                } catch (Exception e) {
                                    lblStatus.setText("Fehler beim Lesen der Karte!");
                                    lblStatus.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10.0), new Insets(-5.0))));
                                }
                            }
                            reRead = false;
                        } else {
                            reRead = true;
                            if (cbAutoSave.isSelected()) {
                                write();
                                clear();
                            }
                        }
                    } catch (CardException | IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        };

    }

    private void clear() {
        cbGeschlecht.setValue(null);
        tfVorname.setText(null);
        tfName.setText(null);
        tfGeburtsdatum.setText(null);
        tfPlz.setText(null);
        tfOrt.setText(null);
        tfStrasse.setText(null);
        tfHausnummer.setText(null);
    }

    private void readCard() {
        Person currentPerson = reader.readCardInTerminal(cardTerminal);

        cbGeschlecht.setValue(currentPerson.getGeschlecht());
        tfVorname.setText(currentPerson.getVorname());
        tfName.setText(currentPerson.getName());
        tfGeburtsdatum.setText(reanrangeDate(currentPerson.getGeburtsdatum()));
        tfPlz.setText(currentPerson.getPlz());
        tfOrt.setText(currentPerson.getOrt());
        tfStrasse.setText(currentPerson.getStrasse());
        tfHausnummer.setText(currentPerson.getHausnummer());
    }

    @FXML
    public void initialize() {
        cbReader.setValue(cardTerminals.get(0));
        cardTerminals.forEach(terminal -> cbReader.getItems().add(terminal));

        cbGeschlecht.getItems().add("");
        cbGeschlecht.getItems().add("M");
        cbGeschlecht.getItems().add("W");
        cbGeschlecht.getItems().add("D");

        cbBriefkontakt.getItems().add("");
        cbBriefkontakt.getItems().add("0");
        cbBriefkontakt.getItems().add("1");

        cbImpfserie.getItems().add("");
        cbImpfserie.getItems().add("1");
        cbImpfserie.getItems().add("2");
        cbImpfserie.getItems().add("3");

        cbJUJ.getItems().add("");
        cbJUJ.getItems().add("0");
        cbJUJ.getItems().add("1");

        cbGenesen.getItems().add("");
        cbGenesen.getItems().add("0");
        cbGenesen.getItems().add("1");

        executor.scheduleAtFixedRate(task, 3000, 500, TimeUnit.MILLISECONDS);

        c1.setCellValueFactory(new PropertyValueFactory<>("Anrede"));
        c2.setCellValueFactory(new PropertyValueFactory<>("Vorname"));
        c3.setCellValueFactory(new PropertyValueFactory<>("Nachname"));
        c4.setCellValueFactory(new PropertyValueFactory<>("Geburtsdatum"));
        c5.setCellValueFactory(new PropertyValueFactory<>("Plz"));
        c6.setCellValueFactory(new PropertyValueFactory<>("Ort"));
        c7.setCellValueFactory(new PropertyValueFactory<>("Strasse"));
        c8.setCellValueFactory(new PropertyValueFactory<>("StrasseNr"));
        c9.setCellValueFactory(new PropertyValueFactory<>("Adresszusatz"));
        c10.setCellValueFactory(new PropertyValueFactory<>("Telefon"));
        c11.setCellValueFactory(new PropertyValueFactory<>("Email"));
        c12.setCellValueFactory(new PropertyValueFactory<>("Briefkontakt"));
        c13.setCellValueFactory(new PropertyValueFactory<>("Impfserie"));
        c14.setCellValueFactory(new PropertyValueFactory<>("Charge"));
        c15.setCellValueFactory(new PropertyValueFactory<>("Impfdatum"));
        c16.setCellValueFactory(new PropertyValueFactory<>("ErstimpfungJuJ"));
        c17.setCellValueFactory(new PropertyValueFactory<>("Genesenen_Bescheinigung"));
        tvCurrent.setItems(personTableData);

        readCSV();
    }

    private void readCSV() {
        List<String[]> lines = in.readData(file);
        lines.forEach(line -> personTableData.add(new PersonTableData(line)));
    }


    private void setStatus(boolean b) {
        if (b) {
            lblStatus.setText("Karte gefunden!");
            lblStatus.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10.0), new Insets(-5.0))));
        } else {
            lblStatus.setText("Keine Karte gefunden!");
            lblStatus.setBackground(new Background(new BackgroundFill(Color.YELLOW, new CornerRadii(10.0), new Insets(-5.0))));
        }
    }

    @FXML
    public void writePerson() throws IOException {
        write();
    }

    private void write() throws IOException {
        String newLine = cbGeschlecht.getValue() + SEPPERATOR
                + tfVorname.getText() + SEPPERATOR
                + tfName.getText() + SEPPERATOR
                + tfGeburtsdatum.getText() + SEPPERATOR
                + tfPlz.getText() + SEPPERATOR
                + tfOrt.getText() + SEPPERATOR
                + tfStrasse.getText() + SEPPERATOR
                + tfHausnummer.getText() + SEPPERATOR
                + tfAdresszusatz.getText() + SEPPERATOR
                + tfTelefon.getText() + SEPPERATOR
                + tfEmail.getText() + SEPPERATOR
                + cbBriefkontakt.getValue() + SEPPERATOR
                + cbImpfserie.getValue() + SEPPERATOR
                + tfCharge.getText() + SEPPERATOR
                + dpImpfdatum.getValue() + SEPPERATOR
                + cbJUJ.getValue() + SEPPERATOR
                + cbGenesen.getValue() + "\n";

        newLine = newLine.replaceAll("null", "");
        out.write(file, newLine);
        personTableData.add(new PersonTableData(newLine.split(";", -1)));
    }

    private String reanrangeDate(String date) {
        String[] split = date.split("");
        return split[6] + split[7] + "." + split[4] + split[5] + "." + split[0] + split[1] + split[2] + split[3];
    }

    @FXML
    public void checkAutoSave() {
        btnSave.setDisable(cbAutoSave.isSelected());
    }

    @FXML
    public void close() {
        Platform.exit();
    }

    @FXML
    public void openSettings() {

    }
}