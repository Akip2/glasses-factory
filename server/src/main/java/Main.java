import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Point d'entrée du serveur de production de lunettes.
 * Charge la configuration, initialise l'usine et démarre le serveur MQTT.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Properties config = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.error("Fichier config.properties introuvable");
                return;
            }
            config.load(input);
        } catch (IOException e) {
            logger.error("Erreur au chargement de la config");
            logger.error(e.getMessage());
            return;
        }

        int capacity = Integer.parseInt(config.getProperty("capacity"));
        Usine usine = new Usine(capacity);
        Serveur serveur = new Serveur(usine);

        try {
            String url = config.getProperty("mqtt.url");
            String clientId = config.getProperty("mqtt.clientId");

            serveur.start(url, clientId);

            logger.info("Serveur lancé sur {}", url);
        } catch (MqttException e) {
            logger.error(e.getMessage());
        }
    }
}