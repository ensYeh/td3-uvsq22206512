package fr.uvsq.cprog.collex;


/**
 * Thomas Torres 22206512
 * Classe CommandeQuit qui implemente Commande est utiliser pour fermer l'application
 */
public class CommandeQuit implements Commande {

	@Override
	public void executer(Dns dns) {
		System.exit(0);
	}
}