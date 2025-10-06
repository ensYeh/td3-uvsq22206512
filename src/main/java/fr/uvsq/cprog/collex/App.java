package fr.uvsq.cprog.collex;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        Dns dns = new Dns("dns_bdd.txt"); // ou null selon ton constructeur
        dns.afficherBdd(); // vérifie que la base est bien remplie
    }
}
