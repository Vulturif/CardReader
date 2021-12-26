module net.exoa.readerui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    //requires eu.hansolo.tilesfx;
    requires net.exoa.readerbl;
    requires java.smartcardio;
    requires java.prefs;

    opens net.exoa.readerui to javafx.fxml;
    exports net.exoa.readerui;
}