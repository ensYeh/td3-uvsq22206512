package fr.uvsq.cprog.collex;

/**
 * Thomas Torres 22206512
 * Classe CommandeGetNom qui implemente Commande qui permet de récupérer le nom de la machine
 * à partir d'une adresse Ip
 */
public class CommandeGetNom implements Commande {
	private final AdresseIp ip;

	public CommandeGetNom(AdresseIp ip) {
		this.ip = ip;
	}

	@Override
	public void executer(Dns dns) {
		DnsItem item = dns.getItem(ip);
		if (item != null) {
			System.out.println(item.getNomMachine());
		} else {
			System.err.println("Adresse IP introuvable : " + ip);
		}
	}
}
