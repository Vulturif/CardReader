module net.vulturif {
    requires java.smartcardio;
    requires com.fasterxml.jackson.dataformat.xml;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires com.google.gson;
    requires java.prefs;
    requires java.net.http;
    requires jdk.crypto.cryptoki;

    opens net.vulturif.readerui to javafx.fxml;
    opens net.vulturif.readerui.fxml to javafx.fxml;
    opens net.vulturif.shema.github to com.google.gson;
    opens net.vulturif.logic;
    exports net.vulturif.logic;
    exports net.vulturif.readerui;
    exports net.vulturif.readerui.fxml;
    exports net.vulturif.shema.github;
    exports net.vulturif.shema.egk;
    exports net.vulturif.util;
}