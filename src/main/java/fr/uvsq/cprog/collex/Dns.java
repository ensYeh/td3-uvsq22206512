package fr.uvsq.cprog.collex;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Dns {

    private List<DnsItem> base = new ArrayList<>();

    public Dns(String path) {
        if (path != null) {
            loadBd(path);
        }

    }

    public void loadBd(String path) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(path);
             BufferedReader br = new BufferedReader(new InputStreamReader(input))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parts = ligne.split("\\s+");
                if (parts.length == 2) {
                    base.add(new DnsItem(new AdresseIp(parts[0]), new NomMachine(parts[1])));
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void afficherBdd() {
        for (DnsItem item : base) {
            System.out.println(item.toString());
        }
    }
}