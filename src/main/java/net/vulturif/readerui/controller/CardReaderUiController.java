package net.vulturif.readerui.controller;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.vulturif.logic.CardReader;
import net.vulturif.logic.Input;
import net.vulturif.logic.Output;
import net.vulturif.readerui.PersonTableData;
import net.vulturif.shema.egk.Person;
import net.vulturif.util.CellValueFactoryHelper;
import net.vulturif.util.PrefHelper;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static net.vulturif.logic.Constants.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class CardReaderUiController {

    @FXML
    private Label lblStatus;
    @FXML
    private TextField tfFirstname;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfBirthDate;
    @FXML
    private TextField tfPlz;
    @FXML
    private TextField tfOrt;
    @FXML
    private TextField tfStreet;
    @FXML
    private TextField tfHouseNumber;
    @FXML
    private ComboBox<String> cbGender;
    @FXML
    private ChoiceBox<CardTerminal> cbReader;
    @FXML
    private ComboBox<String> cbCured;
    @FXML
    private ComboBox<String> cbJUJ;
    @FXML
    private DatePicker dpVaccinationDate;
    @FXML
    private ComboBox<String> cbVaccinationNumber;
    @FXML
    private ComboBox<String> cbPostalContact;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfPhoneNumber;
    @FXML
    private TextField tfStreetAddition;
    @FXML
    private TextField tfCharge;
    @FXML
    private CheckBox cbAutoSave;
    @FXML
    private Button btnSave;
    @FXML
    private TableView<PersonTableData> tvCurrent;
    @FXML
    private TextField tfTime;
    @FXML
    private ToggleGroup vaccine;
    @FXML
    private TitledPane vacBox;
    PieChart.Data genderM = new PieChart.Data("M", 0);
    @FXML
    private TableColumn<PersonTableData, String> c1;
    @FXML
    private TableColumn<PersonTableData, String> c2;
    @FXML
    private TableColumn<PersonTableData, String> c3;
    @FXML
    private TableColumn<PersonTableData, String> c4;
    @FXML
    private TableColumn<PersonTableData, String> c5;
    @FXML
    private TableColumn<PersonTableData, String> c6;
    @FXML
    private TableColumn<PersonTableData, String> c7;
    @FXML
    private TableColumn<PersonTableData, String> c8;
    @FXML
    private TableColumn<PersonTableData, String> c9;
    @FXML
    private TableColumn<PersonTableData, String> c10;
    @FXML
    private TableColumn<PersonTableData, String> c11;
    @FXML
    private TableColumn<PersonTableData, String> c12;
    @FXML
    private TableColumn<PersonTableData, String> c13;
    @FXML
    private TableColumn<PersonTableData, String> c14;
    @FXML
    private TableColumn<PersonTableData, String> c15;
    @FXML
    private TableColumn<PersonTableData, String> c16;
    @FXML
    private TableColumn<PersonTableData, String> c17;


    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    private final CardReader reader = new CardReader();
    private final Output out = new Output();
    private final Input in = new Input();
    private final HashMap<String, File> files = new HashMap<>();
    private String filePath;

    private List<CardTerminal> cardTerminals;
    private CardTerminal cardTerminal;
    private TimerTask task;
    private TimerTask saveTask;



    Border baseBorder;

    private final ObservableList<PersonTableData> personTableData = FXCollections.observableArrayList();
    private boolean reRead = true;
    PieChart.Data genderW = new PieChart.Data("W", 0);
    PieChart.Data genderD = new PieChart.Data("D", 0);
    @FXML
    private PieChart pieGender;

    public CardReaderUiController() {
        buildTask();
        cardTerminals = reader.listTerminals();
        if (!cardTerminals.isEmpty()) {
            cardTerminal = cardTerminals.get(0);
        }

        filePath = PrefHelper.getInstance().getFilePath();
    }

    private void buildTask() {
        task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        if (cbReader.getValue() == null) {
                            cardTerminals = reader.listTerminals();
                            if (!cardTerminals.isEmpty()) {
                                cbReader.setValue(cardTerminals.get(0));
                            } else {
                                lblStatus.setText(NO_CARD_READER);
                                lblStatus.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10.0), new Insets(-5.0))));
                                return;
                            }
                        }
                        boolean isCardPresent = cbReader.getValue().isCardPresent();

                        if (reRead) {
                            setStatus(isCardPresent);
                        }

                        if (isCardPresent) {
                            if (reRead) {
                                try {
                                    readCard();
                                } catch (Exception e) {
                                    lblStatus.setText(ERROR_READING_CARD);
                                    lblStatus.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(10.0), new Insets(-5.0))));
                                }
                            }
                            reRead = false;
                        } else {
                            reRead = true;
                            if (cbAutoSave.isSelected()) {
                                write();
                                cleanUp();
                            }
                        }
                    } catch (CardException | IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        };

        saveTask = new TimerTask() {
            @Override
            public void run() {
                saveData();
            }
        };
    }

    @FXML
    public void cleanUp() {
        cbGender.setValue(null);
        tfFirstname.setText(null);
        tfName.setText(null);
        tfBirthDate.setText(null);
        tfPlz.setText(null);
        tfOrt.setText(null);
        tfStreet.setText(null);
        tfHouseNumber.setText(null);
        tfStreetAddition.setText(null);
        tfPhoneNumber.setText(null);
        tfEmail.setText(null);
        cbPostalContact.setValue("1");
        cbVaccinationNumber.setValue("");
        tfCharge.setText(null);
        dpVaccinationDate.setValue(null);
        tfTime.setText(null);
        cbJUJ.setValue("0");
        cbCured.setValue("0");
        vaccine.selectToggle(null);
    }

    private void readCard() {
        Person currentPerson = reader.readCardInTerminal(cbReader.getValue());

        if (currentPerson == null) {
            return;
        }


        cbGender.setValue(currentPerson.getGeschlecht());
        tfFirstname.setText(currentPerson.getVorname());
        tfName.setText(currentPerson.getNachname());
        tfBirthDate.setText(rearrangeDate(currentPerson.getGeburtsdatum()));
        tfPlz.setText(currentPerson.getStrassenAdresse().getPostleitzahl());
        tfOrt.setText(currentPerson.getStrassenAdresse().getOrt());
        tfStreet.setText(currentPerson.getStrassenAdresse().getStrasse());
        tfHouseNumber.setText(currentPerson.getStrassenAdresse().getHausnummer());
    }

    @FXML
    public void initialize() {
        cbReader.setValue(cardTerminal);
        cardTerminals.forEach(terminal -> cbReader.getItems().add(terminal));

        executor.scheduleAtFixedRate(task, 2000, 500, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(saveTask, 1, 5, TimeUnit.MINUTES);

        setComboBoxValues();
        initTableColumns();

        tvCurrent.setItems(personTableData);

        baseBorder = tfName.getBorder();


        createFileMap();

        pieGender.getData().add(genderM);
        pieGender.getData().add(genderW);
        pieGender.getData().add(genderD);

        pieGender.getData().forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), ": ", data.pieValueProperty()
                        )
                )
        );

        readCSV();
    }

    private void createFileMap() {
        LocalDate current_date = LocalDate.now();
        files.clear();
        vaccine.getToggles().stream().map(toggle -> ((RadioButton) toggle)).forEach(radioButton -> files.put(radioButton.getText(), new File(filePath + String.format("\\Impfung_%d%02d%02d_HHMM_%s.csv", current_date.getYear() - 2000, current_date.getMonthValue(), current_date.getDayOfMonth(), radioButton.getText()))));
    }

    private void readCSV() {
        personTableData.clear();
        files.forEach((key, value) -> in.readData(value).stream().filter(line -> line.length == 17).forEach(line -> personTableData.add(new PersonTableData(value, line))));
        personTableData.forEach(ptData -> updateGenderChart(ptData.getAnrede()));
    }


    private void setStatus(boolean b) {
        if (b) {
            lblStatus.setText(CARD_FOUND);
            lblStatus.setBackground(new Background(new BackgroundFill(Color.GREEN, new CornerRadii(10.0), new Insets(-5.0))));
        } else {
            lblStatus.setText(NO_CARD_FOUND);
            lblStatus.setBackground(new Background(new BackgroundFill(Color.YELLOW, new CornerRadii(10.0), new Insets(-5.0))));
        }
    }

    @FXML
    public void writePerson() throws IOException {
        if (!validate()) {
            write();
            cleanUp();
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private boolean validate() {
        ArrayList<Boolean> results = new ArrayList<>();
        results.add(validate(cbGender));
        results.add(validate(tfFirstname));
        results.add(validate(tfName));
        results.add(validate(tfBirthDate));
        results.add(validate(tfPlz));
        results.add(validate(tfOrt));
        results.add(validate(tfStreet));
        results.add(validate(tfHouseNumber));
        results.add(validate(tfPhoneNumber));
        results.add(validate(cbPostalContact));
        results.add(validate(cbVaccinationNumber));
        results.add(validate(tfCharge));
        results.add(validate(dpVaccinationDate));
        results.add(validate(cbJUJ));
        results.add(validate(cbCured));

        if (tfTime.getText() != null && !tfTime.getText().isEmpty()) {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            try {
                Date format = dateFormat.parse(tfTime.getText());
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(format);
                tfTime.setText(String.format("%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));
                tfTime.setBorder(baseBorder);
                results.add(false);
            } catch (ParseException e) {
                tfTime.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                results.add(true);
            }
        } else {
            tfTime.setBorder(baseBorder);
        }

        try {
            Long.parseLong(tfPhoneNumber.getText());
            tfPhoneNumber.setBorder(baseBorder);
        } catch (NumberFormatException e) {
            tfPhoneNumber.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            results.add(true);
        }

        if (vaccine.getSelectedToggle() == null) {
            vacBox.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            results.add(true);
        } else {
            vacBox.setBorder(baseBorder);
        }

        return results.contains(true);
    }

    private boolean validate(Control field) {

        boolean error = false;
        String value = "";
        if (field instanceof TextField) {
            value = ((TextField) field).getText();
        } else if (field instanceof ComboBox<?>) {
            //noinspection unchecked
            value = ((ComboBox<String>) field).getValue();
        }

        if (field instanceof DatePicker) {
            String text = ((DatePicker) field).getEditor().getText();
            if (text != null && !text.trim().isEmpty()) {
                if (text.matches("^(?:31([/\\-.])(?:0?[13578]|1[02]|(?:Jan|Mar|May|Jul|Aug|Oct|Dec))\\1|(?:29|30)([/\\-.])(?:0?[1,3-9]|1[0-2]|(?:Jan|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec))\\2)(?:1[6-9]|[2-9]\\d)?\\d{2}$|^29([/\\-.])(?:0?2|Feb)\\3(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:16|[2468][048]|[3579][26])00)$|^(?:0?[1-9]|1\\d|2[0-8])([/\\-.])(?:(?:0?[1-9]|(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep))|(?:1[0-2]|(?:Oct|Nov|Dec)))\\4(?:1[6-9]|[2-9]\\d)?\\d{2}$")) {
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    dateFormat.setLenient(false);

                    try {
                        Date format = dateFormat.parse(text);
                        ((DatePicker) field).setValue(convertToLocalDate(format));
                    } catch (ParseException e) {
                        error = true;
                    }
                }
            } else {
                error = true;
            }
        } else {
            error = value == null || value.trim().isEmpty();
        }

        if (error) {
            field.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        } else {
            field.setBorder(baseBorder);
        }

        return error;
    }

    public LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void write() throws IOException {
        String newLine = String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%02d.%02d.%s %s;%s;%s\n",
                cbGender.getValue(),
                replaceUmlauts(tfFirstname.getText()),
                replaceUmlauts(tfName.getText()),
                tfBirthDate.getText(),
                tfPlz.getText(),
                tfOrt.getText(),
                tfStreet.getText(),
                tfHouseNumber.getText(),
                tfStreetAddition.getText(),
                tfPhoneNumber.getText(),
                tfEmail.getText(),
                cbPostalContact.getValue(),
                cbVaccinationNumber.getValue(),
                tfCharge.getText(),
                dpVaccinationDate.getValue().getDayOfMonth(), dpVaccinationDate.getValue().getMonthValue(), dpVaccinationDate.getValue().getYear(),
                tfTime.getText(),
                cbJUJ.getValue(),
                cbCured.getValue());

        newLine = newLine.replaceAll("null", "");

        personTableData.add(new PersonTableData(files.get(((RadioButton) vaccine.getSelectedToggle()).getText()), newLine.split(";", -1)));

        updateGenderChart(cbGender.getValue());
    }

    private void updateGenderChart(String gender) {
        switch (gender) {
            case "M" -> genderM.setPieValue(genderM.getPieValue() + 1);
            case "W" -> genderW.setPieValue(genderW.getPieValue() + 1);
            default -> genderD.setPieValue(genderD.getPieValue() + 1);
        }
    }

    private String replaceUmlauts(String s) {
        return s.replaceAll("ö", "oe")
                .replaceAll("ä", "ae")
                .replaceAll("ü", "ue")
                .replaceAll("Ö", "Oe")
                .replaceAll("Ä", "Ae")
                .replaceAll("Ü", "Ue");
    }

    private String rearrangeDate(String date) {
        String[] split = String.valueOf(date).split("");
        return split[6] + split[7] + "." + split[4] + split[5] + "." + split[0] + split[1] + split[2] + split[3];
    }

    @FXML
    public void checkAutoSave() {
        btnSave.setDisable(cbAutoSave.isSelected());
    }

    @FXML
    //TODO
    public void openSettings() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings.controller"));
        Parent parent = fxmlLoader.load();
        ((SettingsController) fxmlLoader.getController()).setCurrentFilePath(filePath);

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.getIcons().add(PrefHelper.getInstance().getIcon());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

        filePath = PrefHelper.getInstance().getFilePath();
        createFileMap();
        readCSV();
    }

    @FXML
    public void openAbout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("About.controller"));
        Parent parent = fxmlLoader.load();
        ((AboutController) fxmlLoader.getController()).setHostService(service);

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.getIcons().add(PrefHelper.getInstance().getIcon());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML
    public void setDateToday() {
        dpVaccinationDate.setValue(LocalDate.now());
    }

    public void shutdown() {
        saveData();
    }

    private void saveData() {
        HashMap<File, List<String>> fileAndEntryMap = new HashMap<>();
        files.forEach((key, value) -> fileAndEntryMap.put(value, new ArrayList<>()));

        personTableData.forEach(entity -> {
            String newLine = String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s\n",
                    entity.getAnrede(),
                    entity.getVorname(),
                    entity.getNachname(),
                    entity.getGeburtsdatum(),
                    entity.getPlz(),
                    entity.getOrt(),
                    entity.getStrasse(),
                    entity.getStrasseNr(),
                    entity.getAdresszusatz(),
                    entity.getTelefon(),
                    entity.getEmail(),
                    entity.getBriefkontakt(),
                    entity.getImpfserie(),
                    entity.getCharge(),
                    entity.getImpfdatum(),
                    entity.getErstimpfungJuJ(),
                    entity.getGenesenen_Bescheinigung());

            fileAndEntryMap.get(entity.getVaccine()).add(newLine);
        });

        files.forEach((key, value) -> value.delete());

        fileAndEntryMap.forEach((key, value) -> value.forEach(line -> {
            try {
                out.write(key, line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

    }

    private void initTableColumns() {
        c1.setCellValueFactory(CellValueFactoryHelper.ANREDE);
        c2.setCellValueFactory(CellValueFactoryHelper.VORNAME);
        c3.setCellValueFactory(CellValueFactoryHelper.NACHNAME);
        c4.setCellValueFactory(CellValueFactoryHelper.GEBURTSDATUM);
        c5.setCellValueFactory(CellValueFactoryHelper.PLZ);
        c6.setCellValueFactory(CellValueFactoryHelper.ORT);
        c7.setCellValueFactory(CellValueFactoryHelper.STRASSE);
        c8.setCellValueFactory(CellValueFactoryHelper.STRASSE_NR);
        c9.setCellValueFactory(CellValueFactoryHelper.ADRESSZUSATZ);
        c10.setCellValueFactory(CellValueFactoryHelper.TELEFON);
        c11.setCellValueFactory(CellValueFactoryHelper.EMAIL);
        c12.setCellValueFactory(CellValueFactoryHelper.BRIEFKONTAKT);
        c13.setCellValueFactory(CellValueFactoryHelper.IMPFSERIE);
        c14.setCellValueFactory(CellValueFactoryHelper.CHARGE);
        c15.setCellValueFactory(CellValueFactoryHelper.IMPFDATUM);
        c16.setCellValueFactory(CellValueFactoryHelper.ERSTIMPFUNG_JUJ);
        c17.setCellValueFactory(CellValueFactoryHelper.GENESENEN_BESCHEINIGUNG);

        //noinspection unchecked
        tvCurrent.getColumns().forEach(column -> ((TableColumn<PersonTableData, String>) column).setCellFactory(TextFieldTableCell.forTableColumn()));

        c1.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setAnrede(t.getNewValue()));
        c2.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setVorname(t.getNewValue()));
        c3.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setNachname(t.getNewValue()));
        c4.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setGeburtsdatum(t.getNewValue()));
        c5.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setPlz(t.getNewValue()));
        c6.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setOrt(t.getNewValue()));
        c7.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setStrasse(t.getNewValue()));
        c8.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setStrasseNr(t.getNewValue()));
        c9.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setAdresszusatz(t.getNewValue()));
        c10.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setTelefon(t.getNewValue()));
        c11.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setEmail(t.getNewValue()));
        c12.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setBriefkontakt(t.getNewValue()));
        c13.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setImpfserie(t.getNewValue()));
        c14.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setCharge(t.getNewValue()));
        c15.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setImpfdatum(t.getNewValue()));
        c16.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setErstimpfungJuJ(t.getNewValue()));
        c17.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> getRow(t).setGenesenen_Bescheinigung(t.getNewValue()));
    }

    private PersonTableData getRow(TableColumn.CellEditEvent<PersonTableData, String> t) {
        return t.getTableView().getItems().get(t.getTablePosition().getRow());
    }

    private void setComboBoxValues() {
        cbGender.getItems().add("");
        cbGender.getItems().add("M");
        cbGender.getItems().add("W");
        cbGender.getItems().add("D");

        cbPostalContact.getItems().add("0");
        cbPostalContact.getItems().add("1");
        cbPostalContact.setValue("1");

        cbVaccinationNumber.getItems().add("");
        cbVaccinationNumber.getItems().add("1");
        cbVaccinationNumber.getItems().add("2");
        cbVaccinationNumber.getItems().add("3");

        cbJUJ.getItems().add("0");
        cbJUJ.getItems().add("1");
        cbJUJ.setValue("0");

        cbCured.getItems().add("0");
        cbCured.getItems().add("1");
        cbCured.setValue("0");
    }

    private HostServices service;

    public void setHostService(HostServices service) {
        this.service = service;
    }
}