package net.vulturif.readerui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.vulturif.readerui.fxml.CardReaderUiController;
import net.vulturif.readerui.util.PrefHelper;


public class Launcher extends Application {
    @Override
    public void start(Stage stage) {

//        boolean update = false;
//        if(PrefHelper.getInstance().checkForUpdates()) {
//            update = checkForUpdate();
//        }

//        if (update) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/CardReaderUi.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 950, 700);
            stage.setMinHeight(750);
            stage.setMinWidth(1000);
            stage.setTitle("CardReader");
            stage.getIcons().add(PrefHelper.getInstance().getIcon());
            stage.setScene(scene);
            ((CardReaderUiController) fxmlLoader.getController()).setHostService(getHostServices());
            stage.setOnHidden(e -> ((CardReaderUiController) fxmlLoader.getController()).shutdown());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch();
    }

//    private boolean checkForUpdate() {
//        String url = "https://api.github.com/repos/vulturif/CardReader/releases/latest";
//
//        try {
//            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
//            HttpGet request = new HttpGet(url);
//            request.addHeader("content-type", "application/json");
//            HttpResponse result = httpClient.execute(request);
//            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
//            httpClient.close();
//
//            Gson gson = new Gson();
//            Release release = gson.fromJson(json, Release.class);
//
//            System.out.println(release.getTagName());
//
//            if (release.getTagName().equals(PrefHelper.getInstance().getVersion())) {
//                System.out.println("New Version available!");
//
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//                alert.setTitle("CardReader");
//                alert.setHeaderText("Neue Version verfügbar!");
//                alert.setContentText("Jetzt herunterladen und intallieren?");
//
//                ButtonType never = new ButtonType("Nicht mehr anzeigen");
//                ButtonType yes = new ButtonType("Ja");
//                ButtonType no = new ButtonType("Später", ButtonBar.ButtonData.CANCEL_CLOSE);
//
//                alert.getButtonTypes().setAll(never, yes, no);
//
//                Optional<ButtonType> dialogResult = alert.showAndWait();
//                if (dialogResult.isPresent()) {
//                    if (dialogResult.get() == never){
//                        PrefHelper.getInstance().setCheckForUpdates(false);
//                    } else if (dialogResult.get() == yes) {
//
//                        Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
//                        alert2.setTitle("Update wird heruntergeladen");
//                        alert2.setHeaderText("");
//                        alert2.setContentText("Update wird heruntergeladen, bitte warten!");
//                        alert2.show();
//
//                        download(release.getAssets().get(0));
//                        return true;
//                    }
//                }
//            }
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }

//    private void download(Asset asset) throws IOException {
//        new Task<Void>() {
//            @Override
//            protected Void call() throws Exception {
//
//                Path cardReader = Files.createTempFile("cardReader", ".exe");
//
//                try (InputStream in = new URL(asset.getBrowserDownloadUrl()).openStream()) {
//                    Files.copy(in, cardReader, StandardCopyOption.REPLACE_EXISTING);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                System.out.println(cardReader.toFile().getAbsolutePath());
//                Process process = new ProcessBuilder(cardReader.toFile().getAbsolutePath()).start();
//                System.exit(0);
//                return null;
//            }
//        }.run();
//    }
}