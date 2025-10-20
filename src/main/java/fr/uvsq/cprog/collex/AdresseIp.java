package fr.uvsq.cprog.collex;

/**
 * Thomas Torres 22206512
 * Classe AdresseIp qui stock les méthodes et la chaine elle même
 */

public class AdresseIp {

	private final String ip;

	public AdresseIp(String ip) {
		this.ip = ip.trim();
	}

	public String toString() {
		return ip;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AdresseIp other = (AdresseIp) o;
		return ip.equals(other.ip);
	}

	@Override
	public int hashCode() {
		return ip.hashCode();
	}
}
