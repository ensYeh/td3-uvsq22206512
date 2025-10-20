package fr.uvsq.cprog.collex;

/**
 * Thomas Torres 22206512
 * Classe CommandeAdd qui implemente Commande pour ajouter un nouvelle DnsItem dans la liste de Dns
 */
public class CommandeAdd implements Commande {

	private final AdresseIp ip;
	private final NomMachine nom;

	public CommandeAdd(AdresseIp ip, NomMachine nom) {
		this.ip = ip;
		this.nom = nom;
	}

	@Override
	public void executer(Dns dns) {
		try {
			dns.addItem(ip, nom);
		} catch (Exception e) {
			System.err.println("ERREUR : " + e.getMessage());
		}
	}
}
