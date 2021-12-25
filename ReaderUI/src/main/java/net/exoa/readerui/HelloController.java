package net.exoa.readerui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.exoa.logic.CardReader;
import net.exoa.logic.Person;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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


    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);


    CardReader reader = new CardReader();
    List<CardTerminal> cardTerminals;
    CardTerminal cardTerminal;
    TimerTask task;
    Person currentPerson;
    boolean reRead = true;

    public HelloController() throws CardException {
        buildTask();
        cardTerminals = reader.listTerminals();
        cardTerminal = cardTerminals.get(0);
    }

    private void buildTask() {
        task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        boolean isCardPresent = cbReader.getValue().isCardPresent();
                        setStatus(isCardPresent);
                        if (isCardPresent) {
                            if (reRead) {
                                readCard();
                            }
                            reRead = false;
                        } else {
                            reRead = true;
                            if (currentPerson != null) {
//                                clear();
                            }
                            currentPerson = null;
                        }
                    } catch (CardException e) {
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
        if (currentPerson != null) {
            return;
        }
        currentPerson = reader.readCardInTerminal(cardTerminal);

        cbGeschlecht.setValue(currentPerson.getGeschlecht());
        tfVorname.setText(currentPerson.getVorname());
        tfName.setText(currentPerson.getName());
        tfGeburtsdatum.setText(currentPerson.getGeburtsdatum());
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

        executor.scheduleAtFixedRate(task, 3000, 500, TimeUnit.MILLISECONDS);
    }


    private void setStatus(boolean b) {
        if (b) {
            lblStatus.setText("Karte gefunden!");
        } else {
            lblStatus.setText("Keine Karte gefunden!");
        }
    }
}