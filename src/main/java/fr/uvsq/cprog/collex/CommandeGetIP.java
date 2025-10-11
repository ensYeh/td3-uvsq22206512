package fr.uvsq.cprog.collex;

public class CommandeGetIP implements Commande {
	private final NomMachine nom;

	public CommandeGetIP(NomMachine nom) {
		this.nom = nom;
	}

	@Override
	public void executer(Dns dns) {
		DnsItem item = dns.getItem(nom);
		if (item != null) {
			System.out.println(item.getAdresseIp());
		} else {
			System.err.println("Nom de machine introuvable : " + nom);
		}
	}
}
