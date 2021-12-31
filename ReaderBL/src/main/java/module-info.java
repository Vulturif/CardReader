module net.vulturif.readerbl {
    requires java.smartcardio;
    requires java.xml;

    opens net.vulturif.logic;
    exports net.vulturif.logic;
}