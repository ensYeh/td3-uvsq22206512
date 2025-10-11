package fr.uvsq.cprog.collex;

public class CommandeQuit implements Commande {

	@Override
	public void executer(Dns dns) {
		System.out.println("Fermeture de l'application...");
		System.exit(0);
	}
}