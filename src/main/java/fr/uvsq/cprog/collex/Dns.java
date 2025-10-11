package fr.uvsq.cprog.collex;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Dns {

    private List<DnsItem> base = new ArrayList<>();

    public Dns() {
        loadBd();
    }

    private String getFileNameFromProperties(String propertiesFile, String propertyKey) {
        Properties props = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesFile)) {
            if (input == null) {
                throw new RuntimeException("Fichier de propriétés introuvable : " + propertiesFile);
            }
            props.load(input);
            String value = props.getProperty(propertyKey);
            if (value == null) {
                throw new RuntimeException("Clé " + propertyKey + " absente du fichier " + propertiesFile);
            }
            return value;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier de propriétés", e);
        }
    }

    public void loadBd() {
        try {
            String fileName = getFileNameFromProperties("config.properties", "dns.filename");

            URL resourceUrl = getClass().getClassLoader().getResource(fileName);
            if (resourceUrl == null) {
                throw new RuntimeException("Fichier de base DNS introuvable : " + fileName);
            }

            Path filePath = Paths.get(resourceUrl.toURI());
            List<String> lignes = Files.readAllLines(filePath);

            for (String ligne : lignes) {
                if (ligne.trim().isEmpty()) continue;
                String[] parts = ligne.trim().split("\\s+");
                if (parts.length == 2) {
                    base.add(new DnsItem(
                            new AdresseIp(parts[1]),
                            new NomMachine(parts[0])
                    ));
                }
            }

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Erreur lors du chargement de la base DNS", e);
        }
    }

    public DnsItem getItem(NomMachine machine) {
        for (DnsItem item : base) {
            if (item.getNomMachine().equals(machine)){
                return item;
            }
        }
        return null;
    }

    public DnsItem getItem(AdresseIp ip) {
        for (DnsItem item : base) {
            if (item.getAdresseIp().equals(ip)){
                return item;
            }
        }
        return null;
    }

    public void afficherBdd() {
        for (DnsItem item : base) {
            System.out.println(item.toString());
        }
    }

    public List<DnsItem> getBase() {
        return base;
    }

    public List<DnsItem> getItems(String domaine) {
        List<DnsItem> result = new ArrayList<>();

        if (domaine == null || domaine.isBlank()) {
            return result;
        }

        for (DnsItem item : base) {
            if (item != null && item.getNomMachine() != null) {
                String fullName = item.getNomMachine().toString(); // ex: machine.domaine.local
                int firstDot = fullName.indexOf('.');
                if (firstDot >= 0 && firstDot + 1 < fullName.length()) {
                    String domainPart = fullName.substring(firstDot + 1); // partie après le premier point
                    if (domainPart.contains(domaine)) {
                        result.add(item);
                    }
                }
            }
        }

        if (result.isEmpty()) {
            System.err.println("Aucun DnsItem trouvé pour le domaine : " + domaine);
        }

        return result;
    }

}