package fr.uvsq.cprog.collex;

import org.junit.Test;
import static org.junit.Assert.*;

public class DnsItemTest {
    @Test
    public void testConstructorAndGetters() {
        AdresseIp ip = new AdresseIp("192.168.1.1");
        NomMachine name = new NomMachine("machine1");

        DnsItem item = new DnsItem(ip, name);

        assertNotNull(item);
        assertEquals(ip, item.getAdresseIp());
        assertEquals(name, item.getNomMachine());
    }

    @Test
    public void testToString() {
        AdresseIp ip = new AdresseIp("10.0.0.2");
        NomMachine name = new NomMachine("machine2");

        DnsItem item = new DnsItem(ip, name);

        String expected = "10.0.0.2 machine2";
        assertEquals(expected, item.toString());
    }

    @Test
    public void testToStringWithWhitespace() {//gérer par le .trim()
        AdresseIp ip = new AdresseIp("  127.0.0.1  ");
        NomMachine name = new NomMachine("  localhost  ");

        DnsItem item = new DnsItem(ip, name);

        String expected = "127.0.0.1 localhost";
        assertEquals(expected, item.toString());
    }
}
