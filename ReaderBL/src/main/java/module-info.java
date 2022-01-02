module net.vulturif {
    requires java.smartcardio;
    requires java.xml;
//    requires com.google.gson;

    opens net.vulturif.logic;
//    opens net.vulturif.gson.github;
    exports net.vulturif.logic;
//    exports net.vulturif.gson.github;
}