package net.exoa.logic;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class Output {

    public static final String HEADER = "Anrede (M,F,D);Vorname;Nachname;Geburtsdatum;Plz;Ort;Strasse;StrasseNr;Adresszusatz;Telefon;Email;Briefkontakt (1,0);Impfserie(1,2,3);Charge;Impfdatum(TT.MM.JJJJ H24:MI);Erstimpfung Johnson&Johnson (1,0);Genesenen-Bescheinigung (1,0)\n";

    public void write(File file, String newLine) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
            write(file, HEADER);
        }

        try (FileWriter pw = new FileWriter(file, StandardCharsets.UTF_8, true)) {
            pw.append(newLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
