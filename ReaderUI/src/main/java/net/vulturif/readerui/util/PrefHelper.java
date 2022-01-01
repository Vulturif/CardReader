package net.vulturif.readerui.util;

import net.vulturif.readerui.Launcher;

import java.io.File;
import java.util.prefs.Preferences;

public class PrefHelper {

    private static final PrefHelper helper = new PrefHelper();
    private final Preferences prefs = Preferences.userNodeForPackage(Launcher.class);
    String FILE_SAVE_PATH = "path_to_csv";

    private PrefHelper() {
    }

    public static PrefHelper getInstance() {
        return helper;
    }

    public String getFilePath() {

        String filePath = prefs.get(FILE_SAVE_PATH, "C:\\tmp");
        File bla = new File(filePath);
        if (!bla.exists()) {
            //noinspection ResultOfMethodCallIgnored
            bla.mkdir();
        }
        return filePath;
    }

    public void setFilePath(String newPath) {
        prefs.put(FILE_SAVE_PATH, newPath);
    }

}
