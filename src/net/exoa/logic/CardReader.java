package net.exoa.logic;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.smartcardio.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static java.lang.Integer.getInteger;

public class CardReader {

    private static final byte[] SELECT_MF = new byte[]{0x00, (byte) 0xA4, 0x04, 0x0C, 0x07, (byte) 0xD2, 0x76, 0x00, 0x01, 0x44, (byte) 0x80, 0x00};
    private static final byte[] SELECT_HCA = new byte[]{0x00, (byte) 0xA4, 0x04, 0x0C, 0x06, (byte) 0xD2, 0x76, 0x00, 0x00, 0x01, 0x02};
    private static final byte[] SELECT_FILE_PD = new byte[]{(byte) 0x00, (byte) 0xB0, (byte) 0x81,(byte) 0X00,(byte) 0x00, 0x00, 0x00};

    public static void main(String[] args) {
        listCounted();
    }

    public static void listCounted() {
        /* we use the default TerminalFactory */
        TerminalFactory factory = TerminalFactory.getDefault();
        try {
            /* We can have multiple terminals on one System, so we get a list */
            List<CardTerminal> terminals = factory.terminals().list();

            for (CardTerminal terminal : terminals) {
                System.out.println("——————————————–");
                System.out.println("Card_Terminal_Name: "+ terminal.getName());
                System.out.println("Card_in_Terminal_present: "+terminal.isCardPresent());
                System.out.println("——————————————–");
                Card card = terminal.connect("T=1");
                CardChannel channel = card.getBasicChannel();

                System.out.println("Card_Info: "+ card);



                CommandAPDU SELECT_MF_APDU = new CommandAPDU(SELECT_MF);
                CommandAPDU SELECT_HCA_APDU = new CommandAPDU(SELECT_HCA);
                CommandAPDU SELECT_FILE_PD_APDU = new CommandAPDU(SELECT_FILE_PD);

                channel.transmit(SELECT_MF_APDU);
                channel.transmit(SELECT_HCA_APDU);
                ResponseAPDU rPD = channel.transmit(SELECT_FILE_PD_APDU);

                checkResponseCode(Integer.toHexString(rPD.getSW()));

                String pd_data = DecodeVD(rPD.getData());

                card.disconnect(false);

                Person person = parseXml(pd_data);

                Output.write(person);
            }

        } catch (CardException | IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    private static String DecodeVD(byte[] bytes) throws IOException {
        int length = (bytes[0]<<8) + bytes[1];
        byte[] bytes1 = Arrays.copyOfRange(bytes, 2, length);
        return decompress(bytes1);
    }

    private static String decompress(byte[] epaData) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(epaData);
        GZIPInputStream gis = new GZIPInputStream(in);
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "ISO-8859-15"));

        StringBuilder buffer = new StringBuilder();
        int data = bf.read();
        while(data != -1) {
            buffer.append((char) data);
            try {
                data = bf.read();
            } catch (EOFException e) {
                break;
            }
        }

        System.out.println("Data: " + buffer);
        return buffer.toString();
    }

    private static Person parseXml(String pd_data) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(pd_data));

        Document doc = db.parse(is);
        NodeList nodes = doc.getElementsByTagName("UC_PersoenlicheVersichertendatenXML");
        Element xml_person = (Element) ((Element) nodes.item(0).getFirstChild()).getElementsByTagName("Person").item(0);

        NodeList childNodes = xml_person.getChildNodes();
        Person person = new Person();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Element element = (Element) childNodes.item(i);
            switch (element.getTagName()) {
                case "Geburtsdatum":
                    person.setGeburtsdatum(getCharacterDataFromElement(element));
                    break;
                case "Vorname":
                    person.setVorname(getCharacterDataFromElement(element));
                    break;
                case "Nachname":
                    person.setName(getCharacterDataFromElement(element));
                    break;
                case "Geschlecht":
                    person.setGeschlecht(getCharacterDataFromElement(element));
                    break;
                case "StrassenAdresse":
                    NodeList address_nodes = element.getChildNodes();
                    for (int j = 0; j < address_nodes.getLength(); j++) {
                        Element addresselement = (Element) address_nodes.item(j);
                        switch (addresselement.getTagName()) {
                            case "Postleitzahl":
                                person.setPlz(getCharacterDataFromElement(addresselement));
                                break;
                            case "Ort":
                                person.setOrt(getCharacterDataFromElement(addresselement));
                                break;
                            case "Strasse":
                                person.setStrasse(getCharacterDataFromElement(addresselement));
                                break;
                            case "Hausnummer":
                                person.setHausnummer(getCharacterDataFromElement(addresselement));
                        }
                    }
                    break;
            }
        }

        System.out.println(person);

        return person;
    }

    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "?";
    }

    private static void checkResponseCode(String sw) {
        switch (sw) {
            case "9000":
                System.out.println("Command successful -Datei gelesen ");
                break;
            case "6282":
                System.out.println("Warning: EndOfFileWarning - weniger Daten vorhanden, als mittels Ne angefordert ");
                break;
            case "6281":
                System.out.println("Warning: CorruptDataWarning - möglicherweise sind die Antwortdaten korrupt ");
                break;
            case "6700":
                System.out.println("Error: WrongLength - Die Anzahl der angeforderten Daten übersteigt die maximale Puffergröße.");
                break;
            case "6a82":
                System.out.println("Error: FileNotFound - per shortFileIdentifier adressiertes EF nicht gefunden ");
                break;
            case "6986":
                System.out.println("Error: NoCurrentEF - es ist kein EF ausgewählt ");
                break;
            case "6982":
                System.out.println("Error: SecurityStatusNotSatisfied - Zugriffsregel nicht erfüllt ");
                break;
            case "6981":
                System.out.println("Error: WrongFileType - ausgewähltes EF ist nicht transparent ");
                break;
            case "6b00":
                System.out.println("Error: OffsetTooBig - Parameter offset in Kommando APDU ist zu groß ");
                break;
            case "6900":
                System.out.println("Error: Command not allowed - Mobiles Kartenterminal: Autorisierung fehlt ");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + sw);
        }
    }
}

