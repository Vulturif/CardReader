package net.vulturif.shema.egk;

public class UC_PersoenlicheVersichertendatenXML {
    public Versicherter Versicherter;
    public String CDM_VERSION;
    public String xmlns;
    public String text;

    public net.vulturif.shema.egk.Versicherter getVersicherter() {
        return Versicherter;
    }

    public void setVersicherter(net.vulturif.shema.egk.Versicherter versicherter) {
        Versicherter = versicherter;
    }

    public String getCDM_VERSION() {
        return CDM_VERSION;
    }

    public void setCDM_VERSION(String CDM_VERSION) {
        this.CDM_VERSION = CDM_VERSION;
    }

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}