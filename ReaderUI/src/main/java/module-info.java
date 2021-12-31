module net.vulturif.readerui {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    //requires eu.hansolo.tilesfx;
    requires net.vulturif.readerbl;
    requires java.smartcardio;
    requires java.prefs;

    opens net.vulturif.readerui to javafx.fxml;
    opens net.vulturif.readerui.fxml to javafx.fxml;
    exports net.vulturif.readerui;
    exports net.vulturif.readerui.fxml;
}