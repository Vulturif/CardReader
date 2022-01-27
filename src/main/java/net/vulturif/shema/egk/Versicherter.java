package net.vulturif.shema.egk;

public class Versicherter {
    public String Versicherten_ID;
    public Person Person;

    public String getVersicherten_ID() {
        return Versicherten_ID;
    }

    public void setVersicherten_ID(String versicherten_ID) {
        Versicherten_ID = versicherten_ID;
    }

    public Person getPerson() {
        return Person;
    }

    public void setPerson(Person person) {
        Person = person;
    }
}