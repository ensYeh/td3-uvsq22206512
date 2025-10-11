package fr.uvsq.cprog.collex;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DnsTest {
    private Dns dns;

    @Before
    public void setUp() {
        dns = new Dns();

        dns.getBase().clear(); // Si nécessaire, assure que la base est vide
        dns.getBase().add(new DnsItem(new AdresseIp("192.168.0.1"), new NomMachine("www.uvsq.fr")));
        dns.getBase().add(new DnsItem(new AdresseIp("10.0.0.2"), new NomMachine("www.ecampus.fr")));
    }


    @Test
    public void testGetItemByNomMachineExists() {
        DnsItem item = dns.getItem(new NomMachine("www.uvsq.fr"));
        assertNotNull(item);
        assertEquals("192.168.0.1", item.getAdresseIp().toString());
    }

    @Test
    public void testGetItemByNomMachineNotExists() {
        DnsItem item = dns.getItem(new NomMachine("www.rust-lang.org"));
        assertNull(item);
    }

    // ------------------- Tests getItem(AdresseIp) -------------------

    @Test
    public void testGetItemByAdresseIpExists() {
        DnsItem item = dns.getItem(new AdresseIp("10.0.0.2"));
        assertNotNull(item);
        assertEquals("www.ecampus.fr", item.getNomMachine().toString());
    }

    @Test
    public void testGetItemByAdresseIpNotExists() {
        DnsItem item = dns.getItem(new AdresseIp("8.8.8.8"));
        assertNull(item);
    }
    

}
