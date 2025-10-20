package fr.uvsq.cprog.collex;

import java.util.Scanner;

/**
 * Thomas Torres 22206512
 * Classe DnsTUI qui utilise un scanner pour récupérer les commandes taper par l'utilisateur
 */

public class DnsTUI {

	private final Scanner scanner;

	public DnsTUI() {
		scanner = new Scanner(System.in);
	}

	//lit la prochaine commande utilisateur et retourne l'objet Commande correspondant
	public Commande nextCommande() {
		System.out.print("> ");
		String input = scanner.nextLine().trim();
		return parseCommande(input);
	}

	private void affiche(String input) {
		System.out.println(input);
	}

	//analyse une ligne de commande et crée la Commande correspondante (utilise regex car plus simple)
	private Commande parseCommande(String input) {
		if (input.isEmpty()) {
			return null;
		}

		// Commande quit : quitte l'application
		if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
			affiche("Fermeture de l'application...");
			return new CommandeQuit();
		}

		// Commande add : add IP nom.qualifie.machine
		if (input.startsWith("add ")) {
			String[] parts = input.split("\\s+");
			if (parts.length == 3) {
				AdresseIp ip = new AdresseIp(parts[1]);
				NomMachine nom = new NomMachine(parts[2]);
				return new CommandeAdd(ip, nom);
			}
		}

		// Commande ls : ls [-a] domaine
		if (input.startsWith("ls")) {
			String[] parts = input.split("\\s+");
			boolean triParAdresse = false;
			String domaine;

			if (parts.length == 3 && parts[1].equals("-a")) {
				triParAdresse = true;
				domaine = parts[2];
			} else if (parts.length == 2) {
				domaine = parts[1];
			} else {
				return null;
			}

			return new CommandeList(domaine, triParAdresse);
		}

		//si c'est une IP
		if (input.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
			AdresseIp ip = new AdresseIp(input);
			return new CommandeGetNom(ip);
		}

		//sinon c'est un nom
		return new CommandeGetIP(new NomMachine(input));
  }
}
