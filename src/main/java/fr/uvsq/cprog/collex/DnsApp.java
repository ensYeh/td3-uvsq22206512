package fr.uvsq.cprog.collex;

/**
 * Thomas Torres 22206512
 * Classe principale avec la méthode run qui intéragie avec l'interface
 */

public class DnsApp {

	public static void main(String[] args) {
		Dns dns = new Dns();
		DnsTUI tui = new DnsTUI();

		while (true) {
			Commande commande = tui.nextCommande();
			if (commande != null) {
				commande.executer(dns);
			}
		}
	}
}
