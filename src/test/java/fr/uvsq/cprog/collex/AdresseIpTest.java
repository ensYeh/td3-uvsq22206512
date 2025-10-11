package fr.uvsq.cprog.collex;

import org.junit.Test;
import static org.junit.Assert.*;


public class AdresseIpTest {

    @Test
    public void testToString() {
        AdresseIp ip = new AdresseIp("192.168.1.1");
        assertEquals("192.168.1.1", ip.toString());
    }

}
