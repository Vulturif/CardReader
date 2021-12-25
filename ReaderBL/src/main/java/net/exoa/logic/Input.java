package net.exoa.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Input {

    File file = new File("D:\\tmp\\bla.csv");

    public List<String[]> readData() {
        if (!file.exists()) {
            return null;
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
