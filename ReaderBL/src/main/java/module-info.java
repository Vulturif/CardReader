module net.exoa.readerbl {
    requires java.smartcardio;
    requires java.xml;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens net.exoa.logic;
    exports net.exoa.logic;
}