package net.vulturif.util;

import com.google.gson.Gson;
import javafx.concurrent.Task;
import net.vulturif.logic.CardReader;
import net.vulturif.shema.github.Release;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Updater {

    private static final Logger logger = LogManager.getLogger(CardReader.class);

    private static final String url = "https://api.github.com/repos/vulturif/CardReader/releases/latest";
    private Release release;

    public Updater() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            Gson gson = new Gson();
            release = gson.fromJson(response.body(), Release.class);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public boolean checkForUpdate() {
        return !release.getTagName().equals(PrefHelper.getInstance().getVersion());
    }

    public void download() {
        new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                Path cardReader = Files.createTempFile("cardReader", ".exe");

                try (InputStream in = new URL(release.getAssets().get(0).getBrowserDownloadUrl()).openStream()) {
                    Files.copy(in, cardReader, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                logger.debug(cardReader.toFile().getAbsolutePath());
                new ProcessBuilder(cardReader.toFile().getAbsolutePath()).start();
                System.exit(0);
                return null;
            }
        }.run();
    }
}
