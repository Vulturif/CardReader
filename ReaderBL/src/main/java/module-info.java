module net.exoa.readerbl {
    requires java.smartcardio;
    requires java.xml;

    opens net.exoa.logic;
    exports net.exoa.logic;
}