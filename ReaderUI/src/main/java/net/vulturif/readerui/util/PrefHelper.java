package net.vulturif.readerui.util;

import javafx.scene.image.Image;
import net.vulturif.readerui.Launcher;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.prefs.Preferences;

public class PrefHelper {

    private static final PrefHelper helper = new PrefHelper();

    public static final String CHECK_FOR_UPDATES = "checkForUpdates";
    public static final String FILE_SAVE_PATH = "path_to_csv";

    private final Preferences prefs = Preferences.userNodeForPackage(Launcher.class);
    final Properties properties = new Properties();


    private PrefHelper() {
        try {
            properties.load(PrefHelper.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public String getVersion() {
        return properties.getProperty("application.version");
    }

    public Image getIcon() {
        return new Image(Objects.requireNonNull(PrefHelper.class.getClassLoader().getResourceAsStream(properties.getProperty("application.icon", "CardReader.png"))));
    }

    public boolean checkForUpdates() {
        return prefs.getBoolean(CHECK_FOR_UPDATES, true);
    }

    public void setCheckForUpdates(boolean check) {
        prefs.putBoolean(CHECK_FOR_UPDATES, check);
    }
}
