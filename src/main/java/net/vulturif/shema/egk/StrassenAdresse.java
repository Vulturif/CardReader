package net.vulturif.shema.egk;

public class StrassenAdresse {
    public int Postleitzahl;
    public String Ort;
    public Land Land;
    public String Strasse;
    public int Hausnummer;

    public String getPostleitzahl() {
        return String.valueOf(Postleitzahl);
    }

    public void setPostleitzahl(int postleitzahl) {
        Postleitzahl = postleitzahl;
    }

    public String getOrt() {
        return Ort;
    }

    public void setOrt(String ort) {
        Ort = ort;
    }

    public net.vulturif.shema.egk.Land getLand() {
        return Land;
    }

    public void setLand(net.vulturif.shema.egk.Land land) {
        Land = land;
    }

    public String getStrasse() {
        return Strasse;
    }

    public void setStrasse(String strasse) {
        Strasse = strasse;
    }

    public String getHausnummer() {
        return String.valueOf(Hausnummer);
    }

    public void setHausnummer(int hausnummer) {
        Hausnummer = hausnummer;
    }
}