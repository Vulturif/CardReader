package net.vulturif.logic;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.vulturif.shema.egk.Person;
import net.vulturif.shema.egk.UC_PersoenlicheVersichertendatenXML;
import net.vulturif.shema.egk.Versicherter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.smartcardio.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class CardReader {

    private static final Logger logger = LogManager.getLogger(CardReader.class);

    private static final byte[] SELECT_MF = new byte[]{0x00, (byte) 0xA4, 0x04, 0x0C, 0x07, (byte) 0xD2, 0x76, 0x00, 0x01, 0x44, (byte) 0x80, 0x00};
    private static final byte[] SELECT_HCA_EGK = new byte[]{0x00, (byte) 0xA4, 0x04, 0x0C, 0x06, (byte) 0xD2, 0x76, 0x00, 0x00, 0x01, 0x02};
    private static final byte[] SELECT_HCA_PKV = new byte[]{0x00, (byte) 0xA4, 0x04, 0x00, 0x06, (byte) 0xD2, 0x76, 0x00, 0x00, 0x01, 0x01};

    private static final byte[] SELECT_FILE_PD = new byte[]{(byte) 0x00, (byte) 0xB0, (byte) 0x81, (byte) 0X00, (byte) 0x00, 0x00, 0x00};
    private static final byte[] SELECT_FILE_PKV = new byte[]{(byte) 0xb0, 0x00, 0x00, 0x00};

    private final TerminalFactory factory;

    public CardReader() {
        logger.debug("BLA");
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
            case "9000" -> logger.warn("Command successful -Datei gelesen ");
            case "6282" -> logger.warn("Warning: EndOfFileWarning - weniger Daten vorhanden, als mittels Ne angefordert ");
            case "6281" -> logger.warn("Warning: CorruptDataWarning - möglicherweise sind die Antwortdaten korrupt ");
            case "6700" -> logger.warn("Error: WrongLength - Die Anzahl der angeforderten Daten übersteigt die maximale Puffergröße.");
            case "6a82" -> logger.warn("Error: FileNotFound - per shortFileIdentifier adressiertes EF nicht gefunden ");
            case "6986" -> logger.warn("Error: NoCurrentEF - es ist kein EF ausgewählt ");
            case "6982" -> logger.warn("Error: SecurityStatusNotSatisfied - Zugriffsregel nicht erfüllt ");
            case "6981" -> logger.warn("Error: WrongFileType - ausgewähltes EF ist nicht transparent ");
            case "6b00" -> logger.warn("Error: OffsetTooBig - Parameter offset in Kommando APDU ist zu groß ");
            case "6900" -> logger.warn("Error: Command not allowed - Mobiles Kartenterminal: Autorisierung fehlt ");
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

            logger.debug("Card_Info: " + card);

            CommandAPDU SELECT_MF_APDU = new CommandAPDU(SELECT_MF);
            CommandAPDU SELECT_HCA_EKG_APDU = new CommandAPDU(SELECT_HCA_EGK);
            CommandAPDU SELECT_FILE_PD_APDU = new CommandAPDU(SELECT_FILE_PD);

            channel.transmit(SELECT_MF_APDU);
            channel.transmit(SELECT_HCA_EKG_APDU);
            ResponseAPDU rPD = channel.transmit(SELECT_FILE_PD_APDU);


            checkResponseCode(Integer.toHexString(rPD.getSW()));

            if (Integer.toHexString(rPD.getSW()).equals("6a00")) {
                CommandAPDU SELECT_HCA_PKV_APDU = new CommandAPDU(SELECT_HCA_PKV);
                CommandAPDU SELECT_FILE_PKV_APDU = new CommandAPDU(SELECT_FILE_PKV);

                channel.transmit(SELECT_MF_APDU);
                channel.transmit(SELECT_HCA_PKV_APDU);
                ResponseAPDU rPKV = channel.transmit(SELECT_FILE_PKV_APDU);

                Map<Byte, String> byteStringMap = DecodePkv(rPKV.getData());
                logger.debug(byteStringMap);
            } else {
                String pd_data = DecodeVD(rPD.getData());
                card.disconnect(false);

                XmlMapper xmlMapper = new XmlMapper();
                try {
                    return xmlMapper.readValue(pd_data, UC_PersoenlicheVersichertendatenXML.class).getVersicherter().getPerson();
                } catch (UnrecognizedPropertyException e) {
                    return xmlMapper.readValue(pd_data, Versicherter.class).getPerson();
                }
            }
        } catch (CardException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<Byte, String> DecodePkv(byte[] bytes) {
        Map<Byte, String> result = new HashMap<>();

        int start = (bytes[0] == (byte) 0x82 || bytes[0] == (byte) 0x92 || bytes[0] == (byte) 0xa2) ? 30 : 0;
        for (int i = start; i < bytes.length - 1 - 2; i++) //letzte 2 Bytes (ReturnCode) auslassen
        {
            byte tag = bytes[i++];
            int length = ReadLength(bytes, i);
            String value = new String(Arrays.copyOfRange(bytes, i + 1, length), StandardCharsets.ISO_8859_1);

            if (tag != (byte) 0x60) {
                result.put(tag, value);
                i += length;
            }
        }

        return result;
    }

    private int ReadLength(byte[] bytes, int i) {
        byte firstByte = bytes[i];
        if (firstByte == (byte) 0x81)
            return bytes[++i];
        else if (firstByte == (byte) 0x82)
            return (bytes[++i] << 8) + bytes[++i];
        else
            return firstByte;
    }
}

