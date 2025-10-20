package fr.uvsq.cprog.collex;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Dns {

	private final List<DnsItem> base = new ArrayList<>();
	private final Path fichierDns;

	public Dns() {
		// Récupère le nom du fichier depuis config.properties et initialise le Path
		this.fichierDns = initFileFromProperties();
		loadBd();
	}

	private Path initFileFromProperties() {
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				throw new RuntimeException("Fichier config.properties introuvable !");
			}

			Properties props = new Properties();
			props.load(input);

			String filename = props.getProperty("dns.filename");
			if (filename == null || filename.isBlank()) {
				throw new RuntimeException("Propriété 'dns.filename' absente dans config.properties !");
			}

			// Stockage dans un dossier logique : ~/DnsApp/
			Path chemin = Paths.get(System.getProperty("user.home"), "DnsApp", filename);

			if (!Files.exists(chemin.getParent())) {
				Files.createDirectories(chemin.getParent());
			}

			if (!Files.exists(chemin)) {
				Files.createFile(chemin);
				System.out.println("Fichier DNS créé : " + chemin);
			}

			return chemin;

		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de la lecture du fichier de propriétés", e);
		}
	}

	//Charge la base de données depuis le fichier
	public void loadBd() {
		try {
			base.clear();
			List<String> lignes = Files.readAllLines(fichierDns);

			for (String ligne : lignes) {
				if (ligne.trim().isEmpty()) continue;
				String[] parts = ligne.trim().split("\\s+");
				if (parts.length == 2) {
					// Format attendu : nom_machine adresse_ip
					base.add(new DnsItem(new AdresseIp(parts[1]), new NomMachine(parts[0])));
				}
			}

			System.out.println("Base DNS chargée depuis : " + fichierDns);

		} catch (IOException e) {
			throw new RuntimeException("Erreur lors du chargement de la base DNS", e);
		}
	}

	public DnsItem getItem(NomMachine machine) {
		for (DnsItem item : base) {
			if (item.getNomMachine().equals(machine)) {
				return item;
			}
		}
		return null;
	}

	public DnsItem getItem(AdresseIp ip) {
		for (DnsItem item : base) {
			if (item.getAdresseIp().equals(ip)) {
				return item;
			}
		}
		return null;
	}

	public List<DnsItem> getItems(String domaine) {
		List<DnsItem> result = new ArrayList<>();
		if (domaine == null || domaine.isBlank()) return result;

		for (DnsItem item : base) {
			String fullName = item.getNomMachine().toString();
			int firstDot = fullName.indexOf('.');
			if (firstDot >= 0 && firstDot + 1 < fullName.length()) {
				String domainPart = fullName.substring(firstDot + 1);
				if (domainPart.contains(domaine)) {
					result.add(item);
				}
			}
		}

		if (result.isEmpty()) {
			System.err.println("Aucun DnsItem trouvé pour le domaine : " + domaine);
		}

		return result;
	}

	//Ajoute un nouvel item et met à jour le fichier texte
	public void addItem(AdresseIp ip, NomMachine nom) {

		if (ip == null || nom == null) {
			throw new IllegalArgumentException("Adresse IP et NomMachine doivent être non null");
		}

		String ipString = ip.toString().trim();
		if (!ipString.matches(
			"^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$")) {
			throw new IllegalArgumentException("Adresse IP invalide : " + ipString);
		}


		String fullName = nom.toString().trim();

		String[] parts = fullName.split("\\.");
		if (parts.length < 3 || parts[0].isEmpty() || parts[1].isEmpty() || parts[2].isEmpty()) {
			throw new IllegalArgumentException("Le nom de machine doit être au format 'machine.domaine.local'");
		}

		// Vérification des doublons
		for (DnsItem item : base) {
			if (item.getNomMachine().equals(nom)) {
				System.err.println("ERREUR : Le nom de machine existe déjà !");
				return;
			}
			if (item.getAdresseIp().equals(ip)) {
				System.err.println("ERREUR : L'adresse IP existe déjà !");
				return;
			}
		}

		DnsItem newItem = new DnsItem(ip, nom);
		base.add(newItem);
		System.out.println("Ajouté à la base : " + newItem);

		// Écriture dans le fichier
		try {
			String line = nom + " " + ip + System.lineSeparator();
			Files.write(fichierDns, line.getBytes(), StandardOpenOption.APPEND);
			System.out.println("Fichier DNS mis à jour : " + fichierDns);
		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de l'écriture dans le fichier DNS", e);
		}
	}

	public List<DnsItem> getBase() {
		return base;
	}
}
