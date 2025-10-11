package fr.uvsq.cprog.collex;

import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.InputStream;
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

            Path filePath = Path.of(fileName).toAbsolutePath();

            if (!Files.exists(filePath)) {
                throw new RuntimeException("Fichier de base DNS introuvable : " + filePath);
            }

            List<String> lignes = Files.readAllLines(filePath);

            for (String ligne : lignes) {
                if (ligne.trim().isEmpty()) continue;
                String[] parts = ligne.trim().split("\\s+");
                if (parts.length == 2) {
                    base.add(new DnsItem(
                            new AdresseIp(parts[0]),
                            new NomMachine(parts[1])
                    ));
                }
            }

            System.out.println("Base DNS chargée depuis : " + filePath);

        } catch (IOException e) {
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

    public void addItem(AdresseIp ip, NomMachine name) {
        if (ip == null || name == null) {
            throw new IllegalArgumentException("Adresse IP et NomMachine doivent être non null");
        }

        String fullName = name.toString().trim();

        // Vérifie que le nom contient au moins deux points (machine.domaine.local)
        if (fullName.chars().filter(c -> c == '.').count() < 2) {
            throw new IllegalArgumentException(
                    "Le nom de machine doit être au format 'machine.domaine.local' : " + fullName
            );
        }

        // Vérification des doublons
        for (DnsItem item : base) {
            if (item.getNomMachine().equals(name)) {
                System.err.println("ERREUR : Le nom de machine existe déjà !");
                return;
            }
            if (item.getAdresseIp().equals(ip)) {
                System.err.println("ERREUR : L'adresse IP existe déjà !");
                return;
            }
        }

        try {
            // Ajout à la base en mémoire
            DnsItem newItem = new DnsItem(ip, name);
            base.add(newItem);
            System.out.println("Ajouté avec succès : " + newItem);

            // Lecture du chemin du fichier depuis config.properties
            Properties props = new Properties();
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new RuntimeException("Impossible de trouver config.properties");
                }
                props.load(input);
            }

            String filePathStr = props.getProperty("dns.filename");
            if (filePathStr == null || filePathStr.isBlank()) {
                throw new RuntimeException("Propriété 'dns.filename' manquante dans config.properties");
            }

            Path filePath = Path.of(filePathStr).toAbsolutePath();

            // Écriture dans le fichier
            String line = ip.toString() + " " + fullName + System.lineSeparator();
            Files.write(filePath, line.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'écriture dans le fichier dns_bdd.txt", e);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'ajout du DnsItem", e);
        }
    }

}