package net.vulturif.shema.egk;

public class Person {
    public int Geburtsdatum;
    public String Vorname;
    public String Nachname;
    public String Geschlecht;
    public StrassenAdresse StrassenAdresse;

    public String getGeburtsdatum() {
        return String.valueOf(Geburtsdatum);
    }

    public void setGeburtsdatum(int geburtsdatum) {
        Geburtsdatum = geburtsdatum;
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

    public String getGeschlecht() {
        return Geschlecht;
    }

    public void setGeschlecht(String geschlecht) {
        Geschlecht = geschlecht;
    }

    public net.vulturif.shema.egk.StrassenAdresse getStrassenAdresse() {
        return StrassenAdresse;
    }

    public void setStrassenAdresse(net.vulturif.shema.egk.StrassenAdresse strassenAdresse) {
        StrassenAdresse = strassenAdresse;
    }
}