module net.vulturif.readerui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires net.vulturif;
    requires java.smartcardio;
    requires java.prefs;
    requires java.desktop;
    requires com.google.gson;
    requires java.net.http;

    opens net.vulturif.readerui to javafx.fxml;
    opens net.vulturif.readerui.fxml to javafx.fxml;
    opens net.vulturif.readerui.gson.github to com.google.gson;
    exports net.vulturif.readerui;
    exports net.vulturif.readerui.fxml;
    exports net.vulturif.readerui.gson.github;
}