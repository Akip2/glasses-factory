import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties config = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Fichier config.properties introuvable");
                return;
            }
            config.load(input);
        } catch (IOException e) {
            System.out.println("Erreur au chargement de la config");
            System.out.println(e.getMessage());
            return;
        }

        int capacity = Integer.parseInt(config.getProperty("capacity"));
        Usine usine = new Usine(capacity);
        Serveur serveur = new Serveur(usine);

        System.out.println("---LANCEMENT DU SERVEUR---");
        try {
            String url = config.getProperty("mqtt.url");
            String clientId = config.getProperty("mqtt.clientId");

            serveur.start(url, clientId);

            System.out.println("Serveur lancé");
        } catch (MqttException e) {
            System.out.println(e.getMessage());
        }
    }
}