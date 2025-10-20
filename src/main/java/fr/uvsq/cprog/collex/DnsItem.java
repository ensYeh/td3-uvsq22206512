package fr.uvsq.cprog.collex;
/**
 * Thomas Torres 22206512
 * Classe DnsItem qui stock l'ip et le nom de la machine, ainsi que des getteur ...
 */
public class DnsItem {

	private final AdresseIp ip;
	private final NomMachine name;

	public DnsItem(AdresseIp ip, NomMachine name) {
		this.ip = ip;
		this.name = name;
	}

	public String toString() {
		return ip.toString() + " " + name.toString();
	}

	public NomMachine getNomMachine() {
		return name;
	}

	public AdresseIp getAdresseIp() {
		return ip;
	}
}
