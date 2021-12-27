package net.exoa.readerui;

import javafx.beans.property.SimpleStringProperty;

import java.io.File;

public class PersonTableData {

    String Anrede;
    String Vorname;
    String Nachname;
    String Geburtsdatum;
    String Plz;
    String Ort;
    String Strasse;
    String StrasseNr;
    String Adresszusatz;
    String Telefon;
    String Email;
    String Briefkontakt;
    String Impfserie;
    String Charge;
    String Impfdatum;
    String ErstimpfungJuJ;
    String Genesenen_Bescheinigung;
    File vaccine;

    public File getVaccine() {
        return vaccine;
    }

    public void setVaccine(File vaccine) {
        this.vaccine = vaccine;
    }



    public PersonTableData(File vaccine, String... data) {
        this.vaccine = vaccine;

        Anrede = data[0];
        Vorname = data[1].replaceAll("ö", "oe")
                .replaceAll("ä", "ae")
                .replaceAll("ü", "ue")
                .replaceAll("Ö", "Oe")
                .replaceAll("Ä", "Ae")
                .replaceAll("Ü", "Ue");
        Nachname = data[2].replaceAll("ö", "oe")
                .replaceAll("ä", "ae")
                .replaceAll("ü", "ue")
                .replaceAll("Ö", "Oe")
                .replaceAll("Ä", "Ae")
                .replaceAll("Ü", "Ue");
        Geburtsdatum = data[3];
        Plz = data[4];
        Ort = data[5];
        Strasse = data[6];
        StrasseNr = data[7];
        Adresszusatz = data[8];
        Telefon = data[9];
        Email = data[10];
        Briefkontakt = data[11];
        Impfserie = data[12];
        Charge = data[13];
        Impfdatum = data[14];
        ErstimpfungJuJ = data[15];
        Genesenen_Bescheinigung = data[16];
    }


    public String getAnrede() {
        return Anrede;
    }

    public void setAnrede(String anrede) {
        Anrede = anrede;
    }

    public String getVorname() {
        return Vorname;
    }

    public void setVorname(String vorname) {
        Vorname = vorname;
    }

    public String getNachname() {
        return Nachname;
    }

    public void setNachname(String nachname) {
        Nachname = nachname;
    }

    public String getGeburtsdatum() {
        return Geburtsdatum;
    }

    public void setGeburtsdatum(String geburtsdatum) {
        Geburtsdatum = geburtsdatum;
    }

    public String getPlz() {
        return Plz;
    }

    public void setPlz(String plz) {
        Plz = plz;
    }

    public String getOrt() {
        return Ort;
    }

    public void setOrt(String ort) {
        Ort = ort;
    }

    public String getStrasse() {
        return Strasse;
    }

    public void setStrasse(String strasse) {
        Strasse = strasse;
    }

    public String getStrasseNr() {
        return StrasseNr;
    }

    public void setStrasseNr(String strasseNr) {
        StrasseNr = strasseNr;
    }

    public String getAdresszusatz() {
        return Adresszusatz;
    }

    public void setAdresszusatz(String adresszusatz) {
        Adresszusatz = adresszusatz;
    }

    public String getTelefon() {
        return Telefon;
    }

    public void setTelefon(String telefon) {
        Telefon = telefon;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getBriefkontakt() {
        return Briefkontakt;
    }

    public void setBriefkontakt(String briefkontakt) {
        Briefkontakt = briefkontakt;
    }

    public String getImpfserie() {
        return Impfserie;
    }

    public void setImpfserie(String impfserie) {
        Impfserie = impfserie;
    }

    public String getCharge() {
        return Charge;
    }

    public void setCharge(String charge) {
        Charge = charge;
    }

    public String getImpfdatum() {
        return Impfdatum;
    }

    public void setImpfdatum(String impfdatum) {
        Impfdatum = impfdatum;
    }

    public String getErstimpfungJuJ() {
        return ErstimpfungJuJ;
    }

    public void setErstimpfungJuJ(String erstimpfungJuJ) {
        ErstimpfungJuJ = erstimpfungJuJ;
    }

    public String getGenesenen_Bescheinigung() {
        return Genesenen_Bescheinigung;
    }

    public void setGenesenen_Bescheinigung(String genesenen_Bescheinigung) {
        Genesenen_Bescheinigung = genesenen_Bescheinigung;
    }
}
