package fr.uvsq.cprog.collex;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class DnsTest {
    private Dns dns;
    private Path testFile;

    @Before
    public void setUp() throws IOException {
        dns = new Dns();

        // Vide la base et ajoute deux items de test
        dns.getBase().clear();
        dns.getBase().add(new DnsItem(new AdresseIp("192.168.0.1"), new NomMachine("www.uvsq.fr")));
        dns.getBase().add(new DnsItem(new AdresseIp("10.0.0.2"), new NomMachine("www.ecampus.fr")));

        // Création d'un fichier temporaire pour tester l'écriture
        testFile = Files.createTempFile("dns_bdd_test", ".txt");
        // Si tu veux tester l'écriture dans le fichier, tu peux surcharger getFileNameFromProperties
        // pour retourner testFile.toAbsolutePath().toString()
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

    @Test
    public void testGetItemsPythonDomain() {
        List<DnsItem> items = dns.getItems("uvsq");
        assertTrue(items.stream().anyMatch(item -> item.getNomMachine().toString().equals("www.uvsq.fr")));
    }

    @Test
    public void testGetItemsNullDomain() {
        List<DnsItem> items = dns.getItems(null);
        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    @Test
    public void testGetItemsNonExistingDomain() {
        List<DnsItem> items = dns.getItems("nonexistent");

        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    //Partie AddItem
    @Test
    public void testAddItemNomMachineExistant() {
        AdresseIp ip = new AdresseIp("192.168.0.3");
        NomMachine name = new NomMachine("www.uvsq.fr"); // déjà présent

        dns.addItem(ip, name);

        // vérifie l'absence de doublon
        long count = dns.getBase().stream()
                .filter(item -> item.getNomMachine().equals(name))
                .count();
        assertEquals(1, count);
    }

    @Test
    public void testAddItemIpExistant() {
        AdresseIp ip = new AdresseIp("10.0.0.2"); // déjà présent
        NomMachine name = new NomMachine("pc2.python.org");

        dns.addItem(ip, name);

        // Vérifie qu'il n'y a pas de doublon
        long count = dns.getBase().stream()
                .filter(item -> item.getAdresseIp().equals(ip))
                .count();
        assertEquals(1, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddItemIpMauvaisFormat() {
        AdresseIp ip = new AdresseIp("10666.0666.06.26666"); // déjà présent
        NomMachine name = new NomMachine("pc2.python.org");

        dns.addItem(ip, name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddItemNomMachineInvalide() {
        AdresseIp ip = new AdresseIp("192.168.1.12");
        NomMachine name = new NomMachine("invalidname"); // pas de deux points

        dns.addItem(ip, name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddItemNomMachineInvalideWithOnePoint() {
        AdresseIp ip = new AdresseIp("192.168.1.12");
        NomMachine name = new NomMachine(".."); // pas de deux points

        dns.addItem(ip, name);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testAddItemNomMachineInvalideWithTwoPoint() {
        AdresseIp ip = new AdresseIp("192.168.1.12");
        NomMachine name = new NomMachine("."); // pas de deux points

        dns.addItem(ip, name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddItemNull() {
        dns.addItem(null, null);
    }
}
