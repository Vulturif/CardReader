package net.vulturif.logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Input {

    public List<String[]> readData(File file) {
        if (!file.exists()) {
            return new ArrayList<>();
        }

        boolean firstLine = true;
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                } else {
                    records.add(line.split(";", -1));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }
}
