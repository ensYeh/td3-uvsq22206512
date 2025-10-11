package fr.uvsq.cprog.collex;

import java.util.Comparator;
import java.util.List;

public class CommandeList implements Commande {
	private final String domaine;
	private final boolean triParAdresse;

	public CommandeList(String domaine, boolean triParAdresse) {
		this.domaine = domaine;
		this.triParAdresse = triParAdresse;
	}

	@Override
	public void executer(Dns dns) {
		List<DnsItem> items = dns.getItems(domaine);
		if (items.isEmpty()) {
			System.err.println("Aucune machine trouvée pour le domaine : " + domaine);
			return;
		}

		if (triParAdresse) {
			items.sort(Comparator.comparing(item -> item.getAdresseIp().toString()));
		} else {
			items.sort(Comparator.comparing(item -> item.getNomMachine().toString()));
		}

		for (DnsItem item : items) {
			System.out.println(item.getAdresseIp() + " " + item.getNomMachine());
		}
	}
}
