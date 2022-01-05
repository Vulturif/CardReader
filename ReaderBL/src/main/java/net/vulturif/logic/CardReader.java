package net.vulturif.logic;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.vulturif.shema.egk.Person;
import net.vulturif.shema.egk.Versicherter;

import javax.smartcardio.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class CardReader {

    private static final byte[] SELECT_MF = new byte[]{0x00, (byte) 0xA4, 0x04, 0x0C, 0x07, (byte) 0xD2, 0x76, 0x00, 0x01, 0x44, (byte) 0x80, 0x00};
    private static final byte[] SELECT_HCA = new byte[]{0x00, (byte) 0xA4, 0x04, 0x0C, 0x06, (byte) 0xD2, 0x76, 0x00, 0x00, 0x01, 0x02};
    private static final byte[] SELECT_FILE_PD = new byte[]{(byte) 0x00, (byte) 0xB0, (byte) 0x81, (byte) 0X00, (byte) 0x00, 0x00, 0x00};

    private final TerminalFactory factory;

    public CardReader() {
        factory = TerminalFactory.getDefault();
    }

    private static String DecodeVD(byte[] bytes) throws IOException {
        int length = (bytes[0] << 8) + bytes[1];
        byte[] bytes1 = Arrays.copyOfRange(bytes, 2, length);
        return decompress(bytes1);
    }

    private static String decompress(byte[] epaData) throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(epaData);
        GZIPInputStream gis = new GZIPInputStream(in);
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "ISO-8859-15"));

        StringBuilder buffer = new StringBuilder();
        int data = bf.read();
        while (data != -1) {
            buffer.append((char) data);
            try {
                data = bf.read();
            } catch (EOFException e) {
                break;
            }
        }

        return buffer.toString();
    }

    private static void checkResponseCode(String sw) {
        switch (sw) {
            case "9000" -> System.out.println("Command successful -Datei gelesen ");
            case "6282" -> System.out.println("Warning: EndOfFileWarning - weniger Daten vorhanden, als mittels Ne angefordert ");
            case "6281" -> System.out.println("Warning: CorruptDataWarning - möglicherweise sind die Antwortdaten korrupt ");
            case "6700" -> System.out.println("Error: WrongLength - Die Anzahl der angeforderten Daten übersteigt die maximale Puffergröße.");
            case "6a82" -> System.out.println("Error: FileNotFound - per shortFileIdentifier adressiertes EF nicht gefunden ");
            case "6986" -> System.out.println("Error: NoCurrentEF - es ist kein EF ausgewählt ");
            case "6982" -> System.out.println("Error: SecurityStatusNotSatisfied - Zugriffsregel nicht erfüllt ");
            case "6981" -> System.out.println("Error: WrongFileType - ausgewähltes EF ist nicht transparent ");
            case "6b00" -> System.out.println("Error: OffsetTooBig - Parameter offset in Kommando APDU ist zu groß ");
            case "6900" -> System.out.println("Error: Command not allowed - Mobiles Kartenterminal: Autorisierung fehlt ");
            default -> throw new IllegalStateException("Unexpected value: " + sw);
        }
    }

    public List<CardTerminal> listTerminals() {
        try {
            return factory.terminals().list();
        } catch (CardException e) {
            return new ArrayList<>();
        }
    }

    public Person readCardInTerminal(CardTerminal terminal) {
        if (terminal == null) {
            return null;
        }
        try {
            Card card = terminal.connect("T=1");
            CardChannel channel = card.getBasicChannel();

            System.out.println("Card_Info: " + card);

            CommandAPDU SELECT_MF_APDU = new CommandAPDU(SELECT_MF);
            CommandAPDU SELECT_HCA_APDU = new CommandAPDU(SELECT_HCA);
            CommandAPDU SELECT_FILE_PD_APDU = new CommandAPDU(SELECT_FILE_PD);

            channel.transmit(SELECT_MF_APDU);
            channel.transmit(SELECT_HCA_APDU);
            ResponseAPDU rPD = channel.transmit(SELECT_FILE_PD_APDU);

            checkResponseCode(Integer.toHexString(rPD.getSW()));

            String pd_data = DecodeVD(rPD.getData());

            card.disconnect(false);

            XmlMapper xmlMapper = new XmlMapper();
            return xmlMapper.readValue(pd_data, Versicherter.class).getPerson();
        } catch (CardException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

