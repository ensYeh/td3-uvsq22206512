package fr.uvsq.cprog.collex;

public class DnsItem {

    private AdresseIp adresse;
    private NomMachine name;

    public DnsItem(AdresseIp adresse, NomMachine name) {
        this.adresse = adresse;
        this.name = name;
    }
    public String toString() {
        return adresse.toString() + " " +  name.toString();
    }

}
