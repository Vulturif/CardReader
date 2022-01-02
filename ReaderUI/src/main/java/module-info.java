module net.vulturif.readerui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    //requires eu.hansolo.tilesfx;
    requires net.vulturif;
    requires java.smartcardio;
    requires java.prefs;
    requires java.desktop;
//    requires org.apache.httpcomponents.httpclient;
//    requires org.apache.httpcomponents.httpcore;
    requires com.google.gson;

    opens net.vulturif.readerui to javafx.fxml;
    opens net.vulturif.readerui.fxml to javafx.fxml;
    opens net.vulturif.readerui.gson.github to com.google.gson;
    exports net.vulturif.readerui;
    exports net.vulturif.readerui.fxml;
    exports net.vulturif.readerui.gson.github;
}