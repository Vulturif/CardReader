package net.vulturif.util;

import javafx.scene.control.cell.PropertyValueFactory;
import net.vulturif.readerui.PersonTableData;

public class CellValueFactoryHelper {
    public static final PropertyValueFactory<PersonTableData, String> ANREDE = new PropertyValueFactory<>("Anrede");
    public static final PropertyValueFactory<PersonTableData, String> VORNAME = new PropertyValueFactory<>("Vorname");
    public static final PropertyValueFactory<PersonTableData, String> NACHNAME = new PropertyValueFactory<>("Nachname");
    public static final PropertyValueFactory<PersonTableData, String> GEBURTSDATUM = new PropertyValueFactory<>("Geburtsdatum");
    public static final PropertyValueFactory<PersonTableData, String> PLZ = new PropertyValueFactory<>("Plz");
    public static final PropertyValueFactory<PersonTableData, String> ORT = new PropertyValueFactory<>("Ort");
    public static final PropertyValueFactory<PersonTableData, String> STRASSE = new PropertyValueFactory<>("Strasse");
    public static final PropertyValueFactory<PersonTableData, String> STRASSE_NR = new PropertyValueFactory<>("StrasseNr");
    public static final PropertyValueFactory<PersonTableData, String> ADRESSZUSATZ = new PropertyValueFactory<>("Adresszusatz");
    public static final PropertyValueFactory<PersonTableData, String> TELEFON = new PropertyValueFactory<>("Telefon");
    public static final PropertyValueFactory<PersonTableData, String> EMAIL = new PropertyValueFactory<>("Email");
    public static final PropertyValueFactory<PersonTableData, String> BRIEFKONTAKT = new PropertyValueFactory<>("Briefkontakt");
    public static final PropertyValueFactory<PersonTableData, String> IMPFSERIE = new PropertyValueFactory<>("Impfserie");
    public static final PropertyValueFactory<PersonTableData, String> CHARGE = new PropertyValueFactory<>("Charge");
    public static final PropertyValueFactory<PersonTableData, String> IMPFDATUM = new PropertyValueFactory<>("Impfdatum");
    public static final PropertyValueFactory<PersonTableData, String> ERSTIMPFUNG_JUJ = new PropertyValueFactory<>("ErstimpfungJuJ");
    public static final PropertyValueFactory<PersonTableData, String> GENESENEN_BESCHEINIGUNG = new PropertyValueFactory<>("Genesenen_Bescheinigung");
}
