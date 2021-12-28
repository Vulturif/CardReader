package net.exoa.readerui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import net.exoa.logic.Input;
import net.exoa.logic.CardReader;
import net.exoa.logic.Output;
import net.exoa.logic.Person;

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

import java.util.prefs.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class HelloController {

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
    private TextField tfTime;
    @FXML
    private ToggleGroup vaccine;
    @FXML
    private TitledPane vacBox;
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
    private final String filePath;

    private List<CardTerminal> cardTerminals;
    CardTerminal cardTerminal;
    TimerTask task;
    TimerTask saveTask;


    Preferences prefs = Preferences.userNodeForPackage(net.exoa.readerui.HelloController.class);

    Border baseBorder;

    private final ObservableList<PersonTableData> personTableData = FXCollections.observableArrayList();

    boolean reRead = true;

    public HelloController() {
        buildTask();
        cardTerminals = reader.listTerminals();
        if (!cardTerminals.isEmpty()) {
            cardTerminal = cardTerminals.get(0);
        }

        String PREF_NAME = "path_to_csv";
        filePath = prefs.get(PREF_NAME, "C:\\tmp");
        File bla = new File(filePath);
        if (!bla.exists()) {
            bla.mkdir();
        }
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
                                lblStatus.setText("Kein Kartenleser vorhanden!");
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
                                    lblStatus.setText("Fehler beim Lesen der Karte!");
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
        cbGeschlecht.setValue(null);
        tfVorname.setText(null);
        tfName.setText(null);
        tfGeburtsdatum.setText(null);
        tfPlz.setText(null);
        tfOrt.setText(null);
        tfStrasse.setText(null);
        tfHausnummer.setText(null);
        tfAdresszusatz.setText(null);
        tfTelefon.setText(null);
        tfEmail.setText(null);
        cbBriefkontakt.setValue("1");
        cbImpfserie.setValue("");
        tfCharge.setText(null);
        dpImpfdatum.setValue(null);
        tfTime.setText(null);
        cbJUJ.setValue("0");
        cbGenesen.setValue("0");
        vaccine.selectToggle(null);
    }

    private void readCard() {
        Person currentPerson = reader.readCardInTerminal(cbReader.getValue());

        if (currentPerson == null) {
            return;
        }
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
        cbReader.setValue(cardTerminal);
        cardTerminals.forEach(terminal -> cbReader.getItems().add(terminal));

        executor.scheduleAtFixedRate(task, 2000, 500, TimeUnit.MILLISECONDS);
        executor.scheduleAtFixedRate(saveTask, 1, 5, TimeUnit.MINUTES);

        setChoiceBoxValues();
        initTableColumns();

        tvCurrent.setItems(personTableData);

        baseBorder = tfName.getBorder();

        LocalDate current_date = LocalDate.now();
        vaccine.getToggles().stream().map(toggle -> ((RadioButton) toggle)).forEach(radioButton -> files.put(radioButton.getText(), new File(filePath + String.format("\\Impfung_%d%02d%02d_HHMM_%s.csv", current_date.getYear() - 2000, current_date.getMonthValue(), current_date.getDayOfMonth(), radioButton.getText()))));

        readCSV();
    }

    private void readCSV() {
        files.forEach((key, value) -> in.readData(value).stream().filter(line -> line.length == 17).forEach(line -> personTableData.add(new PersonTableData(value, line))));
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
        if (!validate()) {
            write();
            cleanUp();
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private boolean validate() {
        ArrayList<Boolean> results = new ArrayList<>();
        results.add(validate(cbGeschlecht));
        results.add(validate(tfVorname));
        results.add(validate(tfName));
        results.add(validate(tfGeburtsdatum));
        results.add(validate(tfPlz));
        results.add(validate(tfOrt));
        results.add(validate(tfStrasse));
        results.add(validate(tfHausnummer));
        results.add(validate(tfTelefon));
        results.add(validate(cbBriefkontakt));
        results.add(validate(cbImpfserie));
        results.add(validate(tfCharge));
        results.add(validate(dpImpfdatum));
        results.add(validate(cbJUJ));
        results.add(validate(cbGenesen));

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
            Integer.parseInt(tfTelefon.getText());
            tfTelefon.setBorder(baseBorder);
        } catch (NumberFormatException e) {
            tfTelefon.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
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
        } else if (field instanceof ChoiceBox<?>) {
            //noinspection unchecked
            value = ((ChoiceBox<String>) field).getValue();
        }

        if (field instanceof DatePicker) {
            LocalDate date = ((DatePicker) field).getValue();
//            if (date == null) {
            String text = ((DatePicker) field).getEditor().getText();
            if (text != null && !text.trim().isEmpty()) {
                if (text.matches("^(?:31([/\\-.])(?:0?[13578]|1[02]|(?:Jan|Mar|May|Jul|Aug|Oct|Dec))\\1|(?:29|30)([/\\-.])(?:0?[1,3-9]|1[0-2]|(?:Jan|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec))\\2)(?:1[6-9]|[2-9]\\d)?\\d{2}$|^29([/\\-.])(?:0?2|Feb)\\3(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:16|[2468][048]|[3579][26])00)$|^(?:0?[1-9]|1\\d|2[0-8])([/\\-.])(?:(?:0?[1-9]|(?:Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep))|(?:1[0-2]|(?:Oct|Nov|Dec)))\\4(?:1[6-9]|[2-9]\\d)?\\d{2}$")) {
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    dateFormat.setLenient(false);

                    try {
                        Date format = dateFormat.parse(text);
                        ((DatePicker) field).setValue(convertToLocalDateViaMilisecond(format));
//                        final Calendar calendar = Calendar.getInstance();
                    } catch (ParseException e) {
                        error = true;
                    }
                }
            } else {
                error = true;
            }
//            }
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

    public LocalDate convertToLocalDateViaMilisecond(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private void write() throws IOException {
        String newLine = String.format("%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%s;%02d.%02d.%s %s;%s;%s\n",
                cbGeschlecht.getValue(),
                tfVorname.getText().replaceAll("ö", "oe")
                        .replaceAll("ä", "ae")
                        .replaceAll("ü", "ue")
                        .replaceAll("Ö", "Oe")
                        .replaceAll("Ä", "Ae")
                        .replaceAll("Ü", "Ue"),
                tfName.getText().replaceAll("ö", "oe")
                        .replaceAll("ä", "ae")
                        .replaceAll("ü", "ue")
                        .replaceAll("Ö", "Oe")
                        .replaceAll("Ä", "Ae")
                        .replaceAll("Ü", "Ue"),
                tfGeburtsdatum.getText(),
                tfPlz.getText(),
                tfOrt.getText(),
                tfStrasse.getText(),
                tfHausnummer.getText(),
                tfAdresszusatz.getText(),
                tfTelefon.getText(),
                tfEmail.getText(),
                cbBriefkontakt.getValue(),
                cbImpfserie.getValue(),
                tfCharge.getText(),
                dpImpfdatum.getValue().getDayOfMonth(), dpImpfdatum.getValue().getMonthValue(), dpImpfdatum.getValue().getYear(),
                tfTime.getText(),
                cbJUJ.getValue(),
                cbGenesen.getValue());

        newLine = newLine.replaceAll("null", "");

        personTableData.add(new PersonTableData(files.get(((RadioButton) vaccine.getSelectedToggle()).getText()), newLine.split(";", -1)));
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
    public void openSettings() {

    }

    public void shutdown() {
        saveData();
    }

    private void saveData() {
        HashMap<File, List<String>> blub = new HashMap<>();
        files.forEach((key, value) -> blub.put(value, new ArrayList<>()));

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

            blub.get(entity.getVaccine()).add(newLine);
        });

        files.forEach((key, value) -> value.delete());

        blub.forEach((key, value) -> value.forEach(line -> {
            try {
                out.write(key, line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));

    }

    private void initTableColumns() {
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

        setCellFactory(c1);
        setCellFactory(c2);
        setCellFactory(c3);
        setCellFactory(c4);
        setCellFactory(c5);
        setCellFactory(c6);
        setCellFactory(c7);
        setCellFactory(c8);
        setCellFactory(c9);
        setCellFactory(c10);
        setCellFactory(c11);
        setCellFactory(c12);
        setCellFactory(c13);
        setCellFactory(c14);
        setCellFactory(c15);
        setCellFactory(c16);
        setCellFactory(c17);

        c1.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setAnrede(t.getNewValue()));
        c2.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setVorname(t.getNewValue()));
        c3.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setNachname(t.getNewValue()));
        c4.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setGeburtsdatum(t.getNewValue()));
        c5.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setPlz(t.getNewValue()));
        c6.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setOrt(t.getNewValue()));
        c7.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setStrasse(t.getNewValue()));
        c8.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setStrasseNr(t.getNewValue()));
        c9.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setAdresszusatz(t.getNewValue()));
        c10.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setTelefon(t.getNewValue()));
        c11.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setEmail(t.getNewValue()));
        c12.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setBriefkontakt(t.getNewValue()));
        c13.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setImpfserie(t.getNewValue()));
        c14.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setCharge(t.getNewValue()));
        c15.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setImpfdatum(t.getNewValue()));
        c16.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setErstimpfungJuJ(t.getNewValue()));
        c17.setOnEditCommit((TableColumn.CellEditEvent<PersonTableData, String> t) -> t.getTableView().getItems().get(t.getTablePosition().getRow()).setGenesenen_Bescheinigung(t.getNewValue()));
    }

    private void setChoiceBoxValues() {
        cbGeschlecht.getItems().add("");
        cbGeschlecht.getItems().add("M");
        cbGeschlecht.getItems().add("W");
        cbGeschlecht.getItems().add("D");

        cbBriefkontakt.getItems().add("0");
        cbBriefkontakt.getItems().add("1");
        cbBriefkontakt.setValue("1");

        cbImpfserie.getItems().add("");
        cbImpfserie.getItems().add("1");
        cbImpfserie.getItems().add("2");
        cbImpfserie.getItems().add("3");

        cbJUJ.getItems().add("0");
        cbJUJ.getItems().add("1");
        cbJUJ.setValue("0");

        cbGenesen.getItems().add("0");
        cbGenesen.getItems().add("1");
        cbGenesen.setValue("0");
    }

    private void setCellFactory(TableColumn<PersonTableData, String> c) {
        c.setCellFactory(TextFieldTableCell.forTableColumn());
    }
}