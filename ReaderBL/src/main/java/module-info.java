module net.vulturif {
    requires java.smartcardio;
    requires java.xml;
    requires com.fasterxml.jackson.dataformat.xml;

    opens net.vulturif.logic;
    exports net.vulturif.logic;
    exports net.vulturif.shema.egk;
}