package fr.uvsq.cprog.collex;

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
