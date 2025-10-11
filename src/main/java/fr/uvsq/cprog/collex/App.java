package fr.uvsq.cprog.collex;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        Dns dns = new Dns();
        dns.afficherBdd();
        DnsItem item1 = dns.getItem(new NomMachine("www.python.org"));
        DnsItem item2 = dns.getItem(new AdresseIp("142.250.74.14"));
    }
}
