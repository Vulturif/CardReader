package net.exoa.readerui;

import javafx.beans.property.SimpleStringProperty;

public class PersonTableData {

    SimpleStringProperty Anrede;
    SimpleStringProperty Vorname;
    SimpleStringProperty Nachname;
    SimpleStringProperty Geburtsdatum;
    SimpleStringProperty Plz;
    SimpleStringProperty Ort;
    SimpleStringProperty Strasse;
    SimpleStringProperty StrasseNr;
    SimpleStringProperty Adresszusatz;
    SimpleStringProperty Telefon;
    SimpleStringProperty Email;
    SimpleStringProperty Briefkontakt;
    SimpleStringProperty Impfserie;
    SimpleStringProperty Charge;
    SimpleStringProperty Impfdatum;
    SimpleStringProperty ErstimpfungJuJ;
    SimpleStringProperty Genesenen_Bescheinigung;

    public PersonTableData(String... data) {
        Anrede = new SimpleStringProperty(data[0]);
        Vorname = new SimpleStringProperty(data[1]);
        Nachname = new SimpleStringProperty(data[2]);
        Geburtsdatum = new SimpleStringProperty(data[3]);
        Plz = new SimpleStringProperty(data[4]);
        Ort = new SimpleStringProperty(data[5]);
        Strasse = new SimpleStringProperty(data[6]);
        StrasseNr = new SimpleStringProperty(data[7]);
        Adresszusatz = new SimpleStringProperty(data[8]);
        Telefon = new SimpleStringProperty(data[9]);
        Email = new SimpleStringProperty(data[10]);
        Briefkontakt = new SimpleStringProperty(data[11]);
        Impfserie = new SimpleStringProperty(data[12]);
        Charge = new SimpleStringProperty(data[13]);
        Impfdatum = new SimpleStringProperty(data[14]);
        ErstimpfungJuJ = new SimpleStringProperty(data[15]);
        Genesenen_Bescheinigung = new SimpleStringProperty(data[16]);
    }

    public String getAnrede() {
        return Anrede.get();
    }

    public SimpleStringProperty anredeProperty() {
        return Anrede;
    }

    public void setAnrede(String anrede) {
        this.Anrede.set(anrede);
    }

    public String getVorname() {
        return Vorname.get();
    }

    public SimpleStringProperty vornameProperty() {
        return Vorname;
    }

    public void setVorname(String vorname) {
        this.Vorname.set(vorname);
    }

    public String getNachname() {
        return Nachname.get();
    }

    public SimpleStringProperty nachnameProperty() {
        return Nachname;
    }

    public void setNachname(String nachname) {
        this.Nachname.set(nachname);
    }

    public String getGeburtsdatum() {
        return Geburtsdatum.get();
    }

    public SimpleStringProperty geburtsdatumProperty() {
        return Geburtsdatum;
    }

    public void setGeburtsdatum(String geburtsdatum) {
        this.Geburtsdatum.set(geburtsdatum);
    }

    public String getPlz() {
        return Plz.get();
    }

    public SimpleStringProperty plzProperty() {
        return Plz;
    }

    public void setPlz(String plz) {
        this.Plz.set(plz);
    }

    public String getOrt() {
        return Ort.get();
    }

    public SimpleStringProperty ortProperty() {
        return Ort;
    }

    public void setOrt(String ort) {
        this.Ort.set(ort);
    }

    public String getStrasse() {
        return Strasse.get();
    }

    public SimpleStringProperty strasseProperty() {
        return Strasse;
    }

    public void setStrasse(String strasse) {
        this.Strasse.set(strasse);
    }

    public String getStrasseNr() {
        return StrasseNr.get();
    }

    public SimpleStringProperty strasseNrProperty() {
        return StrasseNr;
    }

    public void setStrasseNr(String strasseNr) {
        this.StrasseNr.set(strasseNr);
    }

    public String getAdresszusatz() {
        return Adresszusatz.get();
    }

    public SimpleStringProperty adresszusatzProperty() {
        return Adresszusatz;
    }

    public void setAdresszusatz(String adresszusatz) {
        this.Adresszusatz.set(adresszusatz);
    }

    public String getTelefon() {
        return Telefon.get();
    }

    public SimpleStringProperty telefonProperty() {
        return Telefon;
    }

    public void setTelefon(String telefon) {
        this.Telefon.set(telefon);
    }

    public String getEmail() {
        return Email.get();
    }

    public SimpleStringProperty emailProperty() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email.set(email);
    }

    public String getBriefkontakt() {
        return Briefkontakt.get();
    }

    public SimpleStringProperty briefkontaktProperty() {
        return Briefkontakt;
    }

    public void setBriefkontakt(String briefkontakt) {
        this.Briefkontakt.set(briefkontakt);
    }

    public String getImpfserie() {
        return Impfserie.get();
    }

    public SimpleStringProperty impfserieProperty() {
        return Impfserie;
    }

    public void setImpfserie(String impfserie) {
        this.Impfserie.set(impfserie);
    }

    public String getCharge() {
        return Charge.get();
    }

    public SimpleStringProperty chargeProperty() {
        return Charge;
    }

    public void setCharge(String charge) {
        this.Charge.set(charge);
    }

    public String getImpfdatum() {
        return Impfdatum.get();
    }

    public SimpleStringProperty impfdatumProperty() {
        return Impfdatum;
    }

    public void setImpfdatum(String impfdatum) {
        this.Impfdatum.set(impfdatum);
    }

    public String getErstimpfungJuJ() {
        return ErstimpfungJuJ.get();
    }

    public SimpleStringProperty erstimpfungJuJProperty() {
        return ErstimpfungJuJ;
    }

    public void setErstimpfungJuJ(String erstimpfungJuJ) {
        this.ErstimpfungJuJ.set(erstimpfungJuJ);
    }

    public String getGenesenen_Bescheinigung() {
        return Genesenen_Bescheinigung.get();
    }

    public SimpleStringProperty genesenen_BescheinigungProperty() {
        return Genesenen_Bescheinigung;
    }

    public void setGenesenen_Bescheinigung(String genesenen_Bescheinigung) {
        this.Genesenen_Bescheinigung.set(genesenen_Bescheinigung);
    }
}
